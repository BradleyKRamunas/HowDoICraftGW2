package data.api;

import data.api.api_objects.NameSearch;
import data.api.api_objects.Recipe;
import data.api.api_objects.Version;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiClient {

    @GET("/v2/build")
    Call<Version> getVersion();

    @GET("v2/recipes")
    Call<List<Recipe>> getRecipes(@Query("ids") String idString);

    @GET("/api/0.9/json/item-search/{name}/1")
    Call<NameSearch> getIdFromName(@Path("name") String name);

}
