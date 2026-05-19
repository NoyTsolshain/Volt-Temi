package com.example.volt.selected;

/**
 * <h6>
 * A listener for dragging operations on the recycler view.
 * </h6>
 */
public interface OnItemDraggedListener {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Update the dataset by moving the item at {@code initialPosition} to {@code finalPosition}.</li>
     * </ul>
     * </p>
     * @param initialPosition   The initial position of the item.
     * @param finalPosition     The new position of the item.
     */
    void onItemDragged(int initialPosition, int finalPosition);
}
