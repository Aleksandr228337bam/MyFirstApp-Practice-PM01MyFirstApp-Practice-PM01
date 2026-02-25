package com.example.myfirstapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class GalleryDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);

        ImageView imageView = findViewById(R.id.ivDetailImage);
        TextView title = findViewById(R.id.tvDetailTitle);
        TextView description = findViewById(R.id.tvDetailDescription);

        String imageFile = getIntent().getStringExtra("IMAGE");
        String titleText = getIntent().getStringExtra("TITLE");
        String descText = getIntent().getStringExtra("DESC");

        title.setText(titleText);
        description.setText(descText);

        try {
            InputStream is = getAssets().open("images/" + imageFile);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}