package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private Context context;
    private List<String> items;
    private LayoutInflater inflater;

    // ИСПРАВЛЕННЫЙ КОНСТРУКТОР
    public GalleryAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gallery, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.ivGalleryImage);
            holder.textView = convertView.findViewById(R.id.tvImageName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Устанавливаем данные
        String item = items.get(position);
        holder.textView.setText(item);

        // Устанавливаем изображение (можно использовать разные иконки)
        int imageResId = android.R.drawable.ic_menu_gallery;
        switch (position % 5) {
            case 0: imageResId = android.R.drawable.ic_menu_gallery; break;
            case 1: imageResId = android.R.drawable.ic_menu_camera; break;
            case 2: imageResId = android.R.drawable.ic_menu_myplaces; break;
            case 3: imageResId = android.R.drawable.ic_menu_share; break;
            case 4: imageResId = android.R.drawable.ic_menu_help; break;
        }
        holder.imageView.setImageResource(imageResId);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}