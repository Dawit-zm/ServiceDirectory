package com.negute.servicedirectory.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.negute.servicedirectory.R;

import java.io.File;

public class RoyalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_royal);

//        File pics = new File(getFilesDir(), HomeActivity.PICS_DIR);
//        File albergoDir = new File(pics, getString(R.string.royal));
//        File heroFile = new File(albergoDir, "0.jpg");
//
//        findViewById(R.id.card1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), IndexdMenuActivity.class);
//                intent.putExtra(IndexdMenuActivity.PLACE, getString(R.string.royal));
//                intent.putExtra(IndexdMenuActivity.FOOD_MENU, true);
//                startActivity(intent);
//            }
//        });

        ImageView hero = findViewById(R.id.hero);
        //Glide.with(getApplicationContext()).load(Uri.fromFile(heroFile)).into(hero);
    }
}
