package com.example.ezimart;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezimart.adapter.MyItemAdapter;
import com.example.ezimart.eventbus.MyUpdateCartEvent;
import com.example.ezimart.listener.RecyclerViewClickListener;
import com.example.ezimart.listener.cartloadlistener;
import com.example.ezimart.listener.itemloadlistener;
import com.example.ezimart.model.CartModel;
import com.example.ezimart.model.ItemModel;
import com.example.ezimart.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FindProductActivity extends AppCompatActivity implements cartloadlistener, itemloadlistener,RecyclerViewClickListener {

    @BindView(R.id.recycler_product)
    RecyclerView recyclerView;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
//    @BindView(R.id.button3)
//    Button button;

    // SearchView searchView;
    private List<ItemModel> itemList;
    private List<ItemModel> filter;

    itemloadlistener itemloadlistener;
    cartloadlistener Cartloadlistener;
    user_information userInformation;
    MyItemAdapter myItemAdapter;

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
        setContentView(R.layout.activity_findproduct);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        userInformation= (user_information)getIntent().getSerializableExtra("userInformation");
        init();
        loadItemFromFirebase();
        countCartItem();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(FindProductActivity.this, MainActivity.class));
        finish();
    }

    protected void loadItemFromFirebase(){

        List<ItemModel> itemModels =new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ) {
                if(snapshot.exists()){
                    for(DataSnapshot itemSnapshot:snapshot.getChildren()){
                        ItemModel itemModel = itemSnapshot.getValue(ItemModel.class);
                        itemModel.setKey(itemSnapshot.getKey());
                        itemModels.add(itemModel);
                    }
                    itemList = itemModels;
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
        setOnClickListener();
        ButterKnife.bind(this);
        itemloadlistener = this;
        Cartloadlistener= this;

        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //  recyclerView.setLayoutManager(linearLayoutManager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration());

        /*
        button.findViewById(R.id.button3);
        button.setOnClickListener(v -> {
              onItemLoadSuccessFilter(filter);
        });
        */
    }

    private void setOnClickListener() {
        MyItemAdapter.RecycleViewClickListener listener = new MyItemAdapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int positoin) {

               Intent intent = new Intent(getApplicationContext(), ProductLocation.class);
               Log.d("lllllll","ok");
                //  intent.putExtra("username",);

               startActivity(intent);
            }
        };
    }

    @Override
    public void onItemLoadSuccess(List<ItemModel> itemModelList) {
        user_information userInformation = (user_information)getIntent().getSerializableExtra("userInformation");
        MyItemAdapter adapter= new MyItemAdapter( this,itemModelList,Cartloadlistener,userInformation);
        recyclerView.setAdapter(adapter);
        Log.d("lllllll","ok1");
        //recyclerView.setAdapter(null);
    }

    public void onItemLoadSuccessFilter(List<ItemModel> filter ) {

        user_information userInformation = (user_information)getIntent().getSerializableExtra("userInformation");
        MyItemAdapter adapter= new MyItemAdapter(this,filter,Cartloadlistener,userInformation);
        recyclerView.setAdapter(adapter);
        //  recyclerView.notify();
        //recyclerView.setAdapter(null);
    }

    @Override
    public void onItemLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {

    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }

   @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        userInformation= (user_information)getIntent().getSerializableExtra("userInformation");
        List<CartModel> cartModels = new ArrayList<>();
        DatabaseReference itemCart = FirebaseDatabase.getInstance().getReference("Cart").child(userInformation.getUsername());
        //DatabaseReference itemCart = FirebaseDatabase.getInstance().getReference("Cart").child(FirebaseAuth.getInstance().getUid());

        itemCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot cartSnapshot:snapshot.getChildren()){
                    CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                    cartModel.setKey(cartSnapshot.getKey());
                    cartModels.add(cartModel);
                }
                Cartloadlistener.onCartLoadSuccess(cartModels);
            }@Override
            public void onCancelled(@NonNull DatabaseError error) {
                Cartloadlistener.onCartLoadFailed(error.getMessage());
            }
        });
    }

    public boolean onOptionItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.findproductlocationmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String searchStr = newText;

                filterList(newText);
                return false;
            }
        });
        return false;
    }

    private void filterList(String text) {

        List<ItemModel>  filteredList = new ArrayList<>();
        for(ItemModel itemModel : itemList ){
            if(itemModel.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(itemModel);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this,"nodata",Toast.LENGTH_SHORT).show();
        }else{
            filter = filteredList;
            onItemLoadSuccessFilter(filter);
        }
    }

    @Override
    public void onRecyclerClick(View view, int position) {

    }



/*
public boolean onCreateOptionMenu(Menu menu){

        getMenuInflater().inflate(R.menu.findproductlocationmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchStr = newText;
          //  MyItemAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


}


    public boolean onCreateOptionMenu(Menu menu){

        getMenuInflater().inflate(R.menu.findproductlocationmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchStr = newText;
                 myItemAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }*/
}