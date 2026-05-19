/******************************************************************************
 * @file   VoltScreenManager
 * @brief  Handles screen transitions
 *
 * @author Itay & Noy
 * @date   December 2024
 *****************************************************************************/

/* Project package */
package com.example.volt;

/* import statements */
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 *
 * */
public class ScreenManager {
    private final ConstraintLayout instructionsConstraintScreen;  /* The instructions screen */
    private final LoadingScreen loadingScreen;                    /* The loading screen */

    /**
     * Creates a new screen manager
     * @param instructionsConstraintLayout A reference to the instructions screen view
     * @param loadingScreen A reference to the loading screen view
     * */
    public ScreenManager(ConstraintLayout instructionsConstraintLayout, LoadingScreen loadingScreen) {
        this.instructionsConstraintScreen = instructionsConstraintLayout;
        this.loadingScreen = loadingScreen;
        showLoadingScreen();
    }

    /**
     * Shows the main screen
     * */
    public void showMainScreen() {
        this.instructionsConstraintScreen.setVisibility(View.GONE);
        this.loadingScreen.setVisibility(View.GONE);
    }

    /**
     * Shows the instructions screen
     * */
    public void showInstructionsScreen() {
        this.instructionsConstraintScreen.setVisibility(View.VISIBLE);
        this.loadingScreen.setVisibility(View.GONE);
    }

    /**
     * Shows the loading screen
     * */
    public void showLoadingScreen() {
        this.loadingScreen.setVisibility(View.VISIBLE);
        this.instructionsConstraintScreen.setVisibility(View.GONE);
    }
}
