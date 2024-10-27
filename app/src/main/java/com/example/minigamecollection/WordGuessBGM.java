package com.example.minigamecollection;

import android.content.Context;
import android.media.MediaPlayer;

public class WordGuessBGM {

    private static MediaPlayer bgmPlayer;


    private WordGuessBGM() {}

    public static void startMusic(Context context) {
        if (bgmPlayer == null) {
            bgmPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.wii_party);
            bgmPlayer.setLooping(true);
            bgmPlayer.start();
        } else if (!bgmPlayer.isPlaying()) {
            bgmPlayer.start();
        }
    }

    public static void pauseMusic() {
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause();
        }
    }

    public static void stopMusic() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.release();
            bgmPlayer = null;
        }
    }
}
