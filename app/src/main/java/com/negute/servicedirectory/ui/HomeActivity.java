package com.negute.servicedirectory.ui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.negute.servicedirectory.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        new TemplateExtractorAsyncTask(getAssets(), getFilesDir()).execute(
                getResources().getStringArray(R.array.places));

        final ItemAdapter<HomeActivity.ListItem> itemItemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemItemAdapter).withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, IAdapter<IItem> adapter, IItem item, int position) {
                Log.d("HOME", "onClick: launching activity");
                Intent intent = new Intent(HomeActivity.this, PlaceActivity.class);

                intent.putExtra(PlaceActivity.PLACE_EXTRA, (Place) v.getTag());
                startActivity(intent);
                return true;
            }
        });


        itemItemAdapter.add(getItems(getString(R.string.restaurant)));

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastAdapter);

        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableItemShiftingMode(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                itemItemAdapter.clear();
                itemItemAdapter.set(getItems(item.getTitle().toString()));
                return true;
            }
        });
    }

    private List<HomeActivity.ListItem> getItems(String cat){
        String[] places = null;
        List<ListItem> items = new ArrayList<>();
        if (cat.equals(getString(R.string.restaurant))){
            places = getResources().getStringArray(R.array.restaurants);
        } else if (cat.equals(getString(R.string.hotel))){
            places = getResources().getStringArray(R.array.hotels);
        } else if (cat.equals(getString(R.string.cafe))){
            places = getResources().getStringArray(R.array.cafes);
        } else if (cat.equals(getString(R.string.fast_food))){
            places = getResources().getStringArray(R.array.fast_foods);
        } else if (cat.equals(getString(R.string.bar))){
            places = getResources().getStringArray(R.array.bars);
        }

        if (places == null) return items;

        for (String info: places){
            String[] parts = info.split(",");
            Place place = new Place(parts[0], parts[1], parts[2], parts[5], parts[3], parts[4], parts[6].trim().equalsIgnoreCase("1"));
            Log.d("GALL", String.format("getItems: has gallery %b", place.hasGallery));

            ListItem item = new ListItem(place);
            items.add(item);
        }

        return items;
    }



    public class ListItem extends AbstractItem<HomeActivity.ListItem, HomeActivity.ListItem.ViewHolder > {

        public Place place;


        public ListItem(Place place) {
            this.place = place;
        }

        @NonNull
        @Override
        public HomeActivity.ListItem.ViewHolder getViewHolder(View v) {
            return new  HomeActivity.ListItem.ViewHolder(v);
        }

        @Override
        public int getType() {
            return R.id.fastadapter_item_adapter;
        }



        @Override
        public int getLayoutRes() {
            return R.layout.place_item_layoyt;
        }

        protected class ViewHolder extends FastAdapter.ViewHolder<HomeActivity.ListItem> implements View.OnClickListener {
            TextView name;
            ImageView placeImg;
            Place place;


            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.placeTitle);
                placeImg = itemView.findViewById(R.id.placeImg);
            }


            @Override
            public void bindView(HomeActivity.ListItem item, List<Object> payloads) {
                name.setText(item.place.title);
                this.place = item.place;
                itemView.setTag(place);

                Glide.with(getApplicationContext()).load(Uri.fromFile(getHeroFile(place.id))).into(placeImg);
            }

            @Override
            public void unbindView(HomeActivity.ListItem item) {
                name.setText(null);
            }

            @Override
            public void onClick(View view) {

            }
        }

    }


    private static class TemplateExtractorAsyncTask extends AsyncTask<String[], Void, Boolean> {

        private AssetManager assetManager;
        private File filesDir;

        TemplateExtractorAsyncTask(AssetManager assetManager, File filesDir) {
            this.assetManager = assetManager;
            this.filesDir = filesDir;
        }


        @Override
        protected Boolean doInBackground(String[]... strings) {

            for (String[] paths : strings) {


                File dirPath = new File(filesDir, Constants.PICS_DIR);
                dirPath.mkdirs();


                for (String path : paths) {
                    try {
                        String[] files = assetManager.list(path);
                        for (String file : files) {
                            File subDir = new File(dirPath, path);
                            subDir.mkdirs();

                            InputStream is = assetManager.open(path + "/" + file);
                            File outFile = new File(subDir, file);

                            if (outFile.exists()) continue;

                            OutputStream fileOutputStream = new FileOutputStream(outFile);
                            byte[] data = new byte[is.available()];
                            is.read(data);
                            fileOutputStream.write(data);
                            is.close();
                            fileOutputStream.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
        }


    }

    private File getHeroFile(String id){
        File pics = new File(getFilesDir(), Constants.PICS_DIR);
        File dir = new File(pics, id);
        return new File(dir, "0.jpg");
    }
}
