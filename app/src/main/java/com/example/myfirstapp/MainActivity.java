package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ListView listViewTasks;
    ArrayAdapter<String> tasksAdapter;
    List<Task> tasks = new ArrayList<>();
    int selectedTaskId = -1;

    // Для новой навигации
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Применяем тему перед установкой контента
        applyTheme();

        super.onCreate(savedInstanceState);

        // Проверяем, используем ли мы новую или старую навигацию
        boolean useNewNavigation = getSharedPreferences("app_settings", MODE_PRIVATE)
                .getBoolean("use_new_navigation", false);

        if (useNewNavigation) {
            // Новая навигация с фрагментами
            setupNewNavigation();
        } else {
            // Старая навигация с Activity
            setupOldNavigation();
        }
    }

    private void applyTheme() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_theme", false);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void setupNewNavigation() {
        try {
            setContentView(R.layout.activity_main_nav);

            // Инициализируем Toolbar
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Проверяем, что NavHostFragment существует
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);

            if (navHostFragment == null) {
                Toast.makeText(this, "Ошибка: NavHostFragment не найден", Toast.LENGTH_LONG).show();
                return;
            }

            navController = navHostFragment.getNavController();

            // Проверяем, что BottomNavigationView существует
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            if (bottomNavigationView == null) {
                Toast.makeText(this, "Ошибка: BottomNavigationView не найден", Toast.LENGTH_LONG).show();
                return;
            }

            // Связываем BottomNavigationView с NavController
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Связываем Toolbar с NavController (для отображения заголовков и кнопки "Назад")
            NavigationUI.setupActionBarWithNavController(this, navController);

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка инициализации: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupOldNavigation() {
        setContentView(R.layout.activity_main);

        // Инициализация старой навигации
        initOldNavigation();

        // ИНИЦИАЛИЗАЦИЯ SQLite
        dbHelper = new DatabaseHelper(this);
        listViewTasks = findViewById(R.id.listViewTasks);

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> addTask());

        findViewById(R.id.btnRefresh).setOnClickListener(v -> refreshTasksList());

        // НОВЫЙ ПОИСК
        EditText etSearch = findViewById(R.id.etSearch);
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                try {
                    tasks.clear();
                    tasks.addAll(dbHelper.searchTasks(query));
                    refreshTasksList();
                    Toast.makeText(this, "🔍 Найдено: " + tasks.size() + " задач", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "❌ Ошибка поиска: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                refreshTasksList();
            }
        });

        // Клик по задаче → DetailsActivity
        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTaskId = tasks.get(position).getId();
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("TASK_ID", selectedTaskId);
                startActivityForResult(intent, 1);

                view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                Toast.makeText(MainActivity.this, "📋 Детали задачи #" + selectedTaskId, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnDelete = findViewById(R.id.btnDeleteSelected);
        btnDelete.setOnClickListener(v -> {
            if (selectedTaskId != -1) {
                try {
                    if (dbHelper.deleteTask(selectedTaskId)) {
                        Toast.makeText(this, "🗑️ Задача #" + selectedTaskId + " удалена!", Toast.LENGTH_SHORT).show();
                        refreshTasksList();
                        selectedTaskId = -1;
                    } else {
                        Toast.makeText(this, "❌ Ошибка удаления из БД", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "💥 Ошибка БД: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "⚠️ Выберите задачу кликом!", Toast.LENGTH_SHORT).show();
            }
        });

        refreshTasksList();
    }

    private void initOldNavigation() {
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ListView lvScreens = findViewById(R.id.lvScreens);
        String[] screens = {
                "Открыть профиль",
                "Открыть экран с расчётом",
                "Открыть экран настроек",
                "Каталог картинок",
                "Медиа",
                "Переключиться на новую навигацию"
        };

        ArrayAdapter<String> navAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, screens);
        lvScreens.setAdapter(navAdapter);

        lvScreens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, CalcActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, MediaActivity.class));
                        break;
                    case 5:
                        // Переключение на новую навигацию
                        getSharedPreferences("app_settings", MODE_PRIVATE)
                                .edit()
                                .putBoolean("use_new_navigation", true)
                                .apply();
                        Toast.makeText(MainActivity.this,
                                "🔄 Перезапустите приложение для применения новой навигации",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    private void addTask() {
        try {
            EditText etTitle = findViewById(R.id.etTitle);
            EditText etDesc = findViewById(R.id.etDesc);

            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (title.length() < 3) {
                Toast.makeText(this, "⚠️ Название слишком короткое (минимум 3 символа)", Toast.LENGTH_SHORT).show();
                return;
            }
            if (desc.isEmpty()) {
                Toast.makeText(this, "⚠️ Добавьте описание!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addTask(title, desc)) {
                Toast.makeText(this, "✅ Задача '" + title + "' добавлена!", Toast.LENGTH_SHORT).show();
                refreshTasksList();
                etTitle.setText("");
                etDesc.setText("");
            } else {
                Toast.makeText(this, "❌ Не удалось сохранить в БД", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "💥 Критическая ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void refreshTasksList() {
        try {
            tasks.clear();
            tasks.addAll(dbHelper.getAllTasks());

            List<String> displayList = new ArrayList<>();
            for (Task task : tasks) {
                displayList.add("#" + task.getId() + " " + task.getTitle() + "\n " + task.getDescription());
            }

            tasksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, displayList);
            listViewTasks.setAdapter(tasksAdapter);
            tasksAdapter.notifyDataSetChanged();

            Toast.makeText(this, "📊 Всего задач: " + tasks.size(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "❌ Ошибка загрузки списка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            refreshTasksList();
            Toast.makeText(this, "🔄 Список обновлен!", Toast.LENGTH_SHORT).show();
        }
    }

    // Добавляем поддержку Up button в новой навигации
    @Override
    public boolean onSupportNavigateUp() {
        if (navController != null) {
            return navController.navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
}