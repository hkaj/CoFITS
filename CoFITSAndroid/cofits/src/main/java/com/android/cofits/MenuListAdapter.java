package com.android.cofits;

/**
 * Created by Sanaa on 6/9/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAdapter extends BaseAdapter {
    private Context context;
    private String[] mTitle;
    private int[] mIcon;

    public MenuListAdapter(Context pContext, String[] pTitle, int[] pIcon) {
        context = pContext;
        mTitle = pTitle;
        mIcon = pIcon;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_drawer_list, parent, false);

        TextView txtTitle = (TextView) itemView.findViewById(R.id.title);
        ImageView imgIcon = (ImageView) itemView.findViewById(R.id.icon);

        txtTitle.setText(mTitle[position]);
        imgIcon.setImageResource(mIcon[position]);

        return itemView;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitle[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}