package com.guilardi.baking.utilities;

import java.util.List;

import com.guilardi.baking.data.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by deguilardi on 7/19/18.
 *
 * provide all network access
 */

public final class NetworkUtils {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static final String PATH_RECIPES_LIST = "topher/2017/May/59121517_baking/baking.json";

    private static NetworkUtils instance;
    private Service service;

    private NetworkUtils(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(Service.class);
    }

    public static synchronized NetworkUtils getInstance() {
        if(instance == null){
            instance = new NetworkUtils();
        }
        return instance;
    }

    public void loadRecipes(Callback<List<Recipe>> callback){
        Call<List<Recipe>> call = service.loadRecipes(PATH_RECIPES_LIST);
        call.enqueue(callback);
    }


    /**
     * RETROFIT INTERFACES AND CLASSES
     */
    interface Service{
        @GET("{path}")
        Call<List<Recipe>> loadRecipes(@Path("path") String path);
    }
}
