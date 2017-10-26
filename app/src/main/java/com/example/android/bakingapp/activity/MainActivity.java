package com.example.android.bakingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mFragment,new MainFragment()).commit();

    }
}
