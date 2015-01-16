package com.imagesearch.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by aimango on 15-01-16.
 */
public class GridViewImageAdapter extends BaseAdapter {

    private MainActivity activity;
    public ArrayList<GoogleImage> listImages;
    private static LayoutInflater inflater = null;

    public GridViewImageAdapter(MainActivity a, ArrayList<GoogleImage> listImages) {
        activity = a;
        this.listImages = listImages;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public Object getItem(int i) {
        return listImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(12)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, 300));
        } else {
            imageView = (ImageView) convertView;
            imageView.setImageResource(android.R.color.transparent);
        }

        String imageUrl = listImages.get(position).getThumbUrl();
        imageView.setImageBitmap(activity.imageCache.get(imageUrl));

        return imageView;
    }
}
