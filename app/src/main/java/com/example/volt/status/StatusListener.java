package com.example.volt.status;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.volt.status.message.DraggingMessage;
import com.example.volt.status.message.LiftingMessage;
import com.example.volt.status.message.Message;
import com.example.volt.status.message.ReposeMessage;
import com.robotemi.sdk.BatteryData;
import com.robotemi.sdk.listeners.OnBatteryStatusChangedListener;
import com.robotemi.sdk.listeners.OnRobotDragStateChangedListener;
import com.robotemi.sdk.listeners.OnRobotLiftedListener;
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener;

public class StatusListener implements OnRobotDragStateChangedListener,
                                       OnRobotLiftedListener,
                                       OnReposeStatusChangedListener,
                                       OnBatteryStatusChangedListener {

    private StatusManager     statusManager;
    private Message           draggingMessage;
    private Message           liftingMessage;
    private Message           reposeMessage;
    private static final int  DEFAULT_BATTERY = 100;

    public StatusListener(StatusManager statusManager) {
        this.initialize(statusManager);
    }

    @Override
    public void onBatteryStatusChanged(@Nullable BatteryData batteryData) {
        int batteryLevel;
        if (batteryData != null) {
            batteryLevel = batteryData.getBatteryPercentage();
        }
        else {
            batteryLevel = StatusListener.DEFAULT_BATTERY;
        }
        this.statusManager.updateBattery(batteryLevel);
    }

    @Override
    public void onRobotDragStateChanged(boolean isDragged) {
        if (isDragged) {
            this.statusManager.addMessage(this.draggingMessage);
        }
        else {
            this.statusManager.removeMessage(DraggingMessage.KEY);
        }
    }

    @Override
    public void onRobotLifted(boolean isLifted, @NonNull String description) {
        if (isLifted) {
            this.statusManager.addMessage(this.liftingMessage);
        }
        else {
            this.statusManager.removeMessage(LiftingMessage.KEY);
        }
    }

    @Override
    public void onReposeStatusChanged(int status, @NonNull String description) {
        if (status == REPOSE_REQUIRED) {
            this.statusManager.addMessage(this.reposeMessage);
        }
        else if (status == REPOSING_COMPLETE) {
            this.statusManager.removeMessage(ReposeMessage.KEY);
        }
    }

    private void initialize(StatusManager statusManager) {
        this.statusManager      = statusManager;
        this.draggingMessage    = new DraggingMessage();
        this.liftingMessage = new LiftingMessage();
        this.reposeMessage      = new ReposeMessage();
    }
}