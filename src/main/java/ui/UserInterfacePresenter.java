package ui;

import data.api.ApiTool;
import data.api.BaseApiTool;
import data.api.api_objects.*;
import data.database.BaseDatabaseTool;
import data.database.DatabaseTool;
import datastructure.ItemNode;
import datastructure.TreeHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Constants;

import java.sql.SQLException;
import java.util.*;

public class UserInterfacePresenter implements BaseUserInterfacePresenter {

    private BaseUserInterface view;
    private BaseDatabaseTool databaseTool;
    private BaseApiTool apiTool;
    private int currentBuild;

    private int currentPos = 1;
    private int maxPos;

    public UserInterfacePresenter(BaseUserInterface ui) throws SQLException {
        view = ui;
        databaseTool = DatabaseTool.getInstance();
        databaseTool.setupDatabase();
        apiTool = new ApiTool("", Constants.GW2API_BASE_URL, Constants.SPIDYAPI_BASE_URL);
        currentBuild = databaseTool.getBuild();
    }

    @Override
    public void onStart() {
        apiTool.getBuildVersion(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                if(response.body() != null) {
                    int version = response.body().version;
                    if(currentBuild != version) {
                        try {
                            databaseTool.setBuild(version);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        getMaxPos();
                    } else {
                        view.onStart();
                    }
                } else {
                    System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                    System.err.println("Response code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
            }
        });
    }

    @Override
    public void onRequest(String itemName, int quantity) {
        apiTool.getIdFromName(itemName, new Callback<NameSearch>() {
            @Override
            public void onResponse(Call<NameSearch> call, Response<NameSearch> response) {
                if(response.body() != null) {
                    int itemId = response.body().getItemId(itemName);
                    System.out.println(itemId);
                    try {
                        manageNamingStep(itemId, quantity);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                    System.err.println("Response code: " + response.code());
                    System.err.println("Request URL: " + response.raw().request().url());
                }
            }

            @Override
            public void onFailure(Call<NameSearch> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
            }
        });
    }

    private void updateDatabase() {
        view.onNotify("\r" + currentPos + " out of " + maxPos + " recipes obtained. Please wait. ", true);
        if(currentPos >= maxPos) {
            view.onNotify("All recipes fetched.", false);
            view.onStart();
        } else {
            StringBuilder builder = new StringBuilder();
            int bound = Integer.min(maxPos, currentPos + 199);
            for(; currentPos <= bound; currentPos++) {
                builder.append(currentPos);
                if(currentPos != bound) builder.append(",");
            }
            String request = builder.toString();
            apiTool.getRecipes(request, new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if(response.body() != null) {
                        try {
                            insertRecipesIntoDatabase(response.body());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        updateDatabase();
                    } else {
                        System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                        System.err.println("Response code: " + response.code());
                        System.err.println("Request URL: " + response.raw().request().url());
                    }
                }
                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                }
            });
        }
    }

    private void insertRecipesIntoDatabase(List<Recipe> recipes) throws SQLException {
        databaseTool.putRecipes(recipes);
    }

    private void getMaxPos() {
        apiTool.getRecipeIds(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                body = body.substring(1, body.length()-1);
                int[] ids = Arrays.stream(body.replaceAll("\\s+", "").split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                maxPos = ids[ids.length - 1];
                updateDatabase();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
            }
        });
    }

    private void manageNamingStep(int itemId, int quantity) throws SQLException {
        ItemNode root = TreeHelper.createTree(itemId, quantity, databaseTool);
        Set<Integer> itemIds = TreeHelper.getAllItemIds(root);
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> iterator = itemIds.iterator();
        while(iterator.hasNext()) {
            int next = iterator.next();
            stringBuilder.append(next);
            if(iterator.hasNext()){
                stringBuilder.append(",");
            }
        }
        String requestString = stringBuilder.toString();
        apiTool.getNamesFromIds(requestString, new Callback<List<IdSearch>>() {
            @Override
            public void onResponse(Call<List<IdSearch>> call, Response<List<IdSearch>> response) {
                if(response.body() != null) {
                    List<IdSearch> idSearches = response.body();
                    Map<Integer, String> idToName = new TreeMap<>();
                    for(IdSearch idSearch : idSearches) {
                        idToName.put(idSearch.id, idSearch.name);
                    }
                    manageCraftingTutorial(idToName, root);

                } else {
                    System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                    System.err.println("Response code: " + response.code());
                    System.err.println("Request URL: " + response.raw().request().url());
                }
            }

            @Override
            public void onFailure(Call<List<IdSearch>> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
            }
        });

    }

    private void manageCraftingTutorial(Map<Integer, String> idToName, ItemNode root) {
        Map<Integer, Integer> ingredientMapping = new TreeMap<>();
        TreeHelper.populateIngredientMap(root, ingredientMapping);
        List<Ingredient> craftingOrder = TreeHelper.getCraftingOrder(root);
        StringBuilder response = new StringBuilder();
        response.append("Required Materials: \n");
        for(int id : ingredientMapping.keySet()) {
            int quantity = ingredientMapping.get(id);
            response.append(idToName.get(id));
            response.append(":\t\t");
            response.append(quantity);
            response.append("\n");
        }
        response.append("\nCrafting Order: \n");
        for(Ingredient ingredient : craftingOrder) {
            response.append("Craft ");
            response.append(ingredient.quantity);
            response.append("\t\t");
            response.append(idToName.get(ingredient.id));
            response.append("\n");
        }
        view.onResponse(response.toString());
    }
}
