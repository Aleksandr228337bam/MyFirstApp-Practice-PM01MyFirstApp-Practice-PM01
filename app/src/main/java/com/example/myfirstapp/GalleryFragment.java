package com.example.myfirstapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GridView gridViewGallery;
    private GalleryAdapter galleryAdapter;
    private List<String> imagePaths = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        gridViewGallery = view.findViewById(R.id.gridViewGallery);

        // Загружаем изображения из галереи
        loadImagesFromGallery();

        // Используем кастомный адаптер для отображения изображений
        galleryAdapter = new GalleryAdapter(imagePaths);
        gridViewGallery.setAdapter(galleryAdapter);

        // Обработчик клика по изображению
        gridViewGallery.setOnItemClickListener((parent, v, position, id) -> {
            String imagePath = imagePaths.get(position);
            Toast.makeText(requireContext(),
                    "Выбрано изображение: " + imagePath,
                    Toast.LENGTH_SHORT).show();

            // Здесь можно открыть изображение на весь экран
            openFullImage(imagePath);
        });

        return view;
    }

    private void loadImagesFromGallery() {
        imagePaths.clear();

        // Проверяем разрешения
        if (!checkPermissions()) {
            requestPermissions();
            return;
        }

        // Получаем все изображения из галереи
        String[] projection = {MediaStore.Images.Media.DATA};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = requireContext().getContentResolver()
                .query(uri, projection, null, null, orderBy)) {

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                do {
                    String imagePath = cursor.getString(columnIndex);
                    imagePaths.add(imagePath);
                } while (cursor.moveToNext());

                Toast.makeText(requireContext(),
                        "Загружено изображений: " + imagePaths.size(),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Если нет изображений, добавляем тестовые
                addTestImages();
            }
        } catch (SecurityException e) {
            Toast.makeText(requireContext(),
                    "Нет разрешения на чтение галереи",
                    Toast.LENGTH_SHORT).show();
            addTestImages();
        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "Ошибка загрузки: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            addTestImages();
        }

        if (galleryAdapter != null) {
            galleryAdapter.notifyDataSetChanged();
        }
    }

    private void addTestImages() {
        // Добавляем тестовые изображения для демонстрации с существующими ресурсами Android
        imagePaths.add("test_image_1");
        imagePaths.add("test_image_2");
        imagePaths.add("test_image_3");
        imagePaths.add("test_image_4");
        imagePaths.add("test_image_5");
    }

    private void openFullImage(String imagePath) {
        // Здесь можно реализовать открытие на весь экран
        Toast.makeText(requireContext(), "Открытие изображения...", Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.READ_MEDIA_IMAGES) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 100);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            loadImagesFromGallery();
        } else {
            Toast.makeText(requireContext(), "Разрешение не получено, показываем тестовые изображения", Toast.LENGTH_SHORT).show();
            addTestImages();
            if (galleryAdapter != null) {
                galleryAdapter.notifyDataSetChanged();
            }
        }
    }

    // Кастомный адаптер для отображения изображений
    private class GalleryAdapter extends BaseAdapter {
        private List<String> images;
        private LayoutInflater inflater;

        // Массив доступных ресурсов для тестовых изображений
        private int[] testImages = {
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_camera,
                android.R.drawable.ic_menu_myplaces,
                android.R.drawable.ic_menu_share,
                android.R.drawable.ic_menu_help
        };

        public GalleryAdapter(List<String> images) {
            this.images = images;
            this.inflater = LayoutInflater.from(requireContext());
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_gallery, parent, false);
                holder = new ViewHolder();
                holder.imageView = convertView.findViewById(R.id.ivGalleryImage);
                holder.textView = convertView.findViewById(R.id.tvImageName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String imagePath = images.get(position);

            try {
                if (imagePath.startsWith("test")) {
                    // Тестовое изображение - используем циклически доступные ресурсы
                    int imageResId = testImages[position % testImages.length];
                    holder.imageView.setImageResource(imageResId);
                    holder.textView.setText("Тестовое " + (position + 1));
                } else {
                    // Пытаемся загрузить реальное изображение
                    // Для реальной загрузки нужно использовать BitmapFactory
                    // Показываем иконку как заглушку
                    holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery);

                    // Показываем имя файла
                    String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                    if (fileName.length() > 15) {
                        fileName = fileName.substring(0, 12) + "...";
                    }
                    holder.textView.setText(fileName);
                }
            } catch (Exception e) {
                holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                holder.textView.setText("Image " + position);
            }

            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }
}