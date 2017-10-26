package com.example.android.bakingapp.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.RecipeDetails;
import com.example.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.bakingapp.utils.Constants.SELECTED_RECIPE;

public class RecipesRecyclerAdapter extends RecyclerView.Adapter<RecipesRecyclerAdapter.ViewHolder> {

    private ArrayList<Recipe> recipeList;

    public RecipesRecyclerAdapter(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public RecipesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesRecyclerAdapter.ViewHolder holder, int position) {
        holder.bindData(recipeList.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void AddList(ArrayList<Recipe> results) {
        if (results == null) this.recipeList = new ArrayList<>();
        else
            this.recipeList = results;
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getList() {
        return this.recipeList;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView recipeName;
        ImageView recipe_image;
        CardView cv;

        ViewHolder(final View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipeName);
            recipe_image = itemView.findViewById(R.id.recipe_image);
            cv = itemView.findViewById(R.id.cv);

        }

        void bindData(final Recipe recipe) {
            recipeName.setText(recipe.name);
            if (recipe.image.isEmpty()) {
                if (recipe.id == 1){
                    recipe_image.setImageResource(R.drawable.nutella);
                }else if (recipe.id == 2){
                    recipe_image.setImageResource(R.drawable.brownies);
                }else if (recipe.id == 3){
                    recipe_image.setImageResource(R.drawable.yellowcake);
                }else if (recipe.id == 4){
                    recipe_image.setImageResource(R.drawable.cheesecake);
                }
            }else {
                Picasso.with(cv.getContext())
                        .load(recipe.image)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(recipe_image);
            }
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), RecipeDetails.class);
                    intent.putExtra(SELECTED_RECIPE, recipe);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }


}
