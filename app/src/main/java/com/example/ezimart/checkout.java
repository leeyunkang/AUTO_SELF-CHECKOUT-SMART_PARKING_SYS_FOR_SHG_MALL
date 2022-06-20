package com.example.ezimart;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ezimart.adapter.MyCartAdapter;
import com.example.ezimart.listener.cartloadlistener;
import com.example.ezimart.listener.itemloadlistener;
import com.example.ezimart.model.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class checkout extends AppCompatActivity {
    Integer totalweight,totalweightmore,totalweightless;
    public List<CartModel> cartModelListcheckout;
    TextView totalPrice;
    Button comfirmbutton,scanweight;
    itemloadlistener itemloadlistener;
    cartloadlistener Cartloadlistener;
user_information userInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));

       CartActivity cartActivity= new CartActivity();
        totalPrice = findViewById(R.id.totalprice);
        comfirmbutton= findViewById(R.id.comfirmcheckout);
        scanweight = findViewById(R.id.scanWeight);
        final Loading loading = new Loading(checkout.this);
        loadCartTotalPriceFromFirebase();
        comfirmbutton.setVisibility(View.INVISIBLE);

if(getIntent().getIntExtra("verify",0) == (9999)){
    openDialog();


}

scanweight.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
loading.startLoading();
Handler handler = new Handler();
handler.postDelayed(new Runnable() {
    @Override
    public void run() {
loading.dismissDialog();
        comfirmbutton.setVisibility(View.VISIBLE);
        scanweight.setVisibility(View.INVISIBLE);
        Toast.makeText(checkout.this,"Payment Successful", Toast.LENGTH_SHORT).show();
    }
},3000);

    }


});

        comfirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (checkout.this,checkout2.class);
                user_information  userInformationmore = new user_information(userInformation.getUsername(),userInformation.getFull_name(),userInformation.getPassword(),userInformation.getEmail(),userInformation.getMobile_number(),userInformation.getResidential_address());

                intent.putExtra("userInformation",userInformationmore);

                intent.putExtra("doubleValue_e1", totalweight);
                intent.putExtra("doubleValue_e2", totalweightless);
                intent.putExtra("doubleValue_e3", totalweightmore);
                startActivity(intent);


            }
        });
    }

    public  void  loadCartTotalPriceFromFirebase( ){
        user_information userInformation = (user_information)getIntent().getSerializableExtra("userInformation");
        ArrayList<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(userInformation.getUsername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot cartSanpshot:snapshot.getChildren()){
                                CartModel cartModel = cartSanpshot.getValue(CartModel.class);
                                cartModel.setKey(cartSanpshot.getKey());
                                cartModels.add(cartModel);

                            }
                           Log.d("bbbbbbb", String.valueOf(cartModels));
                            PriceCartLoadSuccess(cartModels);
                            WeightCartLoadSucess(cartModels);

                        }else{
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }


    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void PriceCartLoadSuccess(List<CartModel> cartModelList) {
        userInformation = (user_information)getIntent().getSerializableExtra("userInformation");

        double sum = 0;
        for(CartModel cartModel : cartModelList){
            sum += cartModel.getTotalPrice();

        }
        Log.d("ttttttt", String.valueOf(sum));
        totalPrice.setText(new StringBuilder("$").append(df.format(sum)));

        MyCartAdapter adapter = new MyCartAdapter(this,cartModelList,userInformation);
        cartModelListcheckout = adapter.cartModelList;
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


    public void WeightCartLoadSucess(List<CartModel> cartModelList) {
        userInformation = (user_information)getIntent().getSerializableExtra("userInformation");
        Integer sum = 0;
        Integer sum1 = 0;
        Integer sum2 = 0;
        for(CartModel cartModel : cartModelList){
            sum += cartModel.getTotalWeight();
            sum1 += cartModel.getTotalWeight();
            sum2 += cartModel.getTotalWeight();

        }

        MyCartAdapter adapter = new MyCartAdapter(this,cartModelList,userInformation);
        cartModelListcheckout = adapter.cartModelList;
        totalweight = sum;

        if(sum <=100){
            sum1 -= 10;
            sum2 += 10;
        }else if (sum <=200){
            sum1 -= 20;
            sum2 += 20;
        }
        else if (sum <=300 && sum >200 ){
            sum1 -= 25;
            sum2 += 25;
        }
        else if (sum <=600 && sum >300){
            sum1 -= 30;
            sum2 += 30;
        }
        else if (sum <=900 && sum >600){
            sum1 -= 35;
            sum2 += 35;
        }
        else if (sum <=2000 && sum >900){
            sum1 -= 70;
            sum2 += 70;
        }
        else if (sum <=4000&& sum >2000){
            sum1 -= 140;
            sum2 += 140;
        }
        else if (sum <=6000&& sum >4000){
            sum1 -= 200;
            sum2 += 200;
        }
        else if (sum <=8000&& sum >6000){
            sum1 -= 300;
            sum2 += 300;
        }else{
            sum1 -= 400;
            sum2 += 400;

        }

        totalweightless = sum1;
        totalweightmore = sum2;
        Log.d("getweight", String.valueOf(sum));
        Log.d("getweight", String.valueOf(sum1));
        Log.d("getweight", String.valueOf(sum2));
    }


    public void checkOutCartItem(){
        userInformation = (user_information)getIntent().getSerializableExtra("userInformation");
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(userInformation.getUsername())
                .removeValue();
    }



}
