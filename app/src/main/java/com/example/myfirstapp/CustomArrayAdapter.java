package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Task> {

    private final Context context;
    private final List<Task> objects;
    private float fontSize;

    public CustomArrayAdapter(Context context, List<Task> objects, float fontSize) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.objects = objects;
        this.fontSize = fontSize;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task item = objects.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);

        // Применяем настройки
        textView.setTypeface(Typeface.DEFAULT_BOLD); // Жирный шрифт
        textView.setTextSize(fontSize); // Размер шрифта из настроек

        // Проверяем тему и устанавливаем цвет текста
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_theme", false);

        if (isDark) {
            textView.setTextColor(context.getResources().getColor(android.R.color.white));
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        } else {
            textView.setTextColor(context.getResources().getColor(android.R.color.black));
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        // Отображаем заголовок задачи
        textView.setText("#" + item.getId() + " " + item.getTitle());

        return convertView;
    }

    // Метод для обновления размера шрифта
    public void updateFontSize(float newFontSize) {
        this.fontSize = newFontSize;
        notifyDataSetChanged();
    }
}