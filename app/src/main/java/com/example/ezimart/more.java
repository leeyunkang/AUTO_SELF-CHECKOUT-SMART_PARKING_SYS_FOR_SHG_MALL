package com.example.ezimart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

public class more extends AppCompatActivity {

   // user_information userInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));

       // Log.d("ttest",userInformation.getUsername());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(more.this, MainActivity.class));
        finish();
    }

}