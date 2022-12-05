package com.negute.servicedirectory.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.negute.servicedirectory.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class IndexdMenuActivity extends AppCompatActivity {

    public static final String TAG = IndexdMenuActivity.class.getSimpleName();
    public static final String ID_EXTRA = "ID_EXTRA";
    private String id;
    private Type menuItemModelType = new TypeToken<Collection<MenuItemModel>>(){}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (!getIntent().hasExtra(ID_EXTRA)){
            throw new IllegalArgumentException();
        }

        id = getIntent().getStringExtra(ID_EXTRA);

        //ItemAdapter<MenuItemModel> itemItemAdapter = new ItemAdapter<>();
        //FastAdapter fastAdapter = FastAdapter.with(itemItemAdapter);

        //itemItemAdapter.add(loadMenu().menu);

        MenuAdapter menuAdapter = new MenuAdapter(loadMenu().menu);
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(menuAdapter);

        //setTitle(getString(R.string.menu));
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

        private List<MenuItemModel> menuItemModels;

        public MenuAdapter(List<MenuItemModel> menuItemModels) {
            this.menuItemModels = menuItemModels;
        }

        @Override
        public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuViewHolder(LayoutInflater.from(IndexdMenuActivity.this).inflate(R.layout.list_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(MenuViewHolder holder, int position) {
            holder.bindView(menuItemModels.get(position));
        }

        @Override
        public int getItemCount() {
            return menuItemModels.size();
        }
    }

    private class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        ImageView headerImg;
        FrameLayout headerContainer;
        TextView sectionName;

        public MenuViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.place);
            description = itemView.findViewById(R.id.description);
            headerContainer = itemView.findViewById(R.id.header);
            headerImg = itemView.findViewById(R.id.headerImg);
            sectionName = itemView.findViewById(R.id.headerTitle);
        }

        public void bindView(MenuItemModel item) {
            name.setText(item.title.trim());
            description.setText(item.price.trim());

            if (item.isSection()){
                headerContainer.setVisibility(View.VISIBLE);

                if (item.section_pic != null && !item.section_pic.isEmpty()){
                    File sectionPic = new File(getPlaceDir(), item.section_pic);
                    Glide.with(getApplicationContext()).load(Uri.fromFile(sectionPic)).into(headerImg);
                }

                sectionName.setText(item.section_name);
            } else {
                headerContainer.setVisibility(View.GONE);
            }
        }
    }


    @Keep
    public class MenuItemModel extends AbstractItem<MenuItemModel, MenuItemModel.ViewHolder >{

        String title;
        String price;
        String section_name;
        String section_pic;

        public boolean isSection() {
            return section_name != null && !section_name.isEmpty();
        }

        @Override
        public String toString() {
            return title + " --> " + price;
        }

        @NonNull
        @Override
        public ViewHolder getViewHolder(View v) {
            return new ViewHolder(v);
        }

        @Override
        public int getType() {
            return R.id.fastadapter_item_adapter;
        }

        @Override
        public int getLayoutRes() {
            return R.layout.list_item_layout;
        }

        protected class ViewHolder extends FastAdapter.ViewHolder<MenuItemModel> {
            TextView name;
            TextView description;
            ImageView headerImg;
            FrameLayout headerContainer;
            TextView sectionName;

            public ViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.place);
                description = itemView.findViewById(R.id.description);
                headerContainer = itemView.findViewById(R.id.header);
                headerImg = itemView.findViewById(R.id.headerImg);
                sectionName = itemView.findViewById(R.id.headerTitle);
            }


            @Override
            public void bindView(MenuItemModel item, List<Object> payloads) {
                name.setText(item.title.trim());
                description.setText(item.price.trim());

                Log.d(TAG, String.format("bindView: section pic %s", item.section_pic));

                if (item.isSection()){
                    headerContainer.setVisibility(View.VISIBLE);

                    if (item.section_pic != null && !item.section_pic.isEmpty()){
                        File sectionPic = new File(getPlaceDir(), item.section_pic);
                        Glide.with(getApplicationContext()).load(Uri.fromFile(sectionPic)).into(headerImg);
                    }

                    sectionName.setText(item.section_name);
                } else {
                    headerContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void unbindView(MenuItemModel item) {
                name.setText(null);
                description.setText(null);
            }

        }
    }

    private MenuModel loadMenu(){
        Gson gson = new Gson();
        File menuFile = new File(getPlaceDir(), Constants.MENU_FILE_NAME);

        try {
            JsonReader jsonReader = gson.newJsonReader(new FileReader(menuFile));
            return gson.fromJson(jsonReader, MenuModel.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private File getPlaceDir(){
        File picsDir = new File(getFilesDir(), Constants.PICS_DIR);
        return new File(picsDir, id);
    }
}
