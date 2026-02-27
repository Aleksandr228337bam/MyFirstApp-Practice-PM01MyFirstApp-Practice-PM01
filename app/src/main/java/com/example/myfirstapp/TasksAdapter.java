package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class TasksAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> tasks;
    private SharedPreferences prefs;

    public TasksAdapter(@NonNull Context context, @NonNull List<String> tasks) {
        super(context, android.R.layout.simple_list_item_1, tasks);
        this.context = context;
        this.tasks = tasks;
        this.prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);

        // Устанавливаем текст
        textView.setText(tasks.get(position));

        // Применяем размер шрифта из настроек
        int fontSize = prefs.getInt("font_size", 16);
        textView.setTextSize(fontSize);

        // Применяем цвета в зависимости от темы
        boolean isDark = prefs.getBoolean("dark_theme", false);
        if (isDark) {
            textView.setTextColor(context.getResources().getColor(android.R.color.white));
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        } else {
            textView.setTextColor(context.getResources().getColor(android.R.color.black));
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        return convertView;
    }
}