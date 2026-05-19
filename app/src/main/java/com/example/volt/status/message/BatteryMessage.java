package com.example.volt.status.message;

import com.example.volt.R;

public class BatteryMessage extends Message {
    public static final String KEY      = "battery";
    private static final int TEXT_ID    = R.string.battery_warning;
    private static int COLOR_ID;
    private static final int ICON_ID    = R.drawable.battery_warning;
    private static final int THIRD      = 33;
    private static final int TWO_THIRDS = 67;

    private BatteryMessage() {
        super(BatteryMessage.TEXT_ID, BatteryMessage.COLOR_ID, BatteryMessage.ICON_ID);
    }

    public static Message createMessage(int batteryPercentage) {
        if (batteryPercentage <= THIRD) COLOR_ID = R.color.warning_red;
        else if (batteryPercentage <= TWO_THIRDS) COLOR_ID = R.color.warning_yellow;
        else COLOR_ID = R.color.warning_green;
        return new BatteryMessage();
    }

    @Override
    public String getKey() {
        return BatteryMessage.KEY;
    }
}