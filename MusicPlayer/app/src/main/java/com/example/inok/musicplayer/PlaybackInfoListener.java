package com.example.inok.musicplayer;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Allows {@link Player} to report media playback info to the {@link MainActivity}.
 */
abstract class PlaybackInfoListener {

    @IntDef({State.INVALID, State.PLAYING, State.PAUSED, State.STOPPED, State.COMPLETED})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {

        int INVALID = -1;
        int PLAYING = 0;
        int PAUSED = 1;
        int STOPPED = 2;
        int COMPLETED = 3;
    }

    static String stateToStr(@State int state) {
        String stateStr;
        switch (state) {
            case State.COMPLETED:
                stateStr = "COMPLETED";
                break;
            case State.INVALID:
                stateStr = "INVALID";
                break;
            case State.PAUSED:
                stateStr = "PAUSED";
                break;
            case State.PLAYING:
                stateStr = "PLAYING";
                break;
            case State.STOPPED:
                stateStr = "STOPPED";
                break;
            default:
                stateStr = "N/A";
                break;
        }
        return stateStr;
    }

    void onLogUpdated(String message) {}

    void onStateChanged(@State int state) {}

    void onPlaybackCompleted() {}

    void onDurationChanged(int duration) {}

    void onPositionChanged(int position) {}
}
