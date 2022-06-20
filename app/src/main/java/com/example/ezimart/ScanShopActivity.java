package com.example.ezimart;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.ezimart.eventbus.MyUpdateCartEvent;
import com.example.ezimart.listener.cartloadlistener;
import com.example.ezimart.listener.itemloadlistener;
import com.example.ezimart.model.CartModel;
import com.example.ezimart.model.ItemModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanShopActivity extends AppCompatActivity implements cartloadlistener, itemloadlistener {
    @BindView(R.id.badge2)
    NotificationBadge badge;
    @BindView(R.id.btnCart2)
    FrameLayout btncart;

    itemloadlistener itemloadlistener;
    cartloadlistener Cartloadlistener;
    private List<ItemModel> itemList;
    ImageButton btn_scan;
    user_information userInformation;
    String Result;
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_shop);
        btn_scan =findViewById(R.id.btn_scan);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        btn_scan.setOnClickListener(v->
        {
            scanCode();
        });



        userInformation= (user_information)getIntent().getSerializableExtra("userInformation");
         Log.d("test",userInformation.getUsername());
        init();
        loadItemFromFirebase();
        countCartItem();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getscannerResult (String key){
        List<ItemModel> itemModels =new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Product")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot ) {
                        if(snapshot.exists()){

                            for(DataSnapshot itemSnapshot:snapshot.getChildren()){

                                ItemModel itemModel = itemSnapshot.getValue(ItemModel.class);
                                itemModel.setKey(itemSnapshot.getKey());

                                if(itemModel.getKey().equals(key)){
                                    Toast.makeText(ScanShopActivity.this,itemModel.getKey() , Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ScanShopActivity.this,itemModel.getName() , Toast.LENGTH_SHORT).show();

                                    addToCart(itemModel);
                                }else{

                                }
                            }
                        }else
                            itemloadlistener.onItemLoadFailed("Can't find this item");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //  throw error.toException();
                        itemloadlistener.onItemLoadFailed(error.getMessage());
                    }
                });
       // addToCart(itemList.);
    }

    private void addToCart(ItemModel itemModel) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(userInformation.getUsername());
        userCart.child(itemModel.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            CartModel cartModel= snapshot.getValue(CartModel.class);
                            cartModel.setQuantity(cartModel.getQuantity()+1);
                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("quantity",cartModel.getQuantity());
                           updateData.put("totalWeight",cartModel.getQuantity()*Integer.parseInt(cartModel.getWeight()));
                            updateData.put("totalPrice",cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));
Log.d("getweight3", String.valueOf(cartModel.getQuantity()*Integer.parseInt(cartModel.getWeight())));
                            userCart.child(itemModel.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(aVoid -> {
                                        Cartloadlistener.onCartLoadFailed("Add To Cart Success");
                                    })
                                    .addOnFailureListener(e -> Cartloadlistener.onCartLoadFailed(e.getMessage()));
                        }else{
                            CartModel cartModel = new CartModel();
                            cartModel.setName(itemModel.getName());
                            cartModel.setImage(itemModel.getImage());
                            cartModel.setKey(itemModel.getKey());
                            cartModel.setPrice(itemModel.getPrice());
                            cartModel.setWeight(itemModel.getWeight());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(Float.parseFloat(itemModel.getPrice()));
                            cartModel.setTotalWeight(Integer.parseInt(itemModel.getWeight()));
                            userCart.child(itemModel.getKey())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(aVoid -> {
                                        Cartloadlistener.onCartLoadFailed("Add To Cart Success");
                                    })
                                    .addOnFailureListener(e -> Cartloadlistener.onCartLoadFailed(e.getMessage()));
                        }

                        EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Cartloadlistener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    protected void loadItemFromFirebase(){
        List<ItemModel> itemModels =new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Product")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot ) {
                        if(snapshot.exists()){
                            for(DataSnapshot itemSnapshot:snapshot.getChildren()){
                                ItemModel itemModel = itemSnapshot.getValue(ItemModel.class);
                                itemModel.setKey(itemSnapshot.getKey());
                                itemModels.add(itemModel);
                            }
                            itemList= itemModels;
                            itemloadlistener.onItemLoadSuccess(itemModels);
                        }else
                            itemloadlistener.onItemLoadFailed("Can't find this item");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //  throw error.toException();
                        itemloadlistener.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void init(){
        ButterKnife.bind(this);
        itemloadlistener = this;
       Cartloadlistener= this;
        //testing linear


        // btnCart.setOnClickListener(v -> startActivity(new Intent(this,CartActivity.class)));
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInformation= (user_information)getIntent().getSerializableExtra("userInformation");
                Intent intent = new Intent (ScanShopActivity.this,CartActivity.class);
                user_information  userInformationcart = new user_information(userInformation.getUsername(),userInformation.getFull_name(),userInformation.getPassword(),userInformation.getEmail(),userInformation.getMobile_number(),userInformation.getResidential_address());
                intent.putExtra("userInformation",userInformationcart);
                startActivity(intent);
            }
        });
    }

    public void onItemLoadSuccess2(List<ItemModel> filter ) {

        user_information userInformation = (user_information)getIntent().getSerializableExtra("userInformation");

    }

    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int cartSum = 0;
        for(CartModel cartModel:cartModelList){
            cartSum += cartModel.getQuantity();
            badge.setNumber(cartSum);}
    }

    @Override
    public void onCartLoadFailed(String message) {
     //   Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        userInformation= (user_information)getIntent().getSerializableExtra("userInformation");

        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child(userInformation.getUsername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot cartsnapshot:snapshot.getChildren()){
                            CartModel cartModel = cartsnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartsnapshot.getKey());
                            cartModels.add(cartModel);
                        }


                        Cartloadlistener.onCartLoadSuccess(cartModels);
                    }@Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Cartloadlistener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanShopActivity.this);
            builder.setTitle("Result");
           // builder.setMessage(result.getContents());

            Result = result.getContents();
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.dismiss();
                    getscannerResult(result.getContents());
                }
            }).show();
        }
    });

    @Override
    public void onItemLoadSuccess(List<ItemModel> itemModelList) {

    }

    @Override
    public void onItemLoadFailed(String message) {

    }
}