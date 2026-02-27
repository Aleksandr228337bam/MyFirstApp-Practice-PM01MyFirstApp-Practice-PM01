package com.example.myfirstapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayAdapter<String> adapter;
    private List<String> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = findViewById(R.id.gridView);  // ID должен совпадать

        // Добавляем тестовые данные
        for (int i = 0; i < 20; i++) {
            allItems.add("Изображение " + (i + 1));
        }

        // Используем простой ArrayAdapter
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                allItems
        );

        gridView.setAdapter(adapter);

        // Обработчик клика
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = allItems.get(position);
            Toast.makeText(this, "Выбрано: " + selected, Toast.LENGTH_SHORT).show();
        });
    }
}