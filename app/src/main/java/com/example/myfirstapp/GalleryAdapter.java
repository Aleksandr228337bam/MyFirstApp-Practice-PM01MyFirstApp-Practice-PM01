package com.example.myfirstapp;

import android.content.Intent;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private Context context;
    private List<GalleryItem> items;

    public GalleryAdapter(Context context, List<GalleryItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {

        GalleryItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, GalleryDetailsActivity.class);
            intent.putExtra("IMAGE", item.getImageFileName());
            intent.putExtra("TITLE", item.getTitle());
            intent.putExtra("DESC", item.getDescription());

            context.startActivity(intent);
        });

        try {
            InputStream is = context.getAssets()
                    .open("images/" + item.getImageFileName());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            holder.ivImage.setImageBitmap(bitmap);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDescription;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}