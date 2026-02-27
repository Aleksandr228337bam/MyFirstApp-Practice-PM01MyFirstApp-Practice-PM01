package com.example.myfirstapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FullscreenImageActivity extends AppCompatActivity {

    private ImageView fullscreenImage;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        fullscreenImage = findViewById(R.id.fullscreenImage);

        // Получаем путь к изображению из Intent
        imagePath = getIntent().getStringExtra("image_path");

        // Загружаем изображение
        if (imagePath != null) {
            // Для реальных изображений используйте Glide или Picasso
            // Glide.with(this).load(imagePath).into(fullscreenImage);

            // Для демонстрации используем ресурс
            fullscreenImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Закрытие по клику
        fullscreenImage.setOnClickListener(v -> finish());
    }
}