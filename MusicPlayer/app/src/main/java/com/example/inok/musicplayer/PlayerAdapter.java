package com.example.inok.musicplayer;

import android.content.Context;

/**
 * Provides media playback control of {@link Player}.
 */
interface PlayerAdapter {

    void loadMedia(Context context, int resId);

    void release();

    boolean isPlaying();

    void play();

    void pause();

    void stop(Context context);

    void primeSeekbar();

    void syncSeekbar();

    void seekTo(int position);
}
