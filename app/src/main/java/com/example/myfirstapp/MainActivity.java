package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ListView listViewTasks;
    ArrayAdapter<String> adapter;
    List<Task> tasks = new ArrayList<>();
    int selectedTaskId = -1;

    EditText etTitle, etDesc, etSearch;
    Button btnAdd, btnRefresh, btnDelete, btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etSearch = findViewById(R.id.etSearch);
        listViewTasks = findViewById(R.id.listViewTasks);

        btnAdd = findViewById(R.id.btnAdd);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnDelete = findViewById(R.id.btnDeleteSelected);
        btnEdit = findViewById(R.id.btnEdit);

        // Добавить задачу
        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            if (!title.isEmpty() && !desc.isEmpty()) {
                if (dbHelper.addTask(title, desc)) {
                    Toast.makeText(this, "Задача добавлена", Toast.LENGTH_SHORT).show();
                    refreshList();
                    etTitle.setText("");
                    etDesc.setText("");
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        // Обновить список
        btnRefresh.setOnClickListener(v -> refreshList());

        // Выбор задачи из списка
        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            selectedTaskId = tasks.get(position).getId();
            etTitle.setText(tasks.get(position).getTitle());
            etDesc.setText(tasks.get(position).getDescription());
            Toast.makeText(this, "Выбрана задача ID: " + selectedTaskId, Toast.LENGTH_SHORT).show();
        });

        // Удалить выбранную
        btnDelete.setOnClickListener(v -> {
            if (selectedTaskId != -1) {
                if (dbHelper.deleteTask(selectedTaskId)) {
                    Toast.makeText(this, "Задача удалена", Toast.LENGTH_SHORT).show();
                    refreshList();
                    etTitle.setText("");
                    etDesc.setText("");
                    selectedTaskId = -1;
                }
            } else {
                Toast.makeText(this, "Выберите задачу", Toast.LENGTH_SHORT).show();
            }
        });

        // Редактировать выбранную (самостоятельное задание)
        btnEdit.setOnClickListener(v -> {
            if (selectedTaskId != -1) {
                String title = etTitle.getText().toString().trim();
                String desc = etDesc.getText().toString().trim();
                if (!title.isEmpty() && !desc.isEmpty()) {
                    Task task = new Task();
                    task.setId(selectedTaskId);
                    task.setTitle(title);
                    task.setDescription(desc);
                    if (dbHelper.updateTask(task)) {
                        Toast.makeText(this, "Задача обновлена", Toast.LENGTH_SHORT).show();
                        refreshList();
                        etTitle.setText("");
                        etDesc.setText("");
                        selectedTaskId = -1;
                    }
                } else {
                    Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Выберите задачу", Toast.LENGTH_SHORT).show();
            }
        });

        // Поиск (самостоятельное задание)
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTasks(s.toString());
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        refreshList();
    }

    private void refreshList() {
        tasks.clear();
        tasks.addAll(dbHelper.getAllTasks());
        updateAdapter();
    }

    private void searchTasks(String query) {
        if (query.isEmpty()) {
            refreshList();
        } else {
            tasks.clear();
            tasks.addAll(dbHelper.searchTasks(query));
            updateAdapter();
        }
    }

    private void updateAdapter() {
        List<String> taskTitles = new ArrayList<>();
        for (Task task : tasks) {
            taskTitles.add(task.getTitle() + " (" + task.getDescription() + ")");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskTitles);
        listViewTasks.setAdapter(adapter);
    }
}