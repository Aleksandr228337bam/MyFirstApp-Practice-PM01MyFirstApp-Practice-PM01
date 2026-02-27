package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskManager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";

    // Названия столбцов
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";

    // SQL запрос для создания таблицы
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d("DatabaseHelper", "Table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Добавление новой задачи
    public boolean addTask(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        return result != -1;
    }

    // Получение всех задач
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                null, null, null, null, null, COLUMN_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                taskList.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return taskList;
    }

    // НОВЫЙ МЕТОД: Получение задачи по ID
    public Task getTaskById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Task task = null;

        Cursor cursor = db.query(TABLE_NAME,
                null,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            task = new Task(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            );
            cursor.close();
        }
        db.close();
        return task;
    }

    // Обновление задачи
    public boolean updateTask(int id, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);

        int result = db.update(TABLE_NAME, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();

        return result > 0;
    }

    // Удаление задачи
    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();

        return result > 0;
    }

    // Поиск задач по названию или описанию
    public List<Task> searchTasks(String query) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_TITLE + " LIKE ? OR " + COLUMN_DESCRIPTION + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs,
                null, null, COLUMN_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                taskList.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return taskList;
    }

    // Получение количества задач
    public int getTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    // Удаление всех задач
    public boolean deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, null, null);
        db.close();
        return result > 0;
    }
}