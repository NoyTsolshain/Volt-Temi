package com.example.volt.map;

import android.graphics.Matrix;
import android.view.View;
import androidx.annotation.NonNull;
import com.example.volt.commnad.Command;
import com.example.volt.commnad.DestinationSelectedCommand;
import com.example.volt.model.Destination;
import com.example.volt.popup.OnPopupDestinationChangedListener;
import com.example.volt.popup.OnPopupViewClosedListener;
import com.example.volt.popup.PopupManager;
import com.example.volt.popup.PopupView;
import com.otaliastudios.zoom.ZoomEngine;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <h6>
 * {@link PopupManager} is a link layer between {@link MapView}, {@link PinButtonView}, and the other managers.
 * Controls zooming and highlighting.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code mapView != null}</li>
 *  <li>{@code destinationToViewMap != null}</li>
 *  <li>{@code foreach key in destinationToViewMap: key != null}</li>
 *  <li>{@code foreach value in destinationToViewMap: value != null}</li>
 *  <li>{@code destinationSelectedCommand != null}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link MapManager} {@code mm} represents a link between {@link MapView}, {@link PinButtonView}, and the other managers
 * allowing zooming and highlighting such that:
 * <ul>
 *  <li>{@code mm.mapView} represents the map.</li>
 *  <li>{@code pm.currentlyHighlighted} represents the currently highlighted {@link Destination}.</li>
 *  <li>{@code mm.destinationToViewMap} represents a mapping from {@link Destination} to its visual representation.</li>
 *  <li>{@code mm.destinationSelectedCommand} represents the operations to execute when a {@link Destination} is selected.</li>
 * </ul>
 * </p>
 */
public class MapManager implements OnPopupDestinationChangedListener, OnPopupViewClosedListener {
    private MapView                                 mapView;
    private DestinationMarkerView                   currentlyHighlighted;
    private Map<Destination, DestinationMarkerView> destinationToViewMap;
    private Command<Destination>                    destinationSelectedCommand;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code mapView != null}, {@code popupManager != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new manager.</li>
     * </ul>
     * </p>
     * @param mapView       The {@link MapView}.
     * @param popupManager  The {@link PopupManager}.
     */
    public MapManager(MapView mapView, PopupManager popupManager) {
        this.initialize(mapView, popupManager);

        /* Sets up zoom and click event listeners */
        this.setOnZoomListener();
        this.setOnClickListeners();
        this.checkRepresentation();
    }

    @Override
    public void onPopupDestinationChanged(PopupView source) {
        this.checkRepresentation();
        this.removeHighlight();
        this.checkRepresentation();
    }

    @Override
    public void onPopupViewClosed(PopupView source) {
        this.checkRepresentation();
        this.removeHighlight();
        this.checkRepresentation();
    }

    private void removeHighlight() {
        if (currentlyHighlighted != null) {
            this.currentlyHighlighted.removeHighlight();
            this.currentlyHighlighted = null;
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets a mapping for each {@link Destination} to its visual representation, {@link DestinationMarkerView}.</li>
     * </ul>
     * </p>
     */
    public Map<Destination, DestinationMarkerView> getDestinationToViewMap() {
        this.checkRepresentation();
        return this.destinationToViewMap;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets size of the map in which destination is located.</li>
     * </ul>
     * </p>
     * @param destination The {@link Destination} to calculate its side.
     */
    public Side getDestinationSideOnMap(Destination destination) {
        this.checkRepresentation();
        DestinationMarkerView destinationMarkerView = this.destinationToViewMap.get(destination);
        if (destinationMarkerView.getOriginalX() + 0.5 * destinationMarkerView.getView().getWidth() >= mapView.getMapWidth() / 2.0) {
            this.checkRepresentation();
            return Side.RIGHT;
        }
        else {
            this.checkRepresentation();
            return Side.LEFT;
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Zooms out.</li>
     * </ul>
     * </p>
     */
    public void zoomOut() {
        this.checkRepresentation();
        this.mapView.zoomOut();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Highlights the destination.</li>
     * </ul>
     * </p>
     * @param destination The {@link Destination} to highlight.
     */
    public void highlight(Destination destination) {
        this.checkRepresentation();
        DestinationMarkerView destinationMarkerView = this.destinationToViewMap.get(destination);
        if (destinationMarkerView != null) {
            destinationMarkerView.highlight();
            this.currentlyHighlighted = destinationMarkerView;
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Registers a listener to be called each time the zoom level changes.</li>
     * </ul>
     * </p>
     */
    private void setOnZoomListener() {
        this.mapView.setOnZoomListener(new ZoomEngine.Listener() {
            /**
             * Handles zoom events by changing the scale of the PinButtonViews.
             *
             * @param zoomEngine    The ZoomEngine managing zoom and pan states.
             * @param matrix        The transformation matrix representing the current zoom and pan state.
             */
            @Override
            public void onUpdate(@NonNull ZoomEngine zoomEngine, @NonNull Matrix matrix) {
                mapView.zoomMarkers(zoomEngine.getZoom());
            }

            /**
             * Handles idle events. Not used in this implementation.
             *
             * @param zoomEngine The ZoomEngine managing zoom and pan states.
             */
            @Override
            public void onIdle(@NonNull ZoomEngine zoomEngine) {
                return;
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Registers a listener to be called each time a {@link DestinationMarkerView} is clicked.</li>
     * </ul>
     * </p>
     */
    private void setOnClickListeners() {
        this.mapView.setOnClickListeners(new View.OnClickListener() {
            /**
             * @param view The clicked view.
             */
            @Override
            public void onClick(View view) {
                MapManager.this.destinationSelectedCommand.execute(
                        MapManager.this.convertMarkerToDestination((DestinationMarkerView) view.getParent().getParent())
                );
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code destinationMarkerView != null}</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns a {@link Destination} based on a visual representation of a {@link Destination}.</li>
     * </ul>
     * </p>
     */
    private Destination convertMarkerToDestination(DestinationMarkerView destinationMarkerView) {
        return new Destination(
                destinationMarkerView.getDisplayName(),
                destinationMarkerView.getInternalName(),
                destinationMarkerView.getDescription(),
                destinationMarkerView.getBuilding(),
                destinationMarkerView.getFloor(),
                destinationMarkerView.getImageID()
        );
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns a mapping from {@link Destination} to its visual representation.</li>
     * </ul>
     * </p>
     */
    private Map<Destination, DestinationMarkerView> initializeDestinationToViewMap() {
        Map<Destination, DestinationMarkerView> destinationToView = new HashMap<>();
        for (DestinationMarkerView destinationMarkerView : this.mapView.getAllMarkers()) {
            destinationToView.put(this.convertMarkerToDestination(destinationMarkerView), destinationMarkerView);
        }

        return destinationToView;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code mapView != null}, {@code popupManager != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Initializes the member variables of {@code this}.</li>
     * </ul>
     * </p>
     * @param mapView The {@link MapView}.
     * @param popupManager The {@link PopupManager}.
     */
    private void initialize(MapView mapView, PopupManager popupManager) {
        this.mapView = mapView;
        this.currentlyHighlighted = null;
        this.destinationToViewMap = this.initializeDestinationToViewMap();
        this.destinationSelectedCommand = new DestinationSelectedCommand(this, popupManager);
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
        assert this.mapView != null;
        assert this.destinationToViewMap != null;
        assert this.destinationSelectedCommand != null;
        Set<Destination> keys = this.destinationToViewMap.keySet();
        for (Destination destination : keys) {
            assert destination != null;
        }
        Collection<DestinationMarkerView> values = this.destinationToViewMap.values();
        for (DestinationMarkerView markerView : values) {
            assert markerView != null;
        }
    }
}