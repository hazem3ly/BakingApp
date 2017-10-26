package com.example.android.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private ArrayList<Ingredient> ingredients;

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void AddList(ArrayList<Ingredient> ingredients) {
        if (ingredients == null) this.ingredients = new ArrayList<>();
        else
            this.ingredients = ingredients;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView ingredient_text,quantity_text,measure_text;

        ViewHolder(final View itemView) {
            super(itemView);

            ingredient_text = itemView.findViewById(R.id.ingredient_text);
            quantity_text = itemView.findViewById(R.id.quantity_text);
            measure_text = itemView.findViewById(R.id.measure_text);

        }

        void bindData(Ingredient ingredient) {
            ingredient_text.setText(ingredient.ingredient);
            measure_text.setText(ingredient.measure);
            quantity_text.setText(String.valueOf(ingredient.quantity));
        }
    }

}
