package com.example.volt.audio;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Switch;
import androidx.annotation.NonNull;
import com.example.volt.R;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

/**
 * <h6>
 * {@link AudioManager} represents the ticking mechanism that's played during robot's movement. It's a link layer between
 * the view that enables the sound and the playing mechanism.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code audioFeedbackSwitch != null}</li>
 *  <li>{@code mediaPlayer != null}</li>
 *  <li>{@code mediaPlayer.isPlaying() iff the robot is moving and audioFeedbackSwitch.checked()}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link AudioManager} {@code am} represents the ticking mechanism such that:
 * <ul>
 *  <li>{@code am.audioFeedbackSwitch} is the {@link Switch} that enables and disables playing.</li>
 *  <li>{@code am.mediaPlayer} plays the sounds.</li>
 * </ul>
 * </p>
 */
public class AudioManager implements OnGoToLocationStatusChangedListener {
    private final Switch audioFeedbackSwitch;
    private MediaPlayer mediaPlayer;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code audioFeedbackSwitch != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new manager.</li>
     * </ul>
     * </p>
     * @param audioFeedbackSwitch The {@link Switch} that enables and disables the playing.
     */
    public AudioManager(Switch audioFeedbackSwitch) {
        this.audioFeedbackSwitch = audioFeedbackSwitch;
        this.setMediaPlayer();
        this.checkRepresentation();
    }

    public void pausePlayback() {
        this.checkRepresentation();
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Pauses the playing mechanism.</li>
     * </ul>
     * </p>
     */
    public void clearMediaPlayer() {
        if (this.mediaPlayer != null) {
            if (this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.stop();
            }
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>A callback that is called each time the go to status changes. Starts and stops playing
     *                      tick sounds when the robot starts or stops navigating.</li>
     * </ul>
     * </p>
     * @param location      The name of the location TEMI is navigating to.
     * @param status        Navigation status (start, calculating, going, complete, abort, reposing).
     * @param descriptionID Numerical code that reflects the description of the status.
     * @param description   Verbose more informative description of the navigation status (such as obstacle info).
     */
    @Override
    public void onGoToLocationStatusChanged(@NonNull String location, @NonNull String status, int descriptionID, @NonNull String description) {
        this.checkRepresentation();
        switch (status) {
            case START:
                if (this.audioFeedbackSwitch.isChecked()) {
                    this.mediaPlayer.start();
                }
                break;
            case COMPLETE:
            case ABORT:
                if (this.mediaPlayer.isPlaying()) {
                    this.mediaPlayer.pause();
                }
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the playing mechanism settings.</li>
     * </ul>
     * </p>
     */
    private void setMediaPlayer() {
        this.mediaPlayer = MediaPlayer.create(audioFeedbackSwitch.getContext(), R.raw.tick);
        this.mediaPlayer.setLooping(true);
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Checks that the representation invariant holds.</li>
     * </ul>
     * </p>
     * @throws AssertionError If the representation invariant is violated.
     */
    private void checkRepresentation() {
        assert this.audioFeedbackSwitch != null;
        assert this.mediaPlayer != null;
    }
}