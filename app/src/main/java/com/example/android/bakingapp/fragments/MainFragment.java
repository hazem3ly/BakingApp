package com.example.android.bakingapp.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.RecipesRecyclerAdapter;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.network.RestClient;
import com.example.android.bakingapp.providers.BakingProvider;
import com.example.android.bakingapp.test.SimpleIdlingResource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.bakingapp.providers.BakingContract.IngredientContract.COLUMN_RECIPE_NAME;
import static com.example.android.bakingapp.providers.BakingContract.IngredientContract.INGREDIENT;
import static com.example.android.bakingapp.providers.BakingContract.IngredientContract.MEASURE;
import static com.example.android.bakingapp.providers.BakingContract.IngredientContract.QUANTITY;
import static com.example.android.bakingapp.providers.BakingContract.RecipeContract.RECIPE_ID;
import static com.example.android.bakingapp.utils.Constants.RECIPE_LIST_INSTANCE_KEY;
import static com.example.android.bakingapp.utils.Constants.RECYCLER_POSITION;

public class MainFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private RecipesRecyclerAdapter adapter;


    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;
    private View rootView;
    private RecyclerView recipe_recycler_view;

    public MainFragment() {
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get the IdlingResource instance
        getIdlingResource();
        if (mIdlingResource != null)
            mIdlingResource.setIdleState(false);
        initViews(rootView);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPE_LIST_INSTANCE_KEY)) {
                ArrayList<Recipe> List = savedInstanceState.getParcelableArrayList(RECIPE_LIST_INSTANCE_KEY);
                if (List != null && List.size() > 0)
                    adapter.AddList(List);
                else getRecipes();
            }

            if (savedInstanceState.containsKey(RECYCLER_POSITION))
                layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_POSITION));
        } else
            getRecipes();

        return rootView;
    }

    /**
     * Save out stat when activity destroyed
     *
     * @param outState s
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (adapter != null && adapter.getList() != null)
            outState.putParcelableArrayList(RECIPE_LIST_INSTANCE_KEY, adapter.getList());
        if (layoutManager != null)
            outState.putParcelable(RECYCLER_POSITION, layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    private void initViews(View rootView) {
        recipe_recycler_view = rootView.findViewById(R.id.recipe_recycler_view);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            layoutManager = new LinearLayoutManager(getContext());
        } else {
            layoutManager = new GridLayoutManager(getContext(), 2);
        }

        recipe_recycler_view.setLayoutManager(layoutManager);
        adapter = new RecipesRecyclerAdapter(new ArrayList<Recipe>());
        recipe_recycler_view.setAdapter(adapter);
    }

    private void getRecipes() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        Call<ArrayList<Recipe>> recipesCall = new RestClient().getApi_service().getRecipes();
        recipesCall.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                if (response.body() != null) {
                    ArrayList<Recipe> results = response.body();
                    if (results != null && results.size() > 0) {
                        adapter.AddList(results);
                        insertToDatabase(results);
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        if (mIdlingResource != null) {
                            mIdlingResource.setIdleState(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                recipe_recycler_view.setVisibility(View.GONE);
                ((TextView) rootView.findViewById(R.id.no_data)).setVisibility(View.VISIBLE);

                ((TextView) rootView.findViewById(R.id.no_data)).setText(R.string.connection_error);
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        });
    }


    private void insertToDatabase(final ArrayList<Recipe> recipes) {
        AsyncTask<Void, Void, Void> insertTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {


                Cursor cursor = getContext().getContentResolver().
                        query(BakingProvider.RECIPES_PROVIDER.CONTENT_URI, null, null, null, null);

                if (cursor != null && cursor.getCount() > 0) {
//                    bakingList();
                    cursor.close();
                    return null;
                }

                List<ContentValues> values = new ArrayList<>();
                for (Recipe recipe : recipes) {
                    ContentValues cv = new ContentValues();
                    cv.put(RECIPE_ID, recipe.id);
                    cv.put(COLUMN_RECIPE_NAME, recipe.name);

                    getContext().getContentResolver().
                            insert(BakingProvider.RECIPES_PROVIDER.CONTENT_URI, cv);

                    for (Ingredient ingredient : recipe.ingredients) {
                        ContentValues newrecipe = new ContentValues();
                        newrecipe.put(COLUMN_RECIPE_NAME, String.valueOf(recipe.name));
                        newrecipe.put(QUANTITY, String.valueOf(ingredient.quantity));
                        newrecipe.put(MEASURE, String.valueOf(ingredient.measure));
                        newrecipe.put(INGREDIENT, String.valueOf(ingredient.ingredient));
                        values.add(newrecipe);
                    }
                }

                getContext().getContentResolver().
                        bulkInsert(BakingProvider.INGREDIENTS_PROVIDER.CONTENT_URI,
                                values.toArray(new ContentValues[values.size()]));


                return null;
            }
        };

        insertTask.execute();
    }

}
