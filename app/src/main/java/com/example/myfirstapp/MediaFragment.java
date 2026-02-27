package com.example.myfirstapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MediaFragment extends Fragment {

    private ListView listViewMedia;
    private ArrayAdapter<String> mediaAdapter;
    private List<MediaItem> mediaFiles = new ArrayList<>();

    // Для медиаплеера
    private MediaPlayer mediaPlayer;
    private Button btnPlay, btnPause, btnStop;
    private SeekBar seekBar;
    private TextView tvCurrentTime, tvTotalTime;
    private Handler handler = new Handler();
    private int currentPosition = -1;
    private boolean isPlaying = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_media, container, false);

        listViewMedia = view.findViewById(R.id.listViewMedia);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnPause = view.findViewById(R.id.btnPause);
        btnStop = view.findViewById(R.id.btnStop);
        seekBar = view.findViewById(R.id.seekBar);
        tvCurrentTime = view.findViewById(R.id.tvCurrentTime);
        tvTotalTime = view.findViewById(R.id.tvTotalTime);

        // Загружаем тестовые медиафайлы
        loadTestMediaFiles();

        // Создаем список названий для адаптера
        List<String> mediaTitles = new ArrayList<>();
        for (MediaItem item : mediaFiles) {
            mediaTitles.add(item.getTitle());
        }

        mediaAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, mediaTitles);
        listViewMedia.setAdapter(mediaAdapter);

        // Обработчик выбора медиафайла
        listViewMedia.setOnItemClickListener((parent, v, position, id) -> {
            currentPosition = position;
            MediaItem mediaItem = mediaFiles.get(position);
            playMedia(mediaItem);
        });

        // Кнопки управления
        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                isPlaying = true;
                updateSeekBar();
                Toast.makeText(requireContext(), "Воспроизведение", Toast.LENGTH_SHORT).show();
            } else if (currentPosition == -1) {
                Toast.makeText(requireContext(), "Выберите файл", Toast.LENGTH_SHORT).show();
            }
        });

        btnPause.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPlaying = false;
                Toast.makeText(requireContext(), "Пауза", Toast.LENGTH_SHORT).show();
            }
        });

        btnStop.setOnClickListener(v -> {
            stopPlayback();
        });

        // Ползунок для перемотки
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return view;
    }

    private void loadTestMediaFiles() {
        // Создаем тестовые медиафайлы (в реальном приложении здесь будет сканирование устройства)
        mediaFiles.add(new MediaItem("Песня 1 - Пример.mp3", "audio", 180000)); // 3 минуты
        mediaFiles.add(new MediaItem("Песня 2 - Пример.mp3", "audio", 240000)); // 4 минуты
        mediaFiles.add(new MediaItem("Подкаст 1.mp3", "audio", 360000)); // 6 минут
        mediaFiles.add(new MediaItem("Аудиокнига.mp3", "audio", 600000)); // 10 минут
        mediaFiles.add(new MediaItem("Запись голоса.mp3", "audio", 45000)); // 45 секунд
    }

    private void playMedia(MediaItem mediaItem) {
        try {
            // Останавливаем предыдущее воспроизведение
            stopPlayback();

            // Создаем новый MediaPlayer
            mediaPlayer = new MediaPlayer();

            // В реальном приложении здесь должен быть реальный URI файла
            // Для демонстрации используем тестовый тон
            // mediaPlayer.setDataSource(realPath);

            // Подготавливаем плеер (в демо режиме просто имитируем)
            simulatePlayback(mediaItem);

            Toast.makeText(requireContext(),
                    "Воспроизведение: " + mediaItem.getTitle(),
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "Ошибка: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void simulatePlayback(MediaItem mediaItem) {
        // Имитация воспроизведения для демонстрации
        int duration = mediaItem.getDuration();
        seekBar.setMax(duration);
        tvTotalTime.setText(formatTime(duration));

        isPlaying = true;

        handler.post(new Runnable() {
            int currentProgress = 0;

            @Override
            public void run() {
                if (isPlaying && currentProgress <= duration) {
                    seekBar.setProgress(currentProgress);
                    tvCurrentTime.setText(formatTime(currentProgress));
                    currentProgress += 1000; // +1 секунда

                    handler.postDelayed(this, 1000);
                } else if (currentProgress > duration) {
                    // Конец воспроизведения
                    stopPlayback();
                    Toast.makeText(requireContext(), "Воспроизведение завершено", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception e) {
                // Игнорируем ошибки при остановке
            }
            mediaPlayer = null;
        }

        isPlaying = false;
        handler.removeCallbacksAndMessages(null);
        seekBar.setProgress(0);
        tvCurrentTime.setText("00:00");
        tvTotalTime.setText("00:00");
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && isPlaying) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        tvCurrentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                        handler.postDelayed(this, 1000);
                    }
                }
            });
        }
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayback();
    }

    // Внутренний класс для хранения информации о медиафайле
    private class MediaItem {
        private String title;
        private String type;
        private int duration; // в миллисекундах

        public MediaItem(String title, String type, int duration) {
            this.title = title;
            this.type = type;
            this.duration = duration;
        }

        public String getTitle() { return title; }
        public String getType() { return type; }
        public int getDuration() { return duration; }
    }
}