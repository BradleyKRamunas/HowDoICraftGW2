package data.api;

import data.api.api_objects.*;
import retrofit2.Callback;

import java.util.List;
import java.util.Set;

public interface BaseApiTool {

    void getBuildVersion(Callback<Version> callback);
    void getRecipes(String recipeRequest, Callback<List<Recipe>> callback);
    void getIdFromName(String name, Callback<NameSearch> callback);
    void getNameFromId(int id, Callback<IdSearch> callback);
    void getNamesFromIds(String nameRequest, Callback<List<IdSearch>> callback);
    void getRecipeIds(Callback<String> callback);

}
