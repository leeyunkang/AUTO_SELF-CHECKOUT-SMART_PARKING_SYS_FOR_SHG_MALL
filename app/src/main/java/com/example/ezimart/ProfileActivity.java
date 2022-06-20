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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    DatabaseReference reference;
    private EditText username, fullname, email, address, mobile;
    private TextView password;
    String Username,Password,Fullname,Email,Address,Mobile;
    Button update;
    ImageButton logout;

    String uid = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));

        reference = FirebaseDatabase.getInstance().getReference("Users");

        fullname = findViewById(R.id.profilfullnameid);
        mobile = findViewById(R.id.profilmobileid);
        address = findViewById(R.id.profiladdressid);
        email = findViewById(R.id.profilemailid);
        username = findViewById(R.id.profilusernameid);
        password = findViewById(R.id.profilpasswordid);

        logout = findViewById(R.id.logoutProfile_btn);
        update = findViewById(R.id.button);

        //  user_information userInformation = (user_information)getIntent().getSerializableExtra("userInformationprofile");

        //showAllUserInformation();
        user_information userInformation = new user_information();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editUserInformation();
                updateProfile();
                //   updateUserInformation();


            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, StartActivity.class));
                finish();

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String txt_name = snapshot.child("full_name").getValue(String.class);
                String txt_mobile = snapshot.child("mobile_number").getValue(String.class);
                String txt_address = snapshot.child("residential_address").getValue(String.class);
                String txt_email = snapshot.child("email").getValue(String.class);
                String txt_username = snapshot.child("username").getValue(String.class);
                String txt_password = snapshot.child("password").getValue(String.class);

                fullname.setText(txt_name);
                mobile.setText(txt_mobile);
                address.setText(txt_address);
                email.setText(txt_email);
                username.setText(txt_username);
                password.setText(txt_password);

                //Toast.makeText(StartActivity.this, fullName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                // Toast.makeText(StartActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showAllUserInformation(){

        /*
        Intent intent = getIntent();
        String user_username = intent.getStringExtra("username");
        String user_fullname = intent.getStringExtra("full_name");
        String user_email = intent.getStringExtra("mobile_number");
        String user_password = intent.getStringExtra("password");
        String user_address = intent.getStringExtra("residential_address");
        String user_mobile = intent.getStringExtra("mobile_number");
        user_information userInformation = (user_information)getIntent().getSerializableExtra("userInformationprofile");
        */

        user_information userInformation = new user_information();
        Toast.makeText(ProfileActivity.this, userInformation.getEmail(), Toast.LENGTH_SHORT).show();

        username.setText(userInformation.getUsername());
        fullname.setText(userInformation.getFull_name());
        email.setText(userInformation.getEmail());
        password.setText(userInformation.getPassword());
        address.setText(userInformation.getResidential_address());
        mobile.setText(userInformation.getMobile_number());

    }

    private void editUserInformation(){

        user_information userInformation = new user_information();

        String Userusername = username.getEditableText().toString().trim();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = databaseReference.orderByChild("username").equalTo(Userusername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String txt_full_name = fullname.getText().toString();
                String txt_email = email.getText().toString();
                String txt_mobile_number = mobile.getText().toString();
                String txt_residential_address = address.getText().toString();
                String txt_username = username.getText().toString();
                String txt_password = userInformation.getPassword();

                user_information userinformation = new user_information(txt_username,txt_password,txt_full_name,txt_email,txt_mobile_number,txt_residential_address);
                DatabaseReference databaseReference;

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password))
                {
                    Toast.makeText(ProfileActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6)
                {
                    Toast.makeText(ProfileActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(txt_full_name)||TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_mobile_number)||TextUtils.isEmpty(txt_residential_address)) {
                    Toast.makeText(ProfileActivity.this, "Please fill in all the information ", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(txt_username);
                    databaseReference.setValue(userinformation);
                    // startActivity(new Intent(profile.this, profile.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateProfile() {

        String txt_full_name = fullname.getText().toString();
        String txt_mobile_number = mobile.getText().toString();
        String txt_residential_address = address.getText().toString();
        String txt_email = email.getText().toString();

        String txt_username = username.getText().toString();
        String txt_password = password.getText().toString();

        if (txt_full_name.isEmpty() || txt_mobile_number.isEmpty() || txt_residential_address.isEmpty()
                || txt_email.isEmpty() || txt_username.isEmpty() || txt_password.isEmpty())
        {
            Toast.makeText(ProfileActivity.this, "No text entered!", Toast.LENGTH_SHORT).show();
        } else
        {
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("full_name").setValue(txt_full_name);
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("mobile_number").setValue(txt_mobile_number);
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("residential_address").setValue(txt_residential_address);
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("email").setValue(txt_email);
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("username").setValue(txt_username);
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("password").setValue(txt_password);

            user_information userInformation = new user_information(txt_username,txt_password,
                    txt_full_name,txt_email,txt_mobile_number,txt_residential_address);

            Toast.makeText(ProfileActivity.this, "Account information updated!", Toast.LENGTH_SHORT).show();
        }
    }

}