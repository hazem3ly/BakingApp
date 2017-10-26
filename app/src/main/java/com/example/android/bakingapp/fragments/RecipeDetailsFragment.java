package com.example.android.bakingapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.StepDetails;
import com.example.android.bakingapp.adapters.IngredientAdapter;
import com.example.android.bakingapp.adapters.StepsAdapter;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import static com.example.android.bakingapp.utils.Constants.SELECTED_RECIPE;
import static com.example.android.bakingapp.utils.Constants.SELECTED_STEP;

public class RecipeDetailsFragment extends Fragment implements StepsAdapter.OnStepClickListener {


    RecyclerView recipe_ingredient_recycler, recipe_steps_recycler;
    RecyclerView.LayoutManager ingredientLayoutManager;
    RecyclerView.LayoutManager stepsLayoutManager;
    IngredientAdapter ingredientAdapter;
    StepsAdapter stepsAdapter;
    private Recipe selectedRecipe;
    private View rootView;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Check for selected recipe in arguments
        Bundle data = getArguments();
        if (data.getParcelable(SELECTED_RECIPE) != null) {
            selectedRecipe = data.getParcelable(SELECTED_RECIPE);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        recipe_ingredient_recycler = rootView.findViewById(R.id.recipe_ingredient_recycler);
        recipe_steps_recycler = rootView.findViewById(R.id.recipe_steps_recycler);

        ingredientLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        stepsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recipe_ingredient_recycler.setLayoutManager(ingredientLayoutManager);
        recipe_steps_recycler.setLayoutManager(stepsLayoutManager);

        ingredientAdapter = new IngredientAdapter(new ArrayList<Ingredient>());
        stepsAdapter = new StepsAdapter(new ArrayList<Step>(), this);

        recipe_ingredient_recycler.setAdapter(ingredientAdapter);
        recipe_steps_recycler.setAdapter(stepsAdapter);

        // Check if already recipe not null and bind views with data
        if (selectedRecipe != null) {
            fillData();
        }

        return rootView;
    }

    private void fillData() {
        ingredientAdapter.AddList(selectedRecipe.ingredients);
        stepsAdapter.AddList(selectedRecipe.steps);
    }

    @Override
    public void OnStepClick(Step step) {

        if (getActivity().findViewById(R.id.dStepFragment) != null) {
            Bundle data = new Bundle();
            data.putParcelable(SELECTED_STEP, step);

            StepDetailsFragment detailsFragment = new StepDetailsFragment();
            detailsFragment.setArguments(data);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dStepFragment, detailsFragment).commit();
        } else

            startActivity(new Intent(getContext(), StepDetails.class).putExtra(SELECTED_STEP, step));
    }
}
