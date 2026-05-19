package com.example.volt.selected;

import android.util.Log;

import com.example.volt.model.Destination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <h6>
 * {@link DestinationReorderableQueue} is a collection of {@link Destination}s that can be reordered, notify listeners for changes
 * and managed in a queue-like style.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code destinations != null}</li>
 *  <li>{@code listeners != null}</li>
 *  <li>{@code foreach destination in destinations: destination != null}</li>
 *  <li>{@code foreach listener in listeners: listener != null}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link DestinationReorderableQueue} {@code drq} represents a collection of destinations that can be reordered and managed in a queue-like
 * style such that:
 * <ul>
 *  <li>{@code drq.destinations} are the {@link Destination}s in the collection such that {@code drq.destinations[i]} is the ith {@link Destination}.</li>
 *  <li>{@code drq.listeners} is a collection of objects that want to be notified when the collection changes.</li>
 * </ul>
 * </p>
 */
public class DestinationReorderableQueue implements DestinationCollection {
    private final List<Destination>              destinations;
    private final List<OnItemAddedListener>      listeners;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new empty collection.</li>
     * </ul>
     * </p>
     */
    public DestinationReorderableQueue() {
        this.destinations   = new ArrayList<>();
        this.listeners      = new ArrayList<>();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds a destination to the end of the collection. Does nothing if destination is null.</li>
     * </ul>
     * </p>
     * @param destination The destination to add to the collection.
     */
    @Override
    public void addDestination(Destination destination) {
        this.checkRepresentation();
        if (destination != null) {
            this.destinations.add(destination);
            this.notifyListeners(destination);
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Removes the destination at index from the collection. Does nothing if the index is out of bounds.</li>
     * </ul>
     * </p>
     * @param  index The index of the destination to be removed from the collection.
     */
    public void removeDestinationAt(int index) {
        this.checkRepresentation();
        if (index >= 0 && index < this.destinations.size()) {
            this.destinations.remove(index);
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the next destination from the collection in a queue-like style. Returns null if the
     *                      collection is empty.</li>
     * </ul>
     * </p>
     */
    @Override
    public Destination getNextDestination() {
        this.checkRepresentation();
        if (this.destinations.isEmpty()) {
            return null;
        }
        else {
            /* Returns the first element in the collection */
            return this.destinations.get(0);
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Removes the next destination from the collection in a queue-like style. Does nothing if the
     *                      collection is empty.</li>
     * </ul>
     * </p>
     */
    @Override
    public void removeNextDestination() {
        this.checkRepresentation();
        if (!this.destinations.isEmpty()) {
            /* Removes the first element in the collection */
            this.destinations.remove(0);
        }
        this.checkRepresentation();
    }

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
    @Override
    public boolean isEmpty() {
        this.checkRepresentation();
        return this.destinations.isEmpty();
    }

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
    @Override
    public List<Destination> getAllDestinations() {
        this.checkRepresentation();
        return Collections.unmodifiableList(this.destinations);
    }

    @Override
    public void updateDestinationPosition(int initialPosition, int finalPosition) {
        if (initialPosition >= 0 && initialPosition < this.destinations.size() &&
            finalPosition >= 0 && finalPosition < this.destinations.size()) {
            this.destinations.add(finalPosition, this.destinations.remove(initialPosition));
        }
    }

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
    @Override
    public void addListener(OnItemAddedListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

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
    @Override
    public void notifyListeners(Destination destination) {
        for (OnItemAddedListener listener : this.listeners) {
            listener.onItemAdded(destination);
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Checks that the representation invariant holds.</li>
     * </ul>
     * </p>
     * @throws AssertionError If the representation invariant is violated.
     */
    private void checkRepresentation() {
        assert this.destinations != null;
        for (Destination destination : this.destinations) {
            assert destination != null;
        }
    }
}