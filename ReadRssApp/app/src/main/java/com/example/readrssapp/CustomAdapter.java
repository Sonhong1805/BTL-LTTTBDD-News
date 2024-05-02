package com.example.readrssapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<News> {

    private ArrayList<News> listNews;
    private ArrayList<News> filteredList;

    public CustomAdapter(Context context, int resource, ArrayList<News> listNews) {
        super(context, resource, listNews);
        this.listNews = listNews;
        this.filteredList = new ArrayList<News>(listNews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.dong_layout_listview,null);

        }
        News p = getItem(position);
        if(p != null) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtViewTitle);
            txtTitle.setText(p.title);

            TextView textPubDate = (TextView) view.findViewById(R.id.txtViewPubDate);
            textPubDate.setText(p.pubDate);

            ImageView imageView = view.findViewById(R.id.imageView);
            Picasso.get().load(p.image).into(imageView);
        }
        return view;
    }

}
