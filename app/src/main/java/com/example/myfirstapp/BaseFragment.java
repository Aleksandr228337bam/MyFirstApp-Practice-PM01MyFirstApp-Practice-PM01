package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    protected void applyFontSizeToText(android.widget.TextView textView) {
        int fontSize = prefs.getInt("font_size", 16);
        textView.setTextSize(fontSize);
    }
}