package com.example.android.bakingapp.network;


import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api_Service {

    @GET(Constants.RECIPES_FILE_NAME)
    Call<ArrayList<Recipe>> getRecipes();

}




