package data.api;

import data.api.api_objects.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiClient {

    @GET("/v2/build")
    Call<Version> getVersion();

    @GET("/v2/recipes")
    Call<List<Recipe>> getRecipes(@Query(value = "ids", encoded = true) String idString);

    @GET("/api/v0.9/json/item-search/{name}/1")
    Call<NameSearch> getIdFromName(@Path(value = "name", encoded = true) String name);

    @GET("/v2/items/{id}")
    Call<IdSearch> getNameFromId(@Path("id") int id);

    @GET("/v2/items")
    Call<List<IdSearch>> getNamesFromIds(@Query(value = "ids", encoded = true) String ids);

    @GET("/v2/recipes")
    Call<String> getRecipeIds();

}
