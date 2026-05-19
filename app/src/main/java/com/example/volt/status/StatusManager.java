package com.example.volt.status;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volt.status.message.BatteryMessage;
import com.example.volt.status.message.Message;
import com.robotemi.sdk.BatteryData;
import com.robotemi.sdk.Robot;

public class StatusManager {
    private RecyclerView                recyclerView;
    private StatusAdapter               recyclerViewAdapter;
    private MessageCollection           messageCollection;
    private static final int            DEFAULT_BATTERY = 100;

    public StatusManager(RecyclerView recyclerView) {
        this.initializeMemberVariables(recyclerView);
        this.initializeAdapter();
    }

    public void addMessage(Message message) {
        recyclerView.post(() -> {
            this.messageCollection.addMessage(message);
            this.recyclerViewAdapter.notifyDataSetChanged();
        });
    }

    public void removeMessage(String key) {
        recyclerView.post(() -> {
            this.messageCollection.removeMessage(key);
            this.recyclerViewAdapter.notifyDataSetChanged();
        });
    }

    public void updateBattery(int batteryLevel) {
        recyclerView.post(() -> {
            this.messageCollection.updateBattery(BatteryMessage.createMessage(batteryLevel));
            this.recyclerViewAdapter.notifyDataSetChanged();
        });
    }

    private void initializeMemberVariables(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.messageCollection = new UniqueOrderableMessageCollection();
        this.messageCollection.addMessage(BatteryMessage.createMessage(this.getBatteryPercentage()));
        this.recyclerViewAdapter = new StatusAdapter(this.messageCollection.getAllMessages());
    }

    private void initializeAdapter() {
        this.recyclerView.setAdapter(recyclerViewAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private int getBatteryPercentage() {
        BatteryData batteryData = Robot.getInstance().getBatteryData();
        if (batteryData != null) {
            return batteryData.getBatteryPercentage();
        }
        else {
            return StatusManager.DEFAULT_BATTERY;
        }
    }
}