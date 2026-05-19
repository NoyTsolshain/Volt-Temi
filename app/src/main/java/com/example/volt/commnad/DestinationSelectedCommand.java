package com.example.volt.commnad;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.example.volt.map.MapManager;
import com.example.volt.model.Destination;
import com.example.volt.popup.OnPopupViewAddToListListener;
import com.example.volt.popup.OnPopupViewClosedListener;
import com.example.volt.popup.OnPopupViewGoNowListener;
import com.example.volt.popup.PopupManager;
import com.example.volt.popup.SlidingPopupView;

/**
 * <h6>
 * {@link DestinationSelectedCommand} is a concrete {@link Command} that encapsulates
 * the operations to perform when a {@link Destination} is selected.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code mapManager != null}</li>
 *  <li>{@code popupManager != null}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link DestinationSelectedCommand} {@code dsc} represents a block of operations to execute when a {@link Destination}
 * is selected such that:
 * <ul>
 *  <li>{@code dsc.mapManager} is a {@link MapManager} to which we delegate map-related operations.</li>
 *  <li>{@code dsc.popupManager} is a {@link PopupManager} to which we delegate popup-related operations.</li>
 * </ul>
 * </p>
 */
public class DestinationSelectedCommand implements Command<Destination> {

    private final MapManager mapManager;
    private final PopupManager popupManager;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code mapManager != null}, {@code popupManager != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new {@link DestinationSelectedCommand}.</li>
     * </ul>
     * </p>
     * @param mapManager    The {@link MapManager}.
     * @param popupManager  The {@link PopupManager}.
     */
    public DestinationSelectedCommand(MapManager mapManager, PopupManager popupManager) {
        this.mapManager = mapManager;
        this.popupManager = popupManager;
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Executes the operations associated with selecting a {@link Destination}. Does nothing
     *         if {@code destination == null}.</li>
     * </ul>
     * </p>
     * @param destination The {@link MapManager}.
     */
    @Override
    public boolean execute(Destination destination) {
        this.checkRepresentation();
        if (destination != null) {
            switch (this.mapManager.getDestinationSideOnMap(destination)) {
                case LEFT:
                    this.popupManager.closeLeftPopupView();
                    this.popupManager.openRightPopupView(destination);
                    break;
                case RIGHT:
                    this.popupManager.closeRightPopupView();
                    this.popupManager.openLeftPopupView(destination);
                    break;
            }
            this.mapManager.zoomOut();
            this.mapManager.highlight(destination);
        }
        this.checkRepresentation();
        return true;
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
        assert this.mapManager != null;
        assert this.popupManager != null;
    }
}