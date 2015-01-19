package com.imagesearch.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewImageAdapter extends BaseAdapter {

    private MainActivity activity;
    public ArrayList<GoogleImage> listImages = new ArrayList<GoogleImage>();
    private static LayoutInflater inflater = null;

    public GridViewImageAdapter(MainActivity a) {
        activity = a;
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

    public void addResult(GoogleImage result) {
        listImages.add(result);
        notifyDataSetChanged();
    }

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
        Picasso.with(activity).load(imageUrl).into(imageView);

        return imageView;
    }
}
