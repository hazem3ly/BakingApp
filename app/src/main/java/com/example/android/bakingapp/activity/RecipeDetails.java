package com.example.android.bakingapp.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.RecipeDetailsFragment;
import com.example.android.bakingapp.model.Recipe;

import static com.example.android.bakingapp.utils.Constants.SELECTED_RECIPE;

public class RecipeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        if (savedInstanceState == null) {
            if (getIntent().hasExtra(SELECTED_RECIPE)) {

                setTitle(((Recipe) getIntent().getParcelableExtra(SELECTED_RECIPE)).name);

                Bundle data = new Bundle();
                data.putParcelable(SELECTED_RECIPE, getIntent().getParcelableExtra(SELECTED_RECIPE));

                RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                recipeDetailsFragment.setArguments(data);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.dFragment, recipeDetailsFragment).commit();

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
