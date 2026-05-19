/******************************************************************************
 * @file   LoadingScreenManager.java
 * @brief  Manages the behavior of the LoadingScreen by periodically switching the colors of its elements.
 *         This class acts as a wrapper around the LoadingScreen class, handling the timing and execution
 *         of color transitions. It uses a Handler to schedule periodic color changes at a fixed interval.
 *
 * @author Itay
 * @date   January 2025
 *****************************************************************************/

/* Project package */
package com.example.volt;

/* Import statements */
import android.os.Handler;

/**
 * A wrapper to the LoadingScreen class
 * */
public class LoadingScreenManager {
    private final LoadingScreen loadingScreen;              /* The LoadingScreen instance that this manager controls */
    private final int timeBetweenSwitching = 400;           /* The time interval (in milliseconds) between consecutive color transitions */
    private final int transitionDuration = 100;             /* The duration (in milliseconds) of each individual color transition animation */
    private final Handler handler = new Handler();          /* A handler used to schedule periodic execution of the color switching animation */
    private final Runnable switchColors = new Runnable() {  /* A runnable task that continuously switches colors in the LoadingScreen at fixed intervals */
        @Override
        public void run() {
            /* Switches the colors of the pins in the loading screen */
            loadingScreen.switchColors();

            /* Schedules the next color switch after the defined interval */
            handler.postDelayed(this, timeBetweenSwitching);
        }
    };

    /**
     * Initializes a new LoadingScreenManager instance.
     *
     * @param loadingScreen The loading screen to be managed.
     * */
    public LoadingScreenManager(LoadingScreen loadingScreen) {
        /* Stores a reference to the provided LoadingScreen instance */
        this.loadingScreen = loadingScreen;

        /* Sets the transition duration for color animations */
        this.loadingScreen.setTransitionDuration(this.transitionDuration);

        /* Starts the periodic color-switching animation */
        this.handler.post(this.switchColors);
    }
}