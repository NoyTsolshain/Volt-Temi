/******************************************************************************
 * @file   LoadingScreen.java
 * @brief A custom view representing a loading screen that shows an animated
 *        transition of colored pins while establishing a connection to TEMI.
 *
 * @author Itay
 * @date   January 2025
 *****************************************************************************/

/* Project package */
package com.example.volt;

/* Import statements */
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * A custom view that represents a loading screen.
 * */
public class LoadingScreen extends ConstraintLayout {
    private final ImageView[] images;                               /* The collection of images representing colorful pins */
    private final int[] colors = { R.color.pin_red,                 /* The colors of the images */
                                   R.color.pin_blue,
                                   R.color.pin_green,
                                   R.color.pin_orange,
                                   R.color.pin_pink,
                                   R.color.pin_purple,
                                   R.color.pin_yellow };
    private final int numberOfImages = 7;                           /* The number of pins to animate */
    private int shift;                                              /* The offset to determine the current color position */
    private int transitionDuration                                  /* The duration of the color transition animation */;
    private int DEFAULT_TRANSITION_DURATION = 100;                  /* The default duration of the animation in milliseconds */

    /**
     * Initializes a new LoadingScreen instance.
     *
     * @param context   The context of the application.
     * @param attrs     The attribute set defined in the XML layout.
     * */
    public LoadingScreen(Context context, AttributeSet attrs) {
        super(context, attrs);

        /* Inflates the LoadingScreen with its layout */
        View loadingScreen = LayoutInflater.from(context).inflate(R.layout.loading_screen, this, true);

        /* Initialize references to the image views representing pins */
        this.images = new ImageView[this.numberOfImages];
        images[0] = loadingScreen.findViewById(R.id.PinImageView1);
        images[1] = loadingScreen.findViewById(R.id.PinImageView2);
        images[2] = loadingScreen.findViewById(R.id.PinImageView3);
        images[3] = loadingScreen.findViewById(R.id.PinImageView4);
        images[4] = loadingScreen.findViewById(R.id.PinImageView5);
        images[5] = loadingScreen.findViewById(R.id.PinImageView6);
        images[6] = loadingScreen.findViewById(R.id.PinImageView7);
        this.shift = 0;

        /* Sets default transition duration */
        this.transitionDuration = this.DEFAULT_TRANSITION_DURATION;
    }

    /**
     * Switches the colors of the pins in a circular fashion.
     * */
    public void switchColors() {
        /* Stores the previous shift value */
        int formerShift = this.shift;

        /* Increments the shift value */
        this.shift = (this.shift + 1) % this.numberOfImages;

        /* Animates the color change for each pin */
        for (int i = 0; i < this.numberOfImages; i++) {
            final ImageView image = this.images[i];

            /* Gets the current and next color based on the shift */
            int currentColor = this.getContext().getColor(this.colors[(i + formerShift) % this.numberOfImages]);
            int nextColor = this.getContext().getColor(this.colors[(i + this.shift) % this.numberOfImages]);

            /* Create the color transition animator */
            ValueAnimator colorSwitchAnimator = ValueAnimator.ofArgb(currentColor, nextColor);
            colorSwitchAnimator.setDuration(this.transitionDuration);
            colorSwitchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                /**
                 * Called for each frame of the animation to update the pin color.
                 *
                 * @param valueAnimator The animator responsible for the animation.
                 */
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                    image.setImageTintList(ColorStateList.valueOf(((int)(valueAnimator.getAnimatedValue()))));
                }
            });

            /*  Start the color transition animation */
            colorSwitchAnimator.start();
        }
    }

    /**
     * Sets the duration for the color transition animation.
     *
     * @param transitionDuration The desired duration of the transition animation in milliseconds.
     * */
    public void setTransitionDuration(int transitionDuration) {
        this.transitionDuration = transitionDuration;
    }
}
