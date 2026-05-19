package com.example.volt.screen;

import android.view.View;

public interface FAQView {

    void show();

    void hide();

    void setOnHideListener(View.OnClickListener onClickListener);
}
