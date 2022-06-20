package com.example.ezimart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ProductLocation extends AppCompatActivity{

ImageView[] imageView= new ImageView[32];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_location);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));

        String username;

        String Category,Weight;

        Bundle extras = getIntent().getExtras();
       imageView[0] = (ImageView) findViewById(R.id.groceryUp11);
       imageView[1] = (ImageView) findViewById(R.id.groceryUp12);
        imageView[2] = (ImageView) findViewById(R.id.groceryUp21);

        imageView[3] = (ImageView) findViewById(R.id.groceryUp22);
        imageView[4] = (ImageView) findViewById(R.id.groceryUp31);
        imageView[5] = (ImageView) findViewById(R.id.groceryUp32);
        imageView[6] = (ImageView) findViewById(R.id.groceryUp41);
        imageView[7] = (ImageView) findViewById(R.id.groceryUp42);
        imageView[8] = (ImageView) findViewById(R.id.groceryUp51);
        imageView[9] = (ImageView) findViewById(R.id.groceryUp52);
        imageView[10] = (ImageView) findViewById(R.id.groceryUp61);
        imageView[11] = (ImageView) findViewById(R.id.groceryUp62);


        imageView[12] = (ImageView) findViewById(R.id.groceryDown11);
        imageView[13] = (ImageView) findViewById(R.id.groceryDown12);
        imageView[14] = (ImageView) findViewById(R.id.groceryDown21);
        imageView[15] = (ImageView) findViewById(R.id.groceryDown22);
        imageView[16] = (ImageView) findViewById(R.id.groceryDown31);
        imageView[17] = (ImageView) findViewById(R.id.groceryDown32);
        imageView[18] = (ImageView) findViewById(R.id.groceryDown41);
        imageView[19] = (ImageView) findViewById(R.id.groceryDown42);

        imageView[20] = (ImageView) findViewById(R.id.frozen11);
        imageView[21] = (ImageView) findViewById(R.id.frozen12);
        imageView[22] = (ImageView) findViewById(R.id.frozen21);
        imageView[23] = (ImageView) findViewById(R.id.frozen22);
        imageView[24] = (ImageView) findViewById(R.id.frozen31);
        imageView[25] = (ImageView) findViewById(R.id.frozen32);

        imageView[26] = (ImageView) findViewById(R.id.produce1);
        imageView[27] = (ImageView) findViewById(R.id.produce2);
        imageView[28] = (ImageView) findViewById(R.id.produce3);
        imageView[29] = (ImageView) findViewById(R.id.produce4);
        imageView[30] = (ImageView) findViewById(R.id.produce5);
        imageView[31] = (ImageView) findViewById(R.id.produce6);

         username= extras.getString("key");
        Category = extras.getString("category");
        Weight = extras.getString("weight");

        if(extras != null){
            for (int i = 0 ; i < 32; i++){
                imageView[i].setVisibility(View.INVISIBLE);
            }

if (Category.equals("Snacks")){

    if(Integer.parseInt(Weight)<10){
        imageView[0].setVisibility(View.VISIBLE);
    }else if(Integer.parseInt(Weight)>=10 && Integer.parseInt(Weight)<20){
        imageView[1].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=20 && Integer.parseInt(Weight)<30){
        imageView[2].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=30 && Integer.parseInt(Weight)<40){
        imageView[3].setVisibility(View.VISIBLE);
    }else{
        imageView[4].setVisibility(View.VISIBLE);
    }


}else if(Category.equals("Candy")){

    if(Integer.parseInt(Weight)<10){
        imageView[5].setVisibility(View.VISIBLE);
    }else if(Integer.parseInt(Weight)>=10 && Integer.parseInt(Weight)<25){
        imageView[6].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=25 && Integer.parseInt(Weight)<50){
        imageView[7].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=50 && Integer.parseInt(Weight)<100){
        imageView[8].setVisibility(View.VISIBLE);
    }else{
        imageView[9].setVisibility(View.VISIBLE);
    }



}else if(Category.equals("Beverages")){


    if(Integer.parseInt(Weight)<300){
        imageView[20].setVisibility(View.VISIBLE);
    }else if(Integer.parseInt(Weight)>=300 && Integer.parseInt(Weight)<600){
        imageView[21].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=600 && Integer.parseInt(Weight)<900){
        imageView[22].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=900 && Integer.parseInt(Weight)<1200){
        imageView[23].setVisibility(View.VISIBLE);
    }else{
        imageView[24].setVisibility(View.VISIBLE);
    }

            }else if (Category.equals("Chocolate")){

    if(Integer.parseInt(Weight)<25){
        imageView[10].setVisibility(View.VISIBLE);
    }else if(Integer.parseInt(Weight)>=25 && Integer.parseInt(Weight)<35){
        imageView[11].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=35 && Integer.parseInt(Weight)<45){
        imageView[12].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=45 && Integer.parseInt(Weight)<55){
        imageView[13].setVisibility(View.VISIBLE);
    }else{
        imageView[14].setVisibility(View.VISIBLE);
    }



}else if (Category.equals("Coffee and Tea")){
    if(Integer.parseInt(Weight)<250){
        imageView[15].setVisibility(View.VISIBLE);
    }else if(Integer.parseInt(Weight)>=250 && Integer.parseInt(Weight)<350){
        imageView[16].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=350 && Integer.parseInt(Weight)<450){
        imageView[17].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=450 && Integer.parseInt(Weight)<650){
        imageView[18].setVisibility(View.VISIBLE);
    }else{
        imageView[19].setVisibility(View.VISIBLE);
    }
}
else if(Category.equals("Food and Noodles")){
    if(Integer.parseInt(Weight)<400){
        imageView[26].setVisibility(View.VISIBLE);
    }else if(Integer.parseInt(Weight)>=400 && Integer.parseInt(Weight)<500){
        imageView[27].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=500 && Integer.parseInt(Weight)<600){
        imageView[28].setVisibility(View.VISIBLE);
    }
    else if(Integer.parseInt(Weight)>=600 && Integer.parseInt(Weight)<700){
        imageView[29].setVisibility(View.VISIBLE);
    }else{
        imageView[30].setVisibility(View.VISIBLE);
    }
}else{}
       }


     /*  */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(ProductLocation.this, MainActivity.class));
        finish();
    }



}