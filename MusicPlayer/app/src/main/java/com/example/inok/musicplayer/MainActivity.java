package com.example.inok.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int MEDIA_RES_ID = R.raw.abandoned_in_place;

    private PlayerAdapter mPlayerAdapter;
    private TextView mTextLog;
    private Button mButtonPlay;
    private ScrollView mScrollLog;
    private SeekBar mSeekbarAudio;
    private boolean mUserIsSeeking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        initSeekbar();
        Log.d(TAG, "onCreate: finished");
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* Initialize player */
        Player player = Player.getPlayer();
        if (player == null) {
            /* This is initial run, there's no player yet */
            player = Player.create();
            Log.d(TAG, "onStart: created Player");
            player.setPlaybackInfoListener(new PlaybackInfoListenerMain());
            Log.d(TAG, "onStart: Playback info callback set");
            player.loadMedia(this, MEDIA_RES_ID);
            Log.d(TAG, "onStart: created MediaPlayer");
        } else {
            /* We're reloading MainActivity */
            Log.d(TAG, "onStart: reclaimed Player");
            player.setPlaybackInfoListener(new PlaybackInfoListenerMain());
            Log.d(TAG, "onStart: Playback info callback set");
            if (player.isPlaying()) {
                /* Playback is in progress, we need to sync UI to player state */
                mButtonPlay.setText(R.string.button_pause);
                player.syncSeekbar();
                Log.d(TAG, "onStart: playing, sync UI");
            } else if (player.getState() == PlaybackInfoListener.State.PAUSED) {
                /* Player is paused, sync UI */
                mButtonPlay.setText(R.string.button_play);
                player.syncSeekbar();
                Log.d(TAG, "onStart: paused, sync UI");
            } else {
                /* Player is stopped or completed, we need fresh MediaPlayer*/
                mButtonPlay.setText(R.string.button_play);
                player.loadMedia(this, MEDIA_RES_ID);
                Log.d(TAG, "onStart: created MediaPlayer");
            }
        }
        mPlayerAdapter = player;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() && mPlayerAdapter.isPlaying()) {
            Log.d(TAG, "onStop: don't release MediaPlayer as screen is rotating and playing");
        } else {
            mPlayerAdapter.release();
            Log.d(TAG, "onStop: release MediaPlayer");
        }
    }

    private void initUi() {
        /* Get views */
        mTextLog = findViewById(R.id.text_log);
        mScrollLog = findViewById(R.id.scroll_log);
        mSeekbarAudio = findViewById(R.id.seekbar_audio);
        mButtonPlay = findViewById(R.id.button_play);
        Button buttonStop = findViewById(R.id.button_stop);

        /* Set onClick listeners */
        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerAdapter.isPlaying()) {
                    mPlayerAdapter.pause();
                    mButtonPlay.setText(R.string.button_play);
                } else {
                    mPlayerAdapter.play();
                    mButtonPlay.setText(R.string.button_pause);
                }
            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerAdapter.stop(MainActivity.this);
                mButtonPlay.setText(R.string.button_play);
            }
        });
    }

    private void initSeekbar() {
        mSeekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int userSelectedPosition = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    userSelectedPosition = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mUserIsSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mUserIsSeeking = false;
                mPlayerAdapter.seekTo(userSelectedPosition);
            }
        });
    }

    /**
     * This subclass of PlaybackInfoListener is designed to feed playback info to MainActivity,
     * so it's tightly coupled with it.
     */
    private class PlaybackInfoListenerMain extends PlaybackInfoListener {

        @Override
        void onLogUpdated(String message) {
            if (mTextLog != null) {
                mTextLog.append(message);
                mTextLog.append("\n");
                /* Move the ScrollView focus to the end */
                mScrollLog.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollLog.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }

        @Override
        void onStateChanged(@State int state) {
            String stateStr = PlaybackInfoListener.stateToStr(state);
            onLogUpdated(String.format("MediaPlayer state changed to %s", stateStr));
        }

        @Override
        void onPlaybackCompleted() {
            mSeekbarAudio.setProgress(0);
            mButtonPlay.setText(R.string.button_play);
        }

        @Override
        void onDurationChanged(int duration) {
            mSeekbarAudio.setMax(duration);
            Log.d(TAG, String.format("onDurationChanged: max duration is set to %dms", duration));
        }

        @Override
        void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                mSeekbarAudio.setProgress(position);
                Log.d(TAG, String.format("onPositionChanged: position is set to %dms", position));
            }
        }
    }
}
