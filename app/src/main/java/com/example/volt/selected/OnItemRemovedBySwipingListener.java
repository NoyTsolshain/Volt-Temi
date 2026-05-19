/******************************************************************************
 * @file   OnItemRemovedBySwipingListener.java
 * @brief  Defines a callback interface for handling item removal via swipe actions.
 *
 * @author Itay
 * @date   December 2024
 *****************************************************************************/

/* Project package */
package com.example.volt.selected;

/**
 * <h6>
 * A listener for swiping operations on the recycler view.
 * </h6>
 */
public interface OnItemRemovedBySwipingListener {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>The listener reacts to the removal of the item at index.</li>
     * </ul>
     * </p>
     * @param index The index of the removed item in the adapter's dataset.
     */
    void onItemRemovedBySwiping(int index);
}