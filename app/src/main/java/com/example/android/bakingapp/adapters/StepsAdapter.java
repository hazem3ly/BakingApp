package com.example.android.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<Step> steps;
    private OnStepClickListener onStepClickListener;
    public StepsAdapter(ArrayList<Step> steps,OnStepClickListener onStepClickListener) {
        this.steps = steps;
        this.onStepClickListener = onStepClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(steps.get(position));
        holder.step_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onStepClickListener!=null){
                    onStepClickListener.OnStepClick(steps.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void AddList(ArrayList<Step> steps) {
        if (steps == null) this.steps = new ArrayList<>();
        else
            this.steps = steps;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView step_text;

        ViewHolder(final View itemView) {
            super(itemView);
            step_text = itemView.findViewById(R.id.step_text);
        }

        void bindData(Step step) {
            step_text.setText(step.shortDescription);
        }
    }

    public interface OnStepClickListener{
        void OnStepClick(Step step);
    }

}
