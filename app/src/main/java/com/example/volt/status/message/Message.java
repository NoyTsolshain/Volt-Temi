package com.example.volt.status.message;

public abstract class Message {
    private final int textID;
    private final int colorID;
    private final int iconID;

    public Message(int textID, int colorID, int iconID) {
        this.textID     = textID;
        this.colorID    = colorID;
        this.iconID     = iconID;
    }

    public abstract String getKey();

    public int getText() {
        return this.textID;
    }

    public int getColor() {
        return this.colorID;
    }

    public int getIcon() {
        return this.iconID;
    }
}