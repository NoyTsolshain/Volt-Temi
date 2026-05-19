package com.example.volt.popup;

import android.widget.Button;
import com.example.volt.model.Destination;

/**
 * <h6>
 * A screen that display a {@link Destination} and notifies listeners about user actions.
 * </h6>
 * */
public interface PopupView {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Opens the {@link SlidingPopupView}. If the {@link SlidingPopupView} is already opened
     *         then changing the displayed {@link Destination}. Does nothing if {@code destination == null}.</li>
     * </ul>
     * </p>
     * @param destination The {@link Destination} whose details will be displayed in the popup.
     */
    void open(Destination destination);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Closes the {@link SlidingPopupView}. Does nothing if {@link SlidingPopupView} is already closed.</li>
     * </ul>
     * </p>
     */
    void close();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns true iff this {@link PopupView} is open.</li>
     * </ul>
     * </p>
     */
    boolean isOpen();

    void initializePosition(int widthFactor, int openPosition);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onPopupViewClosedListener as a listener to be notified when the this {@link SlidingPopupView}
     *         is closed. Does nothing if {@code onPopupViewClosedListener == null}.</li>
     * </ul>
     * </p>
     * @param onPopupViewClosedListener The listener to add.
     */
    void addOnPopupViewClosedListener(OnPopupViewClosedListener onPopupViewClosedListener);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onPopupViewGoNowListener as a listener to be notified when the goNow {@link Button}
     *         is clicked. Does nothing if {@code onPopupViewGoNowListener == null}.</li>
     * </ul>
     * </p>
     * @param onPopupViewGoNowListener The listener to add.
     */
    void addOnPopupViewGoNowListener(OnPopupViewGoNowListener onPopupViewGoNowListener);

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onPopupViewAddToListListener as a listener to be notified when the addToList {@link Button}
     *         is clicked. Does nothing if {@code onPopupViewAddToListListener == null}.</li>
     * </ul>
     * </p>
     * @param onPopupViewAddToListListener The listener to add.
     */
    void addOnPopupViewAddToListListener(OnPopupViewAddToListListener onPopupViewAddToListListener);

    void addOnPopupDestinationChangedListener(OnPopupDestinationChangedListener onPopupDestinationChangedListener);
}