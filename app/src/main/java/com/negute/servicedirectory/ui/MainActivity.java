package com.negute.servicedirectory.ui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ItemAdapter<MainActivity.ListItem> itemItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new TemplateExtractorAsyncTask(getAssets(), getFilesDir()).execute(
                getResources().getStringArray(R.array.places));

        itemItemAdapter = new ItemAdapter<>();
        FastAdapter fastAdapter = FastAdapter.with(itemItemAdapter).withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, IAdapter<IItem> adapter, IItem item, int position) {
                Log.d("HOME", "onClick: launching activity");
                Intent intent = new Intent(MainActivity.this, PlaceActivity.class);

                intent.putExtra(PlaceActivity.PLACE_EXTRA, (Place) v.getTag());
                startActivity(intent);
                return true;
            }
        });


        itemItemAdapter.set(getItems(getString(R.string.all_places)));

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastAdapter);

        setTitle(getString(R.string.app_name));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all){
            setTitle(getString(R.string.app_name));
        } else if (id == R.id.restaurant) {
            setTitle(getString(R.string.restaurant));
        } else if (id == R.id.bar) {
            setTitle(getString(R.string.bar));
        } else if (id == R.id.cafe) {
            setTitle(getString(R.string.cafe));
        } else if (id == R.id.fast_food) {
            setTitle(getString(R.string.fast_food));
        } else if (id == R.id.hotel) {
            setTitle(getString(R.string.hotel));
        } else if (id == R.id.about) {

        }

        itemItemAdapter.set(getItems(item.getTitle().toString()));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<MainActivity.ListItem> getItems(String cat){
        String[] places = null;
        List<MainActivity.ListItem> items = new ArrayList<>();
        if (cat.equals(getString(R.string.all_places))){
            places = getResources().getStringArray(R.array.all_places);
        } else if (cat.equals(getString(R.string.restaurant))){
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

            MainActivity.ListItem item = new MainActivity.ListItem(place);
            items.add(item);
        }

        return items;
    }

    public class ListItem extends AbstractItem<MainActivity.ListItem, MainActivity.ListItem.ViewHolder > {

        public Place place;


        public ListItem(Place place) {
            this.place = place;
        }

        @NonNull
        @Override
        public MainActivity.ListItem.ViewHolder getViewHolder(View v) {
            return new MainActivity.ListItem.ViewHolder(v);
        }

        @Override
        public int getType() {
            return R.id.fastadapter_item_adapter;
        }



        @Override
        public int getLayoutRes() {
            return R.layout.place_item_layoyt;
        }

        protected class ViewHolder extends FastAdapter.ViewHolder<MainActivity.ListItem> implements View.OnClickListener {
            TextView name;
            ImageView placeImg;
            Place place;


            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.placeTitle);
                placeImg = itemView.findViewById(R.id.placeImg);
            }


            @Override
            public void bindView(MainActivity.ListItem item, List<Object> payloads) {
                name.setText(item.place.title);
                this.place = item.place;
                itemView.setTag(place);

                Glide.with(getApplicationContext()).load(Uri.fromFile(getHeroFile(place.id))).into(placeImg);
            }

            @Override
            public void unbindView(MainActivity.ListItem item) {
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
