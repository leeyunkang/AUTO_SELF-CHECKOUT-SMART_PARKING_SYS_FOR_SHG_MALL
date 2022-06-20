package com.example.ezimart;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class Loading {
   private Activity activity;
    private AlertDialog dialog;

    Loading(Activity myActivity){

        activity = myActivity;
    }
    void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();


    }

    void dismissDialog(){

        dialog.dismiss();
    }
}
