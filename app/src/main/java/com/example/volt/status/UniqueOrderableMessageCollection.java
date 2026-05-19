package com.example.volt.status;

import com.example.volt.status.message.BatteryMessage;
import com.example.volt.status.message.Message;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.function.UnaryOperator;

public class UniqueOrderableMessageCollection implements MessageCollection {
    private final Set<String>                     keySet;
    private final List<Message>                   messageList;

    public UniqueOrderableMessageCollection() {
        this.keySet = new HashSet<>();
        this.messageList = new ArrayList<>();
    }

    @Override
    public void addMessage(Message message) {
        if (message != null && !this.keySet.contains(message.getKey())) {
            this.messageList.add(message);
            this.keySet.add(message.getKey());
        }
    }

    @Override
    public void removeMessage(String key) {
        if (key != null && this.keySet.contains(key)) {
            this.keySet.remove(key);
            this.messageList.removeIf(message -> message.getKey().equals(key));
        }
    }

    @Override
    public List<Message> getAllMessages() {
        return Collections.unmodifiableList(this.messageList);
    }

    @Override
    public int indexOf(String key) {
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public void updateBattery(Message newBatteryMessage) {
        if (newBatteryMessage != null) {
            this.messageList.replaceAll(message -> {
                if (message.getKey().equals(BatteryMessage.KEY)) {
                    return newBatteryMessage;
                } else {
                    return message;
                }
            });
        }
    }

    @Override
    public boolean isEmpty() {
        return this.messageList.isEmpty();
    }

    @Override
    public int size() {
        return this.messageList.size();
    }
}