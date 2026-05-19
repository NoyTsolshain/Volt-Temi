package com.example.volt.popup;

import android.util.Log;

import com.example.volt.model.Destination;
import com.example.volt.selected.DestinationCollection;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.navigation.model.SpeedLevel;

/**
 * <h6>
 * {@link PopupManager} is a link layer between {@link PopupView}, {@link Destination}, {@link DestinationCollection}
 * and the other managers. Controls opening and closing of {@link PopupView}s and tracks the {@link Destination}s that
 * they show.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code destinationPopupViewLeft != null}</li>
 *  <li>{@code destinationPopupViewRight != null}</li>
 *  <li>{@code destinationCollection != null}</li>
 *  <li>{@code currentlyDisplayedDestinationLeft != null iff destinationPopupViewLeft.isOpen()}</li>
 *  <li>{@code currentlyDisplayedDestinationRight != null iff destinationPopupViewRight.isOpen()}</li>
 *  <li>At most one popup view is open at a time:<br>
 *      {@code !(destinationPopupViewLeft.isOpen() && destinationPopupViewRight.isOpen())}
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link PopupManager} {@code pm} represents a link between {@link PopupView}, {@link Destination}, {@link DestinationCollection}
 * and the other managers such that:
 * <ul>
 *  <li>{@code pm.destinationPopupViewLeft} is the left {@link PopupView}</li>
 *  <li>{@code pm.destinationPopupViewRight} is the right {@link PopupView}</li>
 *  <li>{@code pm.currentlyDisplayedDestinationLeft} is the {@link Destination} that's currently being displayed in the left {@link SlidingPopupView}.</li>
 *  <li>{@code pm.currentlyDisplayedDestinationRight} is the {@link Destination} that's currently being displayed in the right {@link SlidingPopupView}.</li>
 *  <li>{@code pm.destinationCollection} is the collection of all {@link Destination}s.</li>
 * </ul>
 * </p>
 */
public class PopupManager implements OnPopupViewClosedListener,
        OnPopupViewGoNowListener,
        OnPopupViewAddToListListener,
        OnPopupDestinationChangedListener {
    private PopupView destinationPopupViewLeft;
    private PopupView destinationPopupViewRight;
    private Destination currentlyDisplayedDestinationLeft;
    private Destination currentlyDisplayedDestinationRight;
    private DestinationCollection destinationCollection;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code destinationPopupViewLeft != null}, {@code destinationPopupViewRight != null}, {@code destinationCollection != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new manager.</li>
     * </ul>
     * </p>
     * @param destinationPopupViewLeft      The left {@link PopupView}.
     * @param destinationPopupViewRight     The right {@link PopupView}.
     * @param destinationCollection         The The collection of selected {@link Destination}s.
     */
    public PopupManager(PopupView destinationPopupViewLeft, PopupView destinationPopupViewRight, DestinationCollection destinationCollection) {
        this.initialize(destinationPopupViewLeft, destinationPopupViewRight, destinationCollection);
        this.initializeListeners();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Opens the left {@link PopupView}, records the {@link Destination} it shows and closes
     *         the right {@link PopupView}. Does nothing if destination is {@code null}</li>
     * </ul>
     * </p>
     * @param destination The {@link Destination} to display in the left {@link PopupView}.
     */
    public void openLeftPopupView(Destination destination) {
        this.checkRepresentation();
        if (destination != null) {
            this.destinationPopupViewLeft.open(destination);
            this.currentlyDisplayedDestinationLeft = destination;
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Opens the right {@link PopupView}, records the {@link Destination} it shows and closes
     *         the left {@link PopupView}. Does nothing if destination is {@code null}</li>
     * </ul>
     * </p>
     * @param destination The {@link Destination} to display in the right {@link PopupView}.
     */
    public void openRightPopupView(Destination destination) {
        this.checkRepresentation();
        if (destination != null) {
            this.destinationPopupViewRight.open(destination);
            this.currentlyDisplayedDestinationRight = destination;
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Closes the left {@link PopupView} and stops tracking the {@link Destination} it showed.</li>
     * </ul>
     * </p>
     */
    public void closeLeftPopupView() {
        this.checkRepresentation();
        this.destinationPopupViewLeft.close();
        this.currentlyDisplayedDestinationLeft = null;
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Closes the right {@link PopupView} and stops tracking the {@link Destination} it showed.</li>
     * </ul>
     * </p>
     */
    public void closeRightPopupView() {
        this.checkRepresentation();
        this.destinationPopupViewRight.close();
        this.currentlyDisplayedDestinationRight = null;
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Closes the appropriate {@link PopupView} according to {@code source}. Does nothing
     *         if {@code source == null}.</li>
     * </ul>
     * </p>
     * @param source the {@link PopupView} instance that was closed.
     */
    @Override
    public void onPopupViewClosed(PopupView source) {
        this.checkRepresentation();
        if (source != null) {
            if (source == this.destinationPopupViewLeft) {
                this.destinationPopupViewLeft.close();
                currentlyDisplayedDestinationLeft = null;
            } else if (source == this.destinationPopupViewRight) {
                this.destinationPopupViewRight.close();
                currentlyDisplayedDestinationRight = null;
            }
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Closes the appropriate {@link PopupView} according to {@code source} and starts navigating to the
     *         {@link Destination} that was showed in {@code source}. Does nothing if {@code source == null}</li>
     * </ul>
     * </p>
     * @param source the {@link PopupView} that initiated the go-now request.
     */
    @Override
    public void onGoNow(PopupView source) {
        this.checkRepresentation();
        if (source != null) {
            if (source == this.destinationPopupViewLeft) {
                Robot.getInstance().goTo(this.currentlyDisplayedDestinationLeft.getInternalName(), true, false, SpeedLevel.HIGH);
                this.currentlyDisplayedDestinationLeft = null;
                this.destinationPopupViewLeft.close();
            } else if (source == this.destinationPopupViewRight) {
                Robot.getInstance().goTo(currentlyDisplayedDestinationRight.getInternalName(), true, false, SpeedLevel.HIGH);
                this.currentlyDisplayedDestinationRight = null;
                this.destinationPopupViewRight.close();
            }
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Closes the appropriate {@link PopupView} according to {@code source} and adds the
     *         {@link Destination} that was showed in {@code source} to the list. Does nothing if {@code source == null}</li>
     * </ul>
     * </p>
     * @param source the {@link PopupView} that initiated the go-now request.
     */
    @Override
    public void onAddToList(PopupView source) {
        this.checkRepresentation();
        if (source != null) {
            if (source == this.destinationPopupViewLeft) {
                this.destinationCollection.addDestination(this.currentlyDisplayedDestinationLeft);
                currentlyDisplayedDestinationLeft = null;
                this.destinationPopupViewLeft.close();
            } else if (source == this.destinationPopupViewRight) {
                this.destinationCollection.addDestination(this.currentlyDisplayedDestinationRight);
                currentlyDisplayedDestinationRight = null;
                this.destinationPopupViewRight.close();
            }
        }
        this.checkRepresentation();
    }

    @Override
    public void onPopupDestinationChanged(PopupView source) {
        if (source != null) {
            if (source == this.destinationPopupViewLeft) {
                this.destinationPopupViewLeft.close();
                this.destinationCollection.addDestination(this.currentlyDisplayedDestinationLeft);
                currentlyDisplayedDestinationLeft = null;
            } else if (source == this.destinationPopupViewRight) {
                this.destinationPopupViewRight.close();
                this.destinationCollection.addDestination(this.currentlyDisplayedDestinationRight);
                currentlyDisplayedDestinationRight = null;
            }
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Registers a close listener on the managed {@link PopupView}s so that it will
     *         be notified whenever either popup view is closed. Does nothing if {@code onPopupViewClosedListener == null}.</li>
     * </ul>
     * </p>
     * @param onPopupDestinationChangedListener the {@link PopupView} that initiated the go-now request.
     */
    public void addOnPopupDestinationChangedListener(OnPopupDestinationChangedListener onPopupDestinationChangedListener) {
        this.checkRepresentation();
        if (onPopupDestinationChangedListener != null) {
            this.destinationPopupViewLeft.addOnPopupDestinationChangedListener(onPopupDestinationChangedListener);
            this.destinationPopupViewRight.addOnPopupDestinationChangedListener(onPopupDestinationChangedListener);
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Registers a close listener on the managed {@link PopupView}s so that it will
     *         be notified whenever either popup view is closed. Does nothing if {@code onPopupViewClosedListener == null}.</li>
     * </ul>
     * </p>
     * @param onPopupViewClosedListener the {@link PopupView} that initiated the go-now request.
     */
    public void addOnPopupViewClosedListener(OnPopupViewClosedListener onPopupViewClosedListener) {
        this.checkRepresentation();
        if (onPopupViewClosedListener != null) {
            this.destinationPopupViewLeft.addOnPopupViewClosedListener(onPopupViewClosedListener);
            this.destinationPopupViewRight.addOnPopupViewClosedListener(onPopupViewClosedListener);
        }
        this.checkRepresentation();
    }

    public Destination getCurrentlyDisplayedDestinationLeft() { return this.currentlyDisplayedDestinationLeft; }

    public Destination getCurrentlyDisplayedDestinationRight() { return this.currentlyDisplayedDestinationRight; }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code destinationPopupViewLeft != null}, {@code destinationPopupViewRight != null}, {@code destinationCollection != null}.</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Initializes the member variables of {@code this}.</li>
     * </ul>
     * </p>
     */
    private void initialize(PopupView destinationPopupViewLeft, PopupView destinationPopupViewRight, DestinationCollection destinationCollection) {
        this.destinationPopupViewLeft = destinationPopupViewLeft;
        this.destinationPopupViewRight = destinationPopupViewRight;
        this.currentlyDisplayedDestinationLeft = null;
        this.currentlyDisplayedDestinationRight = null;
        this.destinationCollection = destinationCollection;
        this.destinationPopupViewLeft.initializePosition(-1, 0);
        this.destinationPopupViewRight.initializePosition(1, 0);
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Register {@code this} as a listener for all managed {@link PopupView}s' events</li>
     * </ul>
     * </p>
     */
    private void initializeListeners() {
        this.destinationPopupViewLeft.addOnPopupViewClosedListener(this);
        this.destinationPopupViewLeft.addOnPopupViewGoNowListener(this);
        this.destinationPopupViewLeft.addOnPopupViewAddToListListener(this);
        this.destinationPopupViewRight.addOnPopupViewClosedListener(this);
        this.destinationPopupViewRight.addOnPopupViewGoNowListener(this);
        this.destinationPopupViewRight.addOnPopupViewAddToListListener(this);
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
        assert this.destinationPopupViewLeft != null;
        assert this.destinationPopupViewRight != null;
        assert this.destinationCollection != null;
        //assert this.currentlyDisplayedDestinationLeft != null == this.destinationPopupViewLeft.isOpen();
        //assert this.currentlyDisplayedDestinationRight != null == this.destinationPopupViewRight.isOpen();
        assert !(this.destinationPopupViewLeft.isOpen() && this.destinationPopupViewRight.isOpen());
    }
}