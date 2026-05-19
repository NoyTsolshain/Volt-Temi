package com.example.volt.screen;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ScreenManager {

    private FAQView faqScreen;
    private ImageView openFaqImageView;

    public ScreenManager(FAQView faqScreen, ImageView openFaqImageView) {
        this.initialize(faqScreen, openFaqImageView);
        this.setOnFAQScreenHideClickListener();
        this.setOnOpenFaqClicked();
        this.faqScreen.show();
    }

    private void setOnOpenFaqClicked() {
        this.openFaqImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenManager.this.faqScreen.show();
            }
        });
    }

    private void setOnFAQScreenHideClickListener() {
        this.faqScreen.setOnHideListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenManager.this.faqScreen.hide();
            }
        });
    }

    private void initialize(FAQView faqScreen, ImageView openFaqImageView) {
        this.faqScreen = faqScreen;
        this.openFaqImageView = openFaqImageView;
    }
}