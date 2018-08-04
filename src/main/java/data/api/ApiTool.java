package data.api;

import data.api.api_objects.NameSearch;
import data.api.api_objects.Recipe;
import data.api.api_objects.Version;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.Constants;

import java.io.IOException;
import java.util.List;

public class ApiTool implements BaseApiTool {

    private String key;
    private OkHttpClient client;

    private String gw2ApiUrl;
    private String spidyApiUrl;

    public ApiTool(String apiKey, String gw2Url, String spidyUrl) {
        key = apiKey;
        gw2ApiUrl = gw2Url;
        spidyApiUrl = spidyUrl;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + key)
                                .build();
                        return chain.proceed(request);
                    }
                });
        client = builder.build();
    }

    @Override
    public void getBuildVersion(Callback<Version> callback) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(gw2ApiUrl)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(client).build();
        ApiClient connection = retrofit.create(ApiClient.class);
        Call<Version> call = connection.getVersion();
        call.enqueue(callback);
    }

    @Override
    public void getRecipes(String recipeRequest, Callback<List<Recipe>> callback) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(gw2ApiUrl)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(client).build();
        ApiClient connection = retrofit.create(ApiClient.class);
        Call<List<Recipe>> call = connection.getRecipes(recipeRequest);
        call.enqueue(callback);
    }

    @Override
    public void getIdFromName(String name, Callback<NameSearch> callback) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(spidyApiUrl)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(client).build();
        ApiClient connection = retrofit.create(ApiClient.class);
        Call<NameSearch> call = connection.getIdFromName(name);
        call.enqueue(callback);
    }

}
