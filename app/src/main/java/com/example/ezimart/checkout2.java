package com.example.ezimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.support.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class checkout2 extends AppCompatActivity {
    SurfaceView cameraView;
    TextView textView,textView2;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    user_information userInformation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);
        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        textView = (TextView) findViewById(R.id.text_view);
        textView2 = (TextView) findViewById(R.id.text_view2);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(checkout2.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size()!=0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder=new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    // stringBuilder.append("\n");
                                }
                                Intent intent = getIntent();

                                Integer temp , temp1,temp2;
                                temp= intent.getIntExtra("doubleValue_e1",0);
                                temp1= intent.getIntExtra("doubleValue_e2",0);
                                temp2= intent.getIntExtra("doubleValue_e3",0);
                                textView.setText(String.valueOf(temp));

                                String strNew = stringBuilder.toString().replaceAll("([a-z])", "");
                                strNew = stringBuilder.toString().replaceAll("([a-zA-Z])", "");
                                try {
                                    int x = Integer.parseInt(strNew);

                                    if(x>= temp1 && x <= temp2 ){

                                        textView2.setText("vertify");
                                        checkOutCartItem();
                                        Intent intent1 = new Intent (checkout2.this,checkout.class);
                                        user_information  userInformationshopnow = new user_information(userInformation.getUsername(),userInformation.getFull_name(),userInformation.getPassword(),userInformation.getEmail(),userInformation.getMobile_number(),userInformation.getResidential_address());
                                        intent1.putExtra("userInformation",userInformationshopnow);
                                        intent1.putExtra("verify",9999);
                                        startActivity(intent1);
                                        finish();
                                    }else{


                                    }

                                    // You can use this method to convert String to int, But if input
                                    //is not an int  value then this will throws NumberFormatException.
                                   // System.out.println("Valid input");
                                }catch(NumberFormatException e) {
                                  //  System.out.println("input is not an int value");
                                    // Here catch NumberFormatException
                                    // So input is not a int.
                                }


                            }
                        });
                    }
                }
            });
        }
    }



    public void checkOutCartItem(){
        userInformation = (user_information)getIntent().getSerializableExtra("userInformation");
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(userInformation.getUsername())
                .removeValue();
    }


}
