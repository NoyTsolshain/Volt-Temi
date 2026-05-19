package com.example.volt.status;

import com.example.volt.status.message.Message;

import java.util.List;

public interface MessageCollection {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds a message to the end of the collection. Does nothing if there's already
     *                      a message of that type in the collection.</li>
     * </ul>
     * </p>
     * @param message The message to add to the collection.
     */
    void addMessage(Message message);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Removes the message with the key from the collection. Does nothing if there's no
     *                      message of that type in the collection.</li>
     * </ul>
     * </p>
     * @param key The type of message to be removed from the collection.
     */
    void removeMessage(String key);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns a collection of all messages.</li>
     * </ul>
     * </p>
     */
    List<Message> getAllMessages();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the index of the element with the key or -1 if there's no such element.</li>
     * </ul>
     * </p>
     */
    int indexOf(String key);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Replaces all instances of battery message with the new one. Does nothing if the new instance if null.</li>
     * </ul>
     * </p>
     */
    void updateBattery(Message newBatteryMessage);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Checks if the collection is empty and returns true iff it is empty.</li>
     * </ul>
     * </p>
     */
    boolean isEmpty();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the number of elements in the collection.</li>
     * </ul>
     * </p>
     */
    int size();
}
