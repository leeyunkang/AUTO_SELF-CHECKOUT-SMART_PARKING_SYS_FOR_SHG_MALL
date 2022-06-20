package com.example.ezimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private FirebaseAuth mAuth;
    private EditText edit;
    private ImageButton productBtn, prodLocationBtn, parkingBtn, promoBtn, chatBtn, rewardBtn, scanShopBtn, moreBtn;
    private ImageButton logout, profile, cart;
    private ListView listView;
    user_information userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        getSupportActionBar().hide();

        productBtn = findViewById(R.id.productMain_btn);
        prodLocationBtn = findViewById(R.id.prodLocationMain_btn);
        parkingBtn = findViewById(R.id.parkingMain_btn);
        promoBtn = findViewById(R.id.promoMain_btn);
        chatBtn = findViewById(R.id.helpMain_btn);
        rewardBtn = findViewById(R.id.rewardMain_btn);
        scanShopBtn = findViewById(R.id.shopMain_btn);
        moreBtn = findViewById(R.id.moreMain_btn);

//        logout = findViewById(R.id.logoutMain_btn);
        profile = findViewById(R.id.profileMain_btn);
        cart = findViewById(R.id.cartMainBtn);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInformation("cart");
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInformation("profile");

            }
        });


        prodLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInformation("shopnow");

            }
        });

        parkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInformation("payparking");

            }
        });


        promoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getUserInformation("promotion");
                Toast.makeText(MainActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
            }
        });


        rewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInformation("rewards");

            }
        });
        scanShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInformation("scanshop");

            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getUserInformation("more");
                Toast.makeText(MainActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInformation("chatnow");


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getUserInformation(String path) {

//        Toast.makeText(MainActivity.this, "Successfully logged", Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Query check = databaseReference.orderByKey().equalTo(userID);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();


        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String usernameDB = snapshot.child(userID).child("username").getValue(String.class);
                    String passwordDB = snapshot.child(userID).child("password").getValue(String.class);
                    String fullnameDB = snapshot.child(userID).child("full_name").getValue(String.class);
                    String emailDB = snapshot.child(userID).child("email").getValue(String.class);
                    String addressDB = snapshot.child(userID).child("residential_address").getValue(String.class);
                    String mobileDB = snapshot.child(userID).child("mobile_number").getValue(String.class);
                    user_information userInformation = new user_information(usernameDB, fullnameDB, passwordDB, emailDB, mobileDB, addressDB);

                    switch (path) {
                        case "scanshop": {
                            Intent intent = new Intent(MainActivity.this, ScanShopActivity.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        case "payparking": {
                            Intent intent = new Intent(MainActivity.this, ParkingSearchActivity.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        case "promotion": {
                            Intent intent = new Intent(MainActivity.this, promotions.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        case "chatnow": {
                            Intent intent = new Intent(MainActivity.this, chatbot.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        case "rewards": {
                            Intent intent = new Intent(MainActivity.this, rewardscatalogue.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        case "shopnow": {
                            Intent intent = new Intent(MainActivity.this, FindProductActivity.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        case "more": {
                            Intent intent = new Intent(MainActivity.this, more.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        case "profile": {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        case "cart": {
                            Intent intent = new Intent(MainActivity.this, CartActivity.class);
                            intent.putExtra("userInformation", userInformation);
                            startActivity(intent);
                            break;
                        }
                        default:

                            break;
                    }
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



}




