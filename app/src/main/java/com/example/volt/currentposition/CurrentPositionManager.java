package com.example.volt.currentposition;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.model.Position;
import com.robotemi.sdk.Robot;

/**
 * <h6>
 * {@link CurrentPositionManager} updates the {@link ImageView} that represents the current position of the robot so it
 * always shows the right position.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code currentPositionImageView != null}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link CurrentPositionManager} {@code cpm} represents a link between the {@link Robot} and the {@link ImageView}
 * that represents the current position such that:
 * <ul>
 *  <li>{@code cpm.currentPositionImageView} represents the current position on the screen.</li>
 * </ul>
 * </p>
 */
public class CurrentPositionManager implements OnCurrentPositionChangedListener {

    private final ImageView currentPositionImageView;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code currentPositionImageView != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new manager.</li>
     * </ul>
     * </p>
     *
     * @param currentPositionImageView The {@link ImageView} that represents the current position.
     */
    public CurrentPositionManager(ImageView currentPositionImageView) {
        this.currentPositionImageView = currentPositionImageView;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>A callback that is called each time the position of the robot changes. Updates the {@link ImageView}
     *                      that indicates the current position of the robot.</li>
     * </ul>
     * </p>
     *
     * @param position The new position of the robot.
     */
    @Override
    public void onCurrentPositionChanged(@NonNull Position position) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) currentPositionImageView.getLayoutParams();

        double x = -position.getX();
        double y = position.getY();
        double newX = x * Math.cos(Math.toRadians(-3)) + y * Math.sin(Math.toRadians(-3));
        double newY = -x * Math.sin(Math.toRadians(-3)) + y * Math.cos(Math.toRadians(-3));

        double newerX = 57 * (newX);
        double newerY = 30 * newY;

        layoutParams.leftMargin = (int) newerX;
        layoutParams.topMargin = (int) newerY;
        currentPositionImageView.setLayoutParams(layoutParams);
        currentPositionImageView.requestLayout();
    }
}