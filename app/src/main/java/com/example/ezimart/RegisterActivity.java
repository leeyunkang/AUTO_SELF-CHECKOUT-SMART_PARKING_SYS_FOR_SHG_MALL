package com.example.ezimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText username,password,confirm_password,full_name ,email, mobile_number,residential_address;
    private Button register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        email = findViewById(R.id.emailRegister_edt);
        password = findViewById(R.id.passwordRegister_edt);
        confirm_password = findViewById(R.id.confirm_password);
        register = findViewById(R.id.registerUser_btn);
        full_name = findViewById(R.id.fullNameRegister_edt);
        username = findViewById(R.id.usernameRegister_edt);
        mobile_number = findViewById(R.id.mobileNumberRegister_edt);
        residential_address  = findViewById(R.id.addressRegister_edt);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
                //checkRegisterText();
            }
        });
    }

    // lee

    public void userRegister() {

        String txt_username = username.getText().toString();
        String txt_password = password.getText().toString();
        String txt_confirm_password = confirm_password.getText().toString();
        String txt_full_name = full_name.getText().toString();
        String txt_email = email.getText().toString();
        String txt_mobile_number = mobile_number.getText().toString();
        String txt_residential_address = residential_address.getText().toString();

        user_information userInformation = new user_information(txt_username,txt_full_name,txt_password,txt_email,txt_mobile_number,txt_residential_address);

        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password))
        {
            Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
        } else if (txt_password.length() < 6)
        {
            Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
        } else if (!txt_password.equals(txt_confirm_password))
        {
            Toast.makeText(RegisterActivity.this, "Password do not match ", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(txt_full_name)||TextUtils.isEmpty(txt_email)
                ||TextUtils.isEmpty(txt_mobile_number)||TextUtils.isEmpty(txt_residential_address)) {
            Toast.makeText(RegisterActivity.this, "Please fill in all the information ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users");
                        user.child(mAuth.getCurrentUser().getUid()).setValue(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,"Database registered!",Toast.LENGTH_LONG).show();

                                }else{
                                    Toast.makeText(RegisterActivity.this,"Database register failed!",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        Toast.makeText(RegisterActivity.this, "Account registered!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                    else{

                    }
                }
            });
        }
    }

    // BENG

    public void checkRegisterText() {
        String txt_full_name = full_name.getText().toString();
        String txt_mobile_number = mobile_number.getText().toString();
        String txt_residential_address = residential_address.getText().toString();
        String txt_email = email.getText().toString();
        String txt_username = username.getText().toString();
        String txt_password = password.getText().toString();
        String txt_confirm_password = confirm_password.getText().toString();

        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password))
        {
            Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
        }
        else if (txt_password.length() < 6)
        {
            Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
        }
        else if (!txt_password.equals(txt_confirm_password))
        {
            Toast.makeText(RegisterActivity.this, "Password do not match ", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(txt_full_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_mobile_number)
                || TextUtils.isEmpty(txt_residential_address)) {
            Toast.makeText(RegisterActivity.this, "Please fill in all the information ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            registerUser(txt_full_name, txt_mobile_number, txt_residential_address, txt_email, txt_username, txt_password);
        }
    }

    private void registerUser(String name, String number, String address, String email, String username, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user_information user = new user_information(
                            username,
                            password,
                            name,
                            email,
                            number,
                            address
                    );

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).setValue(user);

                    Toast.makeText(RegisterActivity.this, "Registering user successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else
                {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}