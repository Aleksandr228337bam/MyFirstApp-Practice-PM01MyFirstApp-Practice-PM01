package com.example.myfirstapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MediaActivity extends AppCompatActivity {

    private ListView lvAudio;
    private MediaPlayer mediaPlayer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private VideoView videoView;
    private TextView tvCurrentTrack;
    private Button btnPlay, btnPause, btnStop;
    private ProgressBar progressBar;

    private int currentTrackResId = -1;
    private int currentTrackPosition = -1;
    private boolean isPaused = false;

    // Массивы ресурсов и названий треков
    int[] audioRes = {R.raw.audio1, R.raw.audio2};
    String[] audioTitles = {"Трек 1", "Трек 2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_media);
            Toast.makeText(this, "MediaActivity загружена", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка загрузки layout: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            // Инициализация view
            lvAudio = findViewById(R.id.lvAudio);
            swipeRefreshLayout = findViewById(R.id.swipeRefresh);
            videoView = findViewById(R.id.videoView);
            tvCurrentTrack = findViewById(R.id.tvCurrentTrack);
            btnPlay = findViewById(R.id.btnPlay);
            btnPause = findViewById(R.id.btnPause);
            btnStop = findViewById(R.id.btnStop);
            progressBar = findViewById(R.id.progressBar);

            // Проверка, что все view найдены
            if (lvAudio == null) Toast.makeText(this, "lvAudio не найден", Toast.LENGTH_SHORT).show();
            if (videoView == null) Toast.makeText(this, "videoView не найден", Toast.LENGTH_SHORT).show();

            // Список аудио
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    audioTitles
            );
            lvAudio.setAdapter(adapter);

            lvAudio.setOnItemClickListener((parent, view, position, id) -> {
                playAudio(audioRes[position], position);
            });

            // Swipe-to-refresh
            swipeRefreshLayout.setOnRefreshListener(() -> {
                stopAudio();
                initVideo();
                tvCurrentTrack.setText("Нет трека");
                Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            });

            // Кнопки управления
            btnPlay.setOnClickListener(v -> {
                if (currentTrackResId != -1) {
                    if (isPaused && mediaPlayer != null) {
                        mediaPlayer.start();
                        isPaused = false;
                        Toast.makeText(this, "Возобновлено", Toast.LENGTH_SHORT).show();
                    } else if (currentTrackResId != -1) {
                        playAudio(currentTrackResId, currentTrackPosition);
                    }
                } else {
                    Toast.makeText(this, "Выберите трек", Toast.LENGTH_SHORT).show();
                }
            });

            btnPause.setOnClickListener(v -> {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPaused = true;
                    Toast.makeText(this, "Пауза", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Нет активного трека", Toast.LENGTH_SHORT).show();
                }
            });

            btnStop.setOnClickListener(v -> {
                stopAudio();
                tvCurrentTrack.setText("Нет трека");
                currentTrackResId = -1;
                currentTrackPosition = -1;
                Toast.makeText(this, "Остановлено", Toast.LENGTH_SHORT).show();
            });

            initVideo();

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка инициализации: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void playAudio(int resId, int position) {
        try {
            stopAudio();

            mediaPlayer = MediaPlayer.create(this, resId);
            if (mediaPlayer == null) {
                Toast.makeText(this, "Не удалось создать MediaPlayer", Toast.LENGTH_SHORT).show();
                return;
            }

            mediaPlayer.start();

            currentTrackResId = resId;
            currentTrackPosition = position;
            isPaused = false;

            tvCurrentTrack.setText("Сейчас играет: " + audioTitles[position]);
            Toast.makeText(this, "Воспроизведение: " + audioTitles[position], Toast.LENGTH_SHORT).show();

            mediaPlayer.setOnCompletionListener(mp -> {
                tvCurrentTrack.setText("Трек завершен");
                currentTrackResId = -1;
                currentTrackPosition = -1;
            });

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка воспроизведения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVideo() {
        try {
            progressBar.setVisibility(View.VISIBLE);

            // Проверяем, есть ли видеофайл
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample);
            videoView.setVideoURI(uri);

            videoView.setOnPreparedListener(mp -> {
                progressBar.setVisibility(View.GONE);
                videoView.start();
                Toast.makeText(this, "Видео загружено", Toast.LENGTH_SHORT).show();
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Ошибка видео: проверьте файл sample.mp4", Toast.LENGTH_LONG).show();
                return true;
            });

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Ошибка видео: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudio();
    }
}