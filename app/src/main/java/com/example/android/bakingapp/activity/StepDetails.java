package com.example.android.bakingapp.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.StepDetailsFragment;
import com.example.android.bakingapp.model.Step;

import static com.example.android.bakingapp.utils.Constants.SELECTED_STEP;

public class StepDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar

            android.support.v7.app.ActionBar actionBar =  getSupportActionBar();
            if (actionBar != null)
                actionBar.hide();

        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        setContentView(R.layout.activity_step_details);

        if (savedInstanceState == null) {

            if (getIntent().hasExtra(SELECTED_STEP)) {


                Step selectedStep = getIntent().getParcelableExtra(SELECTED_STEP);

                setTitle(selectedStep.shortDescription);


                Bundle data = new Bundle();
                data.putParcelable(SELECTED_STEP, selectedStep);

                StepDetailsFragment detailsFragment = new StepDetailsFragment();
                detailsFragment.setArguments(data);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.dStepFragment, detailsFragment).commit();
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
