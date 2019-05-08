package com.example.operator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewAdapter extends  BaseAdapter{



        Context ctx;
        LayoutInflater lInflater;
        ArrayList<Item> objects;


    NewAdapter(Context context, ArrayList<Item> products) {
            ctx = context;
            objects = products;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return objects.size();

        }

        @Override
        public Object getItem(int position) {

            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                view = lInflater.inflate(R.layout.item, parent, false);
            }


            Item p = getActiveItem(position);

            ((TextView) view.findViewById(R.id.item)).setText(p.str);




            return view;
        }

        Item getActiveItem(int position) {
            return ((Item) getItem(position));
        }


}
