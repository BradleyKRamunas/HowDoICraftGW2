package data.api;

import data.api.api_objects.NameSearch;
import data.api.api_objects.Recipe;
import data.api.api_objects.Version;
import retrofit2.Callback;

import java.util.List;

public interface BaseApiTool {

    void getBuildVersion(Callback<Version> callback);
    void getRecipes(String recipeRequest, Callback<List<Recipe>> callback);
    void getIdFromName(String name, Callback<NameSearch> callback);

}
