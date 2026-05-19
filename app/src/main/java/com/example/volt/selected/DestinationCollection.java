package com.example.volt.selected;

import com.example.volt.model.Destination;

import java.util.List;

/**
 * <h6>
 * A collection of destinations that notifies listeners about changes.
 * </h6>
 * */
public interface DestinationCollection {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds a destination to the end of the collection and notifies the listeners.
     *                      Does nothing if destination is null.</li>
     * </ul>
     * </p>
     * @param destination The destination to add to the collection.
     */
    void addDestination(Destination destination);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Removes the destination at index from the collection and notifies the listeners.
     *                      Does nothing if destination is null.</li>
     * </ul>
     * </p>
     * @param  index The index of the destination to be removed from the collection.
     */
    void removeDestinationAt(int index);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the next destination from the collection in a queue-like style.</li>
     * </ul>
     * </p>
     */
    Destination getNextDestination();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Removes the next destination from the collection in a queue-like style and notifies the listeners.
     *                      Does nothing if the collection is empty.</li>
     * </ul>
     * </p>
     */
    void removeNextDestination();

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
     *  <li><b>effects: </b>Gets a List of all destinations in the collection.</li>
     * </ul>
     * </p>
     */
    List<Destination> getAllDestinations();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Moves the item at initialPosition to finalPosition.</li>
     * </ul>
     * </p>
     * @param initialPosition The initial position.
     * @param finalPosition The new position.
     */
    void updateDestinationPosition(int initialPosition, int finalPosition);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets listener as a listener to be notified when the collection changes.
     *                      Does nothing if listener is null.</li>
     * </ul>
     * </p>
     * @param listener The object to be notified for changes.
     */
    void addListener(OnItemAddedListener listener);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Notifies the listeners that the collection has changed .</li>
     * </ul>
     * </p>
     */
    void notifyListeners(Destination destination);
}