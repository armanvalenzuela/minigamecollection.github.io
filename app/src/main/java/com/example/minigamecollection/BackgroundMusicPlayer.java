package com.example.minigamecollection;

import android.content.Context;
import android.media.MediaPlayer;

public class BackgroundMusicPlayer {

    private static MediaPlayer bgmPlayer;


    private BackgroundMusicPlayer() {}

    public static void startBackgroundMusic(Context context) {
        if (bgmPlayer == null) {
            bgmPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.accumulate_town);
            bgmPlayer.setLooping(true);
            bgmPlayer.start();
        } else if (!bgmPlayer.isPlaying()) {
            bgmPlayer.start();
        }
    }

    public static void pauseBackgroundMusic() {
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause();
        }
    }

    public static void stopBackgroundMusic() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.release();
            bgmPlayer = null;
        }
    }
    public static void resetBackgroundMusic(Context context) {
        stopBackgroundMusic();  // Stop and release the previous instance
        startBackgroundMusic(context);  // Start the music from the beginning
    }
}
