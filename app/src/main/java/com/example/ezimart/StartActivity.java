package com.example.ezimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        register = findViewById(R.id.register);
        login = findViewById(R.id.loginStartBtn);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            startActivity(new Intent(StartActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }

    public boolean onOptionItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}