package com.example.volt.status.message;

import com.example.volt.R;

public class DraggingMessage extends Message {
    public static final String KEY     = "dragging";
    private static final int TEXT_ID    = R.string.pushing_warning;
    private static final int COLOR_ID   = R.color.warning_red;
    private static final int ICON_ID    = R.drawable.pushing_warning;

    public DraggingMessage() {
        super(DraggingMessage.TEXT_ID, DraggingMessage.COLOR_ID, DraggingMessage.ICON_ID);
    }

    @Override
    public String getKey() {
        return DraggingMessage.KEY;
    }
}
