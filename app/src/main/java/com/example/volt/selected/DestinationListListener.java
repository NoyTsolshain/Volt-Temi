package com.example.volt.selected;

/**
 * <h6>
 * A listener for changes in the destination collection.
 * </h6>
 */
public interface DestinationListListener {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>The listener reacts to the change in the collection.</li>
     * </ul>
     * </p>
     */
    void onDestinationListChanged();
}
