package com.example.volt.map;

import android.view.View;

/**
 * <h6>
 * {@link MapMarkerView} visually represents a location in the robot's mapping space.
 * </h6>
 */
public interface MapMarkerView {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Highlights the view.</li>
     * </ul>
     * </p>
     */
    void highlight();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Removes the highlighting from the view.</li>
     * </ul>
     * </p>
     */
    void removeHighlight();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Scales the view.</li>
     * </ul>
     * </p>
     */
    void changeSizeRelativeToZoom(float zoom);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Gets the original x coordinate of the view.</li>
     * </ul>
     * </p>
     */
    float getOriginalX();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Gets the {@link View}.</li>
     * </ul>
     * </p>
     */
    View getView();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onClickListener as a listener to be notified when the view is clicked.
     *         Does nothing if onClickListener is null</li>
     * </ul>
     * </p>
     * @param listener The listener to add.
     */
    void setOnClickListener(View.OnClickListener listener);
}