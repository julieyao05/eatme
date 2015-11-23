package com.example.jarvus.tummybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Minh on 11/22/15.
 */
public class ItemAdapter extends BaseAdapter {
    private ArrayList<Object> itemArray;
    private LayoutInflater inflater;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_DIVIDER = 1;

    public ItemAdapter(Context context, ArrayList<Object> itemArray) {
        this.itemArray = itemArray;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return itemArray.get(position);
    }

    @Override
    public int getViewTypeCount() {
        // TYPE_PERSON and TYPE_DIVIDER
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Item) {
            return TYPE_ITEM;
        }

        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_ITEM);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_ITEM:
                    convertView = inflater.inflate(R.layout.row_item, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.row_header, parent, false);
                    break;
            }
        }

        switch (type) {
            case TYPE_ITEM:
                Item item = (Item)getItem(position);
                TextView name = (TextView)convertView.findViewById(R.id.nameLabel);
                TextView price = (TextView)convertView.findViewById(R.id.priceLabel);
                name.setText(item.getName());
                price.setText(item.getPrice());
                break;
            case TYPE_DIVIDER:
                TextView title = (TextView)convertView.findViewById(R.id.headerTitle);
                String titleString = (String)getItem(position);
                title.setText(titleString);
                break;
        }

        return convertView;
    }
}
