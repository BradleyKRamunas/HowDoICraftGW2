import data.api.ApiTool;
import data.api.BaseApiTool;
import data.api.api_objects.*;
import okhttp3.internal.io.FileSystem;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class ApiToolTest {

    public BaseApiTool apiTool;
    public MockWebServer mockServer;

    public CountDownLatch lock;

    @Before
    public void setUp() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
        apiTool = new ApiTool("", mockServer.url("").toString(), mockServer.url("").toString());
        lock = new CountDownLatch(1);
    }

    @Test
    public void testGetBuildVersion() throws IOException, InterruptedException {
        String mockResponse = readFile("C:\\Users\\Bradley\\IdeaProjects\\HowDoICraftGW2\\src\\test\\resources\\mock_gw2_api_build_response");
        Version mockVersion = new Version();
        mockVersion.version = 1;

        mockServer.enqueue(new MockResponse().setBody(mockResponse));

        apiTool.getBuildVersion(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                Version version = response.body();
                Assert.assertEquals(mockVersion, version);
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                Assert.fail();
            }
        });

        Thread.sleep(2000);
    }

    @Test
    public void testGetRecipes() throws IOException, InterruptedException {
        String mockResponse = readFile("C:\\Users\\Bradley\\IdeaProjects\\HowDoICraftGW2\\src\\test\\resources\\mock_gw2_api_recipe_response");
        Recipe firstRecipe = new Recipe();
        firstRecipe.type = "Refinement";
        firstRecipe.output = 19713;
        firstRecipe.quantity = 1;
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient firstIngredient = new Ingredient();
        firstIngredient.id = 19726;
        firstIngredient.quantity = 2;
        ingredients.add(firstIngredient);
        firstRecipe.ingredients = ingredients;
        firstRecipe.chatLink = "[&CQEAAAA=]";

        Recipe secondRecipe = new Recipe();
        secondRecipe.type = "Refinement";
        secondRecipe.output = 19712;
        secondRecipe.quantity = 1;
        ArrayList<Ingredient> ingredients2 = new ArrayList<>();
        Ingredient secondIngredient = new Ingredient();
        secondIngredient.id = 19725;
        secondIngredient.quantity = 3;
        ingredients2.add(secondIngredient);
        secondRecipe.ingredients = ingredients2;
        secondRecipe.chatLink = "[&CQIAAAA=]";

        List<Recipe> mockRecipes = new ArrayList<>();
        mockRecipes.add(firstRecipe);
        mockRecipes.add(secondRecipe);

        mockServer.enqueue(new MockResponse().setBody(mockResponse));

        apiTool.getRecipes("1,2", new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                Assert.assertEquals(mockRecipes, recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                Assert.fail();
            }
        });

        Thread.sleep(2000);
    }

    @Test
    public void testGetIdFromName() throws IOException, InterruptedException {
        String mockResponse = readFile("C:\\Users\\Bradley\\IdeaProjects\\HowDoICraftGW2\\src\\test\\resources\\mock_spidy_api_namesearch_response");
        int mockId = 66540;

        mockServer.enqueue(new MockResponse().setBody(mockResponse));

        apiTool.getIdFromName("Ambrite Orichalcum Earring", new Callback<NameSearch>() {
            @Override
            public void onResponse(Call<NameSearch> call, Response<NameSearch> response) {
                int id = response.body().getItemId("Ambrite Orichalcum Earring");
                Assert.assertEquals(mockId, id);
            }

            @Override
            public void onFailure(Call<NameSearch> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                Assert.fail();
            }
        });

        Thread.sleep(2000);
    }

    @Test
    public void testGetNameFromId() throws IOException, InterruptedException {
        String mockResponse = readFile("C:\\Users\\Bradley\\IdeaProjects\\HowDoICraftGW2\\src\\test\\resources\\mock_gw2_api_idsearch_response");
        String mockName = "Ambrite Orichalcum Earring";

        mockServer.enqueue(new MockResponse().setBody(mockResponse));

        apiTool.getNameFromId(66540, new Callback<IdSearch>() {
            @Override
            public void onResponse(Call<IdSearch> call, Response<IdSearch> response) {
                String name = response.body().getName();
                Assert.assertEquals(mockName, name);
            }

            @Override
            public void onFailure(Call<IdSearch> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                Assert.fail();
            }
        });

        Thread.sleep(2000);
    }

    @Test
    public void testGetRecipeIds() throws IOException, InterruptedException {
        String mockResponse = readFile("C:\\Users\\Bradley\\IdeaProjects\\HowDoICraftGW2\\src\\test\\resources\\mock_gw2_api_recipe_ids_response");
        int finalId = 12758;

        mockServer.enqueue(new MockResponse().setBody(mockResponse));

        apiTool.getRecipeIds(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                body = body.substring(1, body.length()-1);
                int[] ids = Arrays.stream(body.replaceAll("\\s+", "").split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                Assert.assertEquals(finalId, ids[ids.length-1]);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.err.println("An error occurred. Either parsing failed or there was no server connection.");
                Assert.fail();
            }
        });

        Thread.sleep(2000);

    }

    private String readFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedSource source = Okio.buffer(FileSystem.SYSTEM.source(file));
        String read = source.readUtf8();
        source.close();
        return read;
    }
}
