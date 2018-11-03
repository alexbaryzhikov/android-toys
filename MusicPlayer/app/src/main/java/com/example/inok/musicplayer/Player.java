package com.example.inok.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Exposes the functionality of the {@link MediaPlayer} and implements {@link PlayerAdapter}
 * so that {@link MainActivity} can control music playback.
 */

final class Player implements PlayerAdapter {

    private static final int PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 500;
    private static Player player = null;
    private MediaPlayer mMediaPlayer;
    private int mResId;
    private PlaybackInfoListener mPlaybackInfoListener;
    private ScheduledExecutorService mExecutor;
    private Runnable mSeekbarPositionUpdateTask;
    private @PlaybackInfoListener.State int state;

    private Player() {}

    public static Player create() {
        player = new Player();
        player.state = PlaybackInfoListener.State.INVALID;
        return player;
    }

    public static Player getPlayer() {
        return player;
    }

    public int getState() {
        return state;
    }

    void setPlaybackInfoListener(PlaybackInfoListener mPlaybackInfoListener) {
        this.mPlaybackInfoListener = mPlaybackInfoListener;
    }

    @Override
    public void loadMedia(Context context, int resId) {
        mResId = resId;
        player.state = PlaybackInfoListener.State.STOPPED;
        initMediaPlayer(context);
        primeSeekbar();
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            logToUi("Release MediaPlayer");
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public void play() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            logToUi("Start playback");
            mMediaPlayer.start();
            player.state = PlaybackInfoListener.State.PLAYING;
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PLAYING);
            }
            startUpdatingPosition();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            logToUi("Pause playback");
            mMediaPlayer.pause();
            player.state = PlaybackInfoListener.State.PAUSED;
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PAUSED);
            }
        }
    }

    @Override
    public void stop(Context context) {
        if (mMediaPlayer != null) {
            logToUi("Stop playback");
            mMediaPlayer.reset();
            loadMedia(context, mResId);
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.STOPPED);
            }
        }
    }

    @Override
    public void primeSeekbar() {
        final int duration = mMediaPlayer.getDuration();
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onDurationChanged(duration);
            logToUi(String.format("Playback duration set to %dms", duration));
            mPlaybackInfoListener.onPositionChanged(0);
            logToUi("Playback position set to 0ms");
        }
    }

    @Override
    public void syncSeekbar() {
        final int duration = mMediaPlayer.getDuration();
        final int position = mMediaPlayer.getCurrentPosition();
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onDurationChanged(duration);
            mPlaybackInfoListener.onPositionChanged(position);
        }
    }

    @Override
    public void seekTo(int position) {
        if (mMediaPlayer != null) {
            logToUi(String.format("Seek to position %dms", position));
            mMediaPlayer.seekTo(position);
        }
    }

    /**
     * MediaPlayer is initialized during load() call rather than constructor.
     */
    private void initMediaPlayer(Context context) {
        release();
        logToUi("Create MediaPlayer");
        mMediaPlayer = MediaPlayer.create(context, mResId);
        logToUi(String.format("Data source set to %s",
                context.getResources().getResourceEntryName(mResId)));
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopUpdatingPosition();
                logToUi("Playback completed");
                player.state = PlaybackInfoListener.State.COMPLETED;
                if (mPlaybackInfoListener != null) {
                    mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.COMPLETED);
                    mPlaybackInfoListener.onPlaybackCompleted();
                }
            }
        });
    }

    /**
     * Sync the MediaPlayer position with seekbar via recurring task.
     */
    private void startUpdatingPosition() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    updatePosition();
                }
            };
        }
        mExecutor.scheduleAtFixedRate(mSeekbarPositionUpdateTask, 0,
                PLAYBACK_POSITION_REFRESH_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Update seekbar position.
     */
    private void updatePosition() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            int currentPosition = mMediaPlayer.getCurrentPosition();
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onPositionChanged(currentPosition);
            }
        }
    }

    /**
     * Stop synchronizing MeadiaPlayer with seekbar.
     */
    private void stopUpdatingPosition() {
        if (mExecutor != null) {
            mExecutor.shutdownNow();
            mExecutor = null;
            mSeekbarPositionUpdateTask = null;
        }
    }


    /**
     * Print message to UI log.
     */
    private void logToUi(String message) {
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onLogUpdated(message);
        }
    }
}
