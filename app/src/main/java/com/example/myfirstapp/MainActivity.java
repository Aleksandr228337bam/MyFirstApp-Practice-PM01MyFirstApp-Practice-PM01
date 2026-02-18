package com.example.myfirstapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvScreens = findViewById(R.id.lvScreens);

        String[] screens = {
                "Открыть профиль",
                "Открыть экран с расчётом",
                "Открыть экран настроек"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                screens
        );

        lvScreens.setAdapter(adapter);

        lvScreens.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else if (position == 1) {
                startActivity(new Intent(MainActivity.this, CalcActivity.class));
            } else if (position == 2) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
            // Анимация перехода
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}