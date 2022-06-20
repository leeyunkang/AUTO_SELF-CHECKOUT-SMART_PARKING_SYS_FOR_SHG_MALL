package com.example.ezimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezimart.adapter.MyCartAdapter;
import com.example.ezimart.eventbus.MyUpdateCartEvent;
import com.example.ezimart.listener.cartloadlistener;
import com.example.ezimart.model.CartModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements cartloadlistener {

    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart ;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    //@BindView(R.id.btnBack)
    //ImageView btnBack;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    private Button checkoutbtn;

    public static boolean del = false;
    cartloadlistener Cartloadlistener;
    public List<CartModel> cartModelList;
    user_information userInformation;

    Double fintotal =0.0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        checkoutbtn = findViewById(R.id.checkout);
        user_information userInformation = (user_information)getIntent().getSerializableExtra("userInformation");
        content();
        init();
        loadCartFromFirebase();

        Log.d("oooooooo", String.valueOf(del));


        checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, checkout.class);
                user_information userInformationmore = new user_information(userInformation.getUsername(), userInformation.getFull_name(), userInformation.getPassword(), userInformation.getEmail(), userInformation.getMobile_number(), userInformation.getResidential_address());
                intent.putExtra("userInformation", userInformationmore);
                startActivity(intent);


            }
        });

    }


    public static void changedel(){
        del = true;
    }
    public static Boolean delfun()
    {
        return del;
    }

    public void content(){
        Log.d("oooooooo", String.valueOf(del));
       if(del==true){
           this.recreate();
           del = false;
       }else{

       }

        loadCartFromFirebase();
        refresh(1000);

    }


    private void refresh(int milliseconds){
        final Handler hanler = new Handler();
        final Runnable runnable=  new Runnable() {
            @Override
            public void run() {
                content();
            }
        };


        hanler.postDelayed(runnable,milliseconds);

    }

    private void init(){

        ButterKnife.bind(this); //avoid repeat read R.id
        Cartloadlistener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        //recycler view allocation
   //
    }

    public    void loadCartTotalPriceFromFirebase( ){
        ArrayList<CartModel> cartModels = new ArrayList<>();

        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot cartSanpshot:snapshot.getChildren()){
                                CartModel cartModel = cartSanpshot.getValue(CartModel.class);
                                cartModel.setKey(cartSanpshot.getKey());
                                cartModels.add(cartModel);

                                               }

                            PriceCartLoadSuccess(cartModels);
                          //  Log.d("new",cartModels.get(01).getName());


                        }else{

                        }
//return cartModels;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
    }

    protected void loadCartFromFirebase(){
        user_information userInformation = (user_information)getIntent().getSerializableExtra("userInformation");

        List<CartModel> cartModels = new ArrayList<>();
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


                                //Log.d("new1","inside load database");
                            }
                            PriceCartLoadSuccess(cartModels);
                            //Log.d("new",cartModels.get(0).getName());
                            Cartloadlistener.onCartLoadSuccess(cartModels);
                        }else{
                          //  Cartloadlistener.onCartLoadFailed("Cart empty");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Cartloadlistener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event){
       loadCartFromFirebase();
    }



    public void PriceCartLoadSuccess(List<CartModel> cartModelList1){
        //cartModelList.this =  cartModelList1;
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        userInformation = (user_information)getIntent().getSerializableExtra("userInformation");


    double sum = 0;
     int weightsum = 0;
    for(CartModel cartModel : cartModelList){
    sum += cartModel.getTotalPrice();
        weightsum += cartModel.getTotalWeight();
    }
        txtTotal.setText(new StringBuilder("RM").append(df.format(sum)));
        Log.d("ttttttfintotal", String.valueOf(fintotal));
        Log.d("ttttttsum", String.valueOf(sum));

MyCartAdapter adapter = new MyCartAdapter(this,cartModelList,userInformation);


recyclerCart.setAdapter(adapter);
        adapter.notifyDataSetChanged();

   }

    @Override
    public void onCartLoadFailed(String message) {

        Snackbar.make(mainLayout,message, Snackbar.LENGTH_LONG).show();

    }


}