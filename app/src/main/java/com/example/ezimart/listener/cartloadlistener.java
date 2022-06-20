package com.example.ezimart.listener;



import com.example.ezimart.model.CartModel;

import java.util.List;

public interface cartloadlistener {



    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);


}
