package com.example.ezimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button login,Register;
    private String userID;
    private FirebaseUser user;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.usernameRegister_edt);
        password = findViewById(R.id.passwordRegister_edt);
        login = findViewById(R.id.loginStartBtn);
        Register = findViewById(R.id.registerlogin);
        mAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            loginuser(v);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private Boolean validateusername(){
        String val = email.getEditableText().toString();
        String noWithSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }else if(val.length()>=15){
            email.setError("Username too long");
            return false;
        }else{
            email.setError(null);
            return true;
        }
    }

    private Boolean validatepassword(){
        String val = password.getEditableText().toString();
        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;

        }else {
            password.setError(null);
            return true;

        }


    }

    public void loginuser(View view){
        if(!validateusername()|!validatepassword()){
            return;
        }else{
            isUser();
        }
    }

    private void isUser() {
        String Useremail = email.getEditableText().toString();
        String Userpassword = password.getEditableText().toString();

        mAuth.signInWithEmailAndPassword(Useremail, Userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Successfully logged", Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    Query checkUser = databaseReference.orderByChild("email").equalTo(Useremail);
                    Query checkPassword = databaseReference.orderByChild("password").equalTo(Userpassword);

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("User");
                    userID = user.getUid();
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                checkPassword.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String Useremail = email.getEditableText().toString().trim();
                                            String Userpassword = password.getEditableText().toString().trim();

                                            //Toast.makeText(LoginActivity.this, "Successfully logged", Toast.LENGTH_SHORT).show();
                                            String usernameDB = snapshot.child(userID).child("username").getValue(String.class);
                                            String passwordDB = snapshot.child(userID).child("password").getValue(String.class);
                                            String fullnameDB = snapshot.child(userID).child("full_name").getValue(String.class);
                                            String emailDB = snapshot.child(userID).child("email").getValue(String.class);
                                            String addressDB = snapshot.child(userID).child("residential_address").getValue(String.class);
                                            String mobileDB = snapshot.child(userID).child("mobile_number").getValue(String.class);
                                            Toast.makeText(LoginActivity.this, usernameDB, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            user_information userInformation = new user_information(usernameDB, fullnameDB, passwordDB, emailDB, mobileDB, addressDB);
                                            intent.putExtra("userInformation", userInformation);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            } else {
                                Toast.makeText(LoginActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}