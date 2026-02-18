package com.example.myfirstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.utils.MathUtils;

public class CalcActivity extends AppCompatActivity {

    private EditText etNumber1, etNumber2;
    private Button btnCalculate;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        // Инициализация view
        etNumber1 = findViewById(R.id.etNumber1);
        etNumber2 = findViewById(R.id.etNumber2);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);

        btnCalculate.setOnClickListener(v -> calculateSum());
    }

    private void calculateSum() {
        String n1Str = etNumber1.getText().toString().trim();
        String n2Str = etNumber2.getText().toString().trim();

        // Проверка на пустые поля
        if (n1Str.isEmpty() || n2Str.isEmpty()) {
            Toast.makeText(this, "Введите оба числа", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Пробуем преобразовать в числа
            int n1 = Integer.parseInt(n1Str);
            int n2 = Integer.parseInt(n2Str);

            // Вызываем метод из модуля utils
            int sum = MathUtils.add(n1, n2);

            // Показываем результат
            tvResult.setText("Результат: " + sum);

        } catch (NumberFormatException e) {
            // Если введены не числа (буквы, символы)
            Toast.makeText(this, "Введите корректные числа", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}