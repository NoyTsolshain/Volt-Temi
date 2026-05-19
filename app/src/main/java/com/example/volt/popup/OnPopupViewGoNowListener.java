package com.example.volt.popup;

/**
 * <h6>
 * A listener for handling "go now" requests originating from a {@link PopupView}.
 * </h6>
 */
public interface OnPopupViewGoNowListener {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>The listener reacts to the event.</li>
     * </ul>
     * </p>
     * @param source The {@link PopupView} that initiated the go-now request.
     */
    void onGoNow(PopupView source);
}
