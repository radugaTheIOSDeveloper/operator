package com.example.operator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EmptyAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ItemEmpty> objects;


    EmptyAdapter(Context context, ArrayList<ItemEmpty> products) {
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
            view = lInflater.inflate(R.layout.item_no, parent, false);
        }


        ItemEmpty p = getActiveItem(position);

        ((TextView) view.findViewById(R.id.item_no)).setText(p.str);




        return view;
    }

    ItemEmpty getActiveItem(int position) {
        return ((ItemEmpty) getItem(position));
    }

}
