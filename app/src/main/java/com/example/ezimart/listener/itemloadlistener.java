package com.example.ezimart.listener;


import com.example.ezimart.model.ItemModel;

import java.util.List;

import kotlinx.coroutines.channels.ChannelsKt;

public interface itemloadlistener {
    void onItemLoadSuccess(List<ItemModel> itemModelList);
     void onItemLoadFailed(String message);


}
