package com.negute.servicedirectory.ui;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageViewerAdapter extends PagerAdapter {

    public static final String PNG_EXT = "PNG";
    public static final String JPG_EXT = "JPG";

    private File imageDir;
    private List<Uri> imageUris = new ArrayList<>();

    public ImageViewerAdapter(File imageDir) {
        this.imageDir = imageDir;

        File[] files = imageDir.listFiles();
        for (File file: files){
            if (!file.isDirectory()){
                Uri fileUri = Uri.fromFile(file);
                String ext = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
                if (ext.equalsIgnoreCase(PNG_EXT) || ext.equalsIgnoreCase(JPG_EXT)){
                    imageUris.add(fileUri);
                }
            }
        }
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        Glide.with(container.getContext()).load(imageUris.get(position)).into(photoView);
        container.addView(
                photoView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageUris.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}