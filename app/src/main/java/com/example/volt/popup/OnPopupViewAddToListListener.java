package com.example.volt.popup;

/**
 * <h6>
 * A listener for handling "add to list" requests originating from a {@link PopupView}.
 * </h6>
 */
public interface OnPopupViewAddToListListener {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>The listener reacts to the event.</li>
     * </ul>
     * </p>
     * @param source the {@link PopupView} that initiated the add-to-list request.
     */
    void onAddToList(PopupView source);
}
