package com.example.ezimart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class chatbot extends AppCompatActivity {
    private ArrayList<Message> messages;
    private RecyclerView recyclerView;
    private recyclerAdapter adapter;
    private ImageButton sendButton;
    private EditText msgInput;
    private getRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        request = new getRequest(this);

        recyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        messages = new ArrayList<>();
        adapter = new recyclerAdapter(messages);
        recyclerView.setAdapter(adapter);

        sendButton = (ImageButton) findViewById(R.id.msgButton);
        msgInput = (EditText) findViewById(R.id.msgInput);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msgInput.getText().toString();
                if(message.length() != 0){
                    messages.add(new Message(true, message));
                    int newPosition = messages.size() - 1;
                    adapter.notifyItemInserted(newPosition);
                    recyclerView.scrollToPosition(newPosition);
                    msgInput.setText("");
                    getReply(message);
                }
            }
        });

        if (isNetwork(getApplicationContext())){

            Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "Internet Is Not Connected", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isNetwork(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



    private void getReply(String message) {
        request.getResponse(message, new getRequest.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("REQUEST ERROR", message);
            }

            @Override
            public void onResponse(String reply) {
                if(reply.equals("")){
                    messages.add(new Message(false, "I’m sorry, I can’t understand the Question."));
                    int newPosition = messages.size() - 1;
                    adapter.notifyItemInserted(newPosition);
                    recyclerView.scrollToPosition(newPosition);

                }else{

                    messages.add(new Message(false, reply));
                    int newPosition = messages.size() - 1;
                    adapter.notifyItemInserted(newPosition);
                    recyclerView.scrollToPosition(newPosition);

                }

            }
        });

    }

}