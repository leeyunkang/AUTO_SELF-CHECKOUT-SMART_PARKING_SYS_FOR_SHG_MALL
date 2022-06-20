package com.example.ezimart.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ezimart.CartActivity;
import com.example.ezimart.R;
import com.example.ezimart.eventbus.MyUpdateCartEvent;
import com.example.ezimart.model.CartModel;
import com.example.ezimart.user_information;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

    private Context context;
    public List<CartModel> cartModelList;
    user_information userInformation;


    public MyCartAdapter(Context context, List<CartModel> cartModelList,user_information userInformation) {
        this.context = context;
        this.cartModelList = cartModelList;
        this.userInformation = userInformation;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
        getcart(cartModelList);
        Glide.with(context)
                .load(cartModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("RM").append(cartModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(cartModelList.get(position).getName()));
        holder.txtQuantity.setText(new StringBuilder().append(cartModelList.get(position).getQuantity()));


        holder.btnMinus.setOnClickListener(v -> {
            minusCartItem(holder,cartModelList.get(position));


        });
        holder.btnPlus.setOnClickListener(v -> {

            plusCartItem(holder,cartModelList.get(position));

        });



        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Do you really want to delete item")

                    .setNegativeButton("CANCEL", (dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton("OK", (dialog12, which) -> {
                        deleteFromFirebase(cartModelList.get(position));
                        notifyItemRemoved(position);
                        notifyDataSetChanged();

                        CartActivity.changedel();
                        dialog12.dismiss();


                    }).create();
            dialog.show();




        });


    }


    private void deleteFromFirebase(CartModel cartModel) {

        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(userInformation.getUsername())
                .child(cartModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));


    }

    private void plusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        DecimalFormat df = new DecimalFormat("#.00");
        float total;
        cartModel.setQuantity(cartModel.getQuantity()+1);
        cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));
        cartModel.setTotalWeight(cartModel.getQuantity()*Integer.parseInt(cartModel.getWeight()));

        holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
        updateFirebase(cartModel);


    }






    private void minusCartItem(MyCartViewHolder holder, CartModel cartModel){
        DecimalFormat df = new DecimalFormat("#.00");
        if(cartModel.getQuantity()>1){
            cartModel.setQuantity(cartModel.getQuantity()-1);
            cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));
            // cartModel.setTotalPrice(Float.valueOf(df.format(cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()))));
            //update quantity
            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
            updateFirebase(cartModel);

        }

    }

    private void updateFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(userInformation.getUsername())
                .child(cartModel.getKey())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }



    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public List<CartModel> getcart(List<CartModel> cartModelList1){
        cartModelList = cartModelList1;


        return cartModelList;
    }

    public class MyCartViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.btnMinus)
        ImageView btnMinus;
        @BindView(R.id.btnPlus)
        ImageView btnPlus;
        @BindView(R.id.btnDelete)
        ImageView btnDelete;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtpriceCart)
        TextView txtPrice;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;


        Unbinder unbinder;




        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);

        }


    }

}
