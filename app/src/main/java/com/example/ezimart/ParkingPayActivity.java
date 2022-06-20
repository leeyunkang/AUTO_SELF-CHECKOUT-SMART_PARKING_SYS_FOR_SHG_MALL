package com.example.ezimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParkingPayActivity extends AppCompatActivity {

    DatabaseReference reference;
    Button pay;
    TextView carplate, intime, currenttime, total, duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkingpay);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        carplate = findViewById(R.id.carPlatePayTxv);
        intime = findViewById(R.id.Intime);

        total = findViewById(R.id.Total);
        duration = findViewById(R.id.Duration);


        String txtcarplate = getIntent().getExtras().getString("car_plate", null);
        String txtdatetime = getIntent().getExtras().getString("date_time", null);


        carplate.setText("  Car Plate Number :"+txtcarplate);
        intime.setText("  Time In :"+txtdatetime);

        SimpleDateFormat dateStampFormat = new SimpleDateFormat("yyyy / MM / dd  HH:mm ");
        Date myDate = new Date();
        String date = dateStampFormat.format(myDate);


        calculate(txtdatetime,date);
        pay = findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payparking(txtcarplate);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ParkingPayActivity.this, ParkingSearchActivity.class));
        finish();
    }

    void calculate(String timein, String currenttime) {

        String Time1 = timein;
        String Time2 =  currenttime;

        // date format
        SimpleDateFormat format = new SimpleDateFormat("yyyy / MM / dd  HH:mm ");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(Time1);
            d2 = format.parse(Time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);


        if (diffHours <= 1 ){
            total.setText("Parking fee:"+"No parking fee");
            duration.setText("Duration :" +"  less than an hour");
        } else{
            //total.setText("no parking fee");
            total.setText("Parking fee:"+"RM"+Long.toString(diffHours*2) );
            duration.setText("Duration : "+Long.toString(diffHours) + " hours");

        }



    }


    void payparking(String carplate ){
        FirebaseDatabase.getInstance().getReference("car_plate_record")
                .child(carplate).child("status")
                .setValue("clear").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ParkingPayActivity.this, "successfully pay the parking fee", Toast.LENGTH_SHORT).show();
                            finish();

                        }else{
                            Toast.makeText(ParkingPayActivity.this, "failed to pay the parking fee", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}