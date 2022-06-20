package com.example.ezimart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class rewardscatalogue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardscatalogue);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}