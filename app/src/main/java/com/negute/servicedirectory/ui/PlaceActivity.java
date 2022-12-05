package com.negute.servicedirectory.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.negute.servicedirectory.R;

import java.io.File;

public class PlaceActivity extends AppCompatActivity {

    public static final String PLACE_EXTRA = "PLACE_EXTRA";

    private ImageView hero;
    private TextView title;
    private TextView addr;
    private Button call;
    private Button navigate;
    private Button menu;
    private Button gallery;
    private TextView speciality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        if (!getIntent().hasExtra(PLACE_EXTRA)){
            throw new IllegalArgumentException("Sucker!");
        }



        // Map views
        hero = findViewById(R.id.hero);
        title = findViewById(R.id.title);
        addr = findViewById(R.id.addr);
        call = findViewById(R.id.tel);
        navigate = findViewById(R.id.navigate);
        gallery = findViewById(R.id.gallery);
        menu = findViewById(R.id.menu);
        speciality = findViewById(R.id.speciality);


        final Place place = (Place) getIntent().getSerializableExtra(PLACE_EXTRA);
        Log.d(PlaceActivity.class.getSimpleName(), String.format("onCreate: %s has gallery %b", place.title, place.hasGallery));
        Glide.with(getApplicationContext()).load(Uri.fromFile(getHeroFile(place.id))).into(hero);

        title.setText(place.title);
        addr.setText(place.addr);
        speciality.setText(getString(R.string.speciality));
        speciality.append(place.spec);

        if (Integer.valueOf(place.tel.trim()) == 0) {
            call.setVisibility(View.GONE);
        }

        if (!place.hasGallery) {
            gallery.setVisibility(View.GONE);
        }

        final String lat = place.coord.split("–")[0];
        final String longt =  place.coord.split("–")[1];

        if (Double.valueOf(lat.trim()) == 0 && Double.valueOf(longt.trim()) == 0) navigate.setVisibility(View.GONE);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+place.tel));
                startActivity(i);
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("geo: " + lat + "," + longt));
                startActivity(i);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlaceActivity.this, IndexdMenuActivity.class);
                i.putExtra(IndexdMenuActivity.ID_EXTRA, place.id);
                startActivity(i);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlaceActivity.this, GalleryActivity.class);
                i.putExtra(GalleryActivity.PLACE_ID_EXTRA, place.id);
                startActivity(i);
            }
        });

    }

    private File getHeroFile(String id){
        File pics = new File(getFilesDir(), Constants.PICS_DIR);
        File dir = new File(pics, id);
        return new File(dir, "0.jpg");
    }
}
