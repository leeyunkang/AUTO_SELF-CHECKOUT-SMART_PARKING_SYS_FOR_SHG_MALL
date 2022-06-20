package com.example.ezimart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.example.ezimart.R;

import com.bumptech.glide.Glide;
import com.example.ezimart.eventbus.MyUpdateCartEvent;
import com.example.ezimart.ProductLocation;
import com.example.ezimart.listener.RecyclerViewClickListener;
import com.example.ezimart.listener.cartloadlistener;
import com.example.ezimart.model.CartModel;
import com.example.ezimart.model.ItemModel;
import com.example.ezimart.user_information;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyItemViewHolder> {


    public RecyclerViewClickListener listener;

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return null;
        return new MyItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false));
        //return new MyItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false));
    }
    public MyItemAdapter(){}

    public MyItemAdapter(Context context, List<ItemModel> itemModelList, cartloadlistener cartloadlistener, user_information userinformation) {
        this.context = context;

        this.itemModelList = itemModelList;
        this.userinformation = userinformation;
        this.getItemModelListFilter= itemModelList;
        Cartloadlistener = cartloadlistener;
    }
    private user_information userinformation;
    private Context context;
    public List<ItemModel> itemModelList;
    private  cartloadlistener  Cartloadlistener;
    public List<ItemModel>getItemModelListFilter;

    public void setFilterList(List<ItemModel>filterList){
        //this.itemModelList = filterList;
        //notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {
        Glide.with(context)
        .load(itemModelList.get(position).getImage())
        .into(holder.imageView);
        holder.txtprice.setText(new StringBuilder("RM").append(itemModelList.get(position).getPrice()));
        holder.txtname.setText(new StringBuilder().append(itemModelList.get(position).getName()));

        holder.setListener((view, adapterPosition) -> {

        //openActivity();

Intent intent = new Intent(context, ProductLocation.class);
intent.putExtra("category",itemModelList.get(position).getCategory());
            intent.putExtra("weight",itemModelList.get(position).getWeight());
context.startActivity(intent);
       // addToCart(itemModelList.get(position));
});
    }
public void openActivity(){
      //  Intent intent = new Intent(this);
       // starActivity(intent);
}

public interface RecycleViewClickListener{
        void onClick(View v, int positoin);
}

    private void addToCart(ItemModel itemModel) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(userinformation.getUsername());
        userCart.child(itemModel.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//Toast.makeText(MainActivity.this, userInformation.getEmail(), Toast.LENGTH_SHORT).show();

if(snapshot.exists()){
    CartModel cartModel= snapshot.getValue(CartModel.class);
    cartModel.setQuantity(cartModel.getQuantity()+1);
    Map<String,Object> updateData = new HashMap<>();
    updateData.put("quantity",cartModel.getQuantity());
    updateData.put("totalPrice",cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));
    updateData.put("totalWeight",cartModel.getQuantity()*Integer.parseInt(cartModel.getWeight()));

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
cartModel.setQuantity(1);
cartModel.setTotalPrice(Float.parseFloat(itemModel.getPrice()));

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

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }


    public class MyItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

@BindView(R.id.imageView)
ImageView imageView;
@BindView(R.id.txtname)
TextView txtname;
@BindView(R.id.txtprice)
TextView txtprice;


        public void setListener(RecyclerViewClickListener listener) {
            this.listener = listener;
        }

        RecyclerViewClickListener listener;

private Unbinder unbinder;

    public MyItemViewHolder(@NonNull View itemView) {
        super(itemView);
        unbinder = ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {

        listener.onRecyclerClick(v,getAbsoluteAdapterPosition());

        }
    }

}
