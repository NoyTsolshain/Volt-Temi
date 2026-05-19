package com.example.volt.status.message;

import com.example.volt.R;

public class ReposeMessage extends Message {
    public static final String KEY = "repose";
    private static final int TEXT_ID    = R.string.positioning_warning;
    private static final int COLOR_ID   = R.color.warning_orange;
    private static final int ICON_ID    = R.drawable.positioning_warning;

    public ReposeMessage() {
        super(ReposeMessage.TEXT_ID, ReposeMessage.COLOR_ID, ReposeMessage.ICON_ID);
    }

    @Override
    public String getKey() {
        return ReposeMessage.KEY;
    }
}
