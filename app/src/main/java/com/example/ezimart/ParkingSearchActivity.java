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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class ParkingSearchActivity extends AppCompatActivity {
    Button entre;
    EditText carplate;
    user_information userInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkingsearch);
        carplate= findViewById(R.id.carPlateSearch);
        entre = findViewById(R.id.search);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        userInformation= (user_information)getIntent().getSerializableExtra("userInformation");

        entre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchcarplate();

            }
        });


    }

    public void searchcarplate() {

        String Carplate = carplate.getEditableText().toString().trim().toUpperCase(Locale.ROOT);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("car_plate_record");
        Query checkCarPlate = databaseReference.orderByChild("car_plate").equalTo(Carplate);
        Query checkStatus = databaseReference.orderByChild("status").equalTo("unclear");

        checkCarPlate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String status = snapshot.child(Carplate).child("status").getValue(String.class);
                    String CarPlateDB = snapshot.child(Carplate).child("car_plate").getValue(String.class);
                    String Date_TimeDB = snapshot.child(Carplate).child("date_time").getValue(String.class);
                    if(status.equals("clear")){
                        Toast.makeText(ParkingSearchActivity.this, "The fee for this license plate number is already clear", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(ParkingSearchActivity.this, CarPlateDB, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ParkingSearchActivity.this, ParkingPayActivity.class);
                        intent.putExtra("car_plate", CarPlateDB);
                        intent.putExtra("date_time", Date_TimeDB);
                        startActivity(intent);
                        finish();
                    }

                    //Toast.makeText(LoginActivity.this, "Successfully logged", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ParkingSearchActivity.this, "Can't find this car plate number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}