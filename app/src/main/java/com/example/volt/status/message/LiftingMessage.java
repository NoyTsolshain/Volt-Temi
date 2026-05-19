package com.example.volt.status.message;

import com.example.volt.R;

public class LiftingMessage extends Message {
    public static final String KEY = "lifting";
    private static final int TEXT_ID    = R.string.lifting_warning;
    private static final int COLOR_ID   = R.color.warning_red;
    private static final int ICON_ID    = R.drawable.lifting_warning;

    public LiftingMessage() {
        super(LiftingMessage.TEXT_ID, LiftingMessage.COLOR_ID, LiftingMessage.ICON_ID);
    }

    @Override
    public String getKey() {
        return LiftingMessage.KEY;
    }
}
