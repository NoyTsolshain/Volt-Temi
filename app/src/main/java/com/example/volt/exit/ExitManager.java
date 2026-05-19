package com.example.volt.exit;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.constants.HomeScreenMode;

public class ExitManager {
    private final ImageView exitImageView;

    public ExitManager(ImageView exitButton) {
        this.exitImageView = exitButton;
        this.setExitOnClickListener();
    }

    private void setExitOnClickListener() {
        this.exitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) exitImageView.getContext()).finish();
            }
        });
    }
}