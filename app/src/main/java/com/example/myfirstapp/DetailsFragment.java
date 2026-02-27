package com.example.myfirstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class DetailsFragment extends Fragment {

    private TextView tvTaskId, tvTaskTitle, tvTaskDesc;
    private Button btnBack, btnDelete, btnEdit;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Инициализация
        tvTaskId = view.findViewById(R.id.tvTaskId);
        tvTaskTitle = view.findViewById(R.id.tvTaskTitle);
        tvTaskDesc = view.findViewById(R.id.tvTaskDesc);
        btnBack = view.findViewById(R.id.btnBack);

        // Если есть кнопки удаления/редактирования
        btnDelete = view.findViewById(R.id.btnDelete);
        btnEdit = view.findViewById(R.id.btnEdit);

        dbHelper = new DatabaseHelper(requireContext());

        // Получаем ID задачи из аргументов
        if (getArguments() != null) {
            int taskId = getArguments().getInt("TASK_ID", -1);
            loadTaskDetails(taskId);
        } else {
            Toast.makeText(requireContext(), "Ошибка: ID задачи не передан", Toast.LENGTH_SHORT).show();
        }

        // Кнопка "Назад"
        btnBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        // Если есть кнопка удаления
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                // Здесь можно добавить логику удаления
                Toast.makeText(requireContext(), "Удаление задачи", Toast.LENGTH_SHORT).show();
            });
        }

        // Если есть кнопка редактирования
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                // Здесь можно добавить логику редактирования
                Toast.makeText(requireContext(), "Редактирование задачи", Toast.LENGTH_SHORT).show();
            });
        }

        return view;
    }

    private void loadTaskDetails(int taskId) {
        try {
            Task task = dbHelper.getTaskById(taskId);

            if (task != null) {
                tvTaskId.setText("ID: " + task.getId());
                tvTaskTitle.setText("Название: " + task.getTitle());
                tvTaskDesc.setText("Описание: " + task.getDescription());
            } else {
                Toast.makeText(requireContext(),
                        "Задача с ID " + taskId + " не найдена",
                        Toast.LENGTH_SHORT).show();

                // Показываем заглушку
                tvTaskId.setText("ID: " + taskId);
                tvTaskTitle.setText("Название: Задача не найдена");
                tvTaskDesc.setText("Описание: -");
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "Ошибка загрузки задачи: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}