package com.example.volt.popup;

/**
 * <h6>
 * A listener for handling close events from a {@link PopupView}.
 * </h6>
 */
public interface OnPopupViewClosedListener {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>The listener reacts to the event.</li>
     * </ul>
     * </p>
     * @param source The {@link PopupView} instance that was closed.
     */
    void onPopupViewClosed(PopupView source);
}
