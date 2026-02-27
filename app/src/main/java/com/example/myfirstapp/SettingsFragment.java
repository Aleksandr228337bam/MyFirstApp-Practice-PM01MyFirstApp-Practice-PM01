package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchTheme;
    private SeekBar seekFont;
    private TextView tvPreview;
    private Button btnResetSettings;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchTheme = view.findViewById(R.id.switchTheme);
        seekFont = view.findViewById(R.id.seekFont);
        tvPreview = view.findViewById(R.id.tvPreview);
        btnResetSettings = view.findViewById(R.id.btnResetSettings);

        prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        // Загружаем настройки
        loadSettings();

        // Обработчик переключения темы
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_theme", isChecked).apply();

            // Применяем тему немедленно
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            Toast.makeText(requireContext(),
                    "Тема изменена. Перезапустите приложение для полного применения",
                    Toast.LENGTH_SHORT).show();
        });

        // Обработчик изменения размера шрифта
        seekFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPreview.setTextSize(progress);
                tvPreview.setText("Пример текста (размер " + progress + "sp)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int fontSize = seekBar.getProgress();
                prefs.edit().putInt("font_size", fontSize).apply();
                Toast.makeText(requireContext(),
                        "Размер шрифта сохранён: " + fontSize,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка сброса настроек
        btnResetSettings.setOnClickListener(v -> resetSettings());

        return view;
    }

    private void loadSettings() {
        boolean isDark = prefs.getBoolean("dark_theme", false);
        int fontSize = prefs.getInt("font_size", 16);

        switchTheme.setChecked(isDark);
        seekFont.setProgress(fontSize);
        tvPreview.setTextSize(fontSize);
        tvPreview.setText("Пример текста (размер " + fontSize + "sp)");
    }

    private void resetSettings() {
        prefs.edit()
                .putBoolean("dark_theme", false)
                .putInt("font_size", 16)
                .apply();

        loadSettings();

        // Применяем светлую тему
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toast.makeText(requireContext(), "Настройки сброшены", Toast.LENGTH_SHORT).show();
    }
}