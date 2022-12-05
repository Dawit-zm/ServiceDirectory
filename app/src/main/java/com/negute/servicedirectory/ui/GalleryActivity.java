package com.negute.servicedirectory.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.negute.servicedirectory.R;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {

    public static final String PLACE_ID_EXTRA = "PLACE_ID_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if (!getIntent().hasExtra(PLACE_ID_EXTRA)){
            throw new IllegalArgumentException();
        }

        String id = getIntent().getStringExtra(PLACE_ID_EXTRA);
        File imageDir = getPlaceDir(id);
        ImageViewerAdapter imageViewerAdapter = new ImageViewerAdapter(imageDir);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(imageViewerAdapter);

    }

    private File getPlaceDir(String id){
        File picsDir = new File(getFilesDir(), Constants.PICS_DIR);
        return new File(picsDir, id);
    }
}
