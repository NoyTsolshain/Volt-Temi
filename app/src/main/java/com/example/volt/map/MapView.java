package com.example.volt.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.volt.R;
import com.otaliastudios.zoom.ZoomEngine;
import com.otaliastudios.zoom.ZoomLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * <h6>
 * {@link MapView} is a custom {@link ConstraintLayout} that represents the robot’s
 * navigable map with interactive {@link DestinationMarkerView} markers and a zoom mechanism.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code markerViews != null}</li>
 *  <li>{@code mapZoomLayout != null}</li>
 *  <li>{@code foreach destinationMarkerView in markerViews: destinationMarkerView != null}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * A {@link MapView} {@code mv} represents a zoomable, interactive map such that:
 * <ul>
 *  <li>{@code mv.markerViews} is the collection of all {@link DestinationMarkerView} on the map.</li>
 *  <li>{@code mv.mapZoomLayout} is the zoom mechanism.</li>
 * </ul>
 * </p>
 */
public class MapView extends ConstraintLayout {
    private List<DestinationMarkerView> markerViews;
    private ZoomLayout                  mapZoomLayout;
    private static final float          ZOOM_OUT = 1.0f;          /* The default zoom-out level */


    //-------------------------------------- Constructors --------------------------------------//

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new {@link MapView}.</li>
     * </ul>
     * </p>
     * @param context The application's {@link Context}, used to inflate the view and access resources.
     * @param attrs The {@link AttributeSet} containing XML-defined attributes for this view.
     */
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context);
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets a collection of all {@link DestinationMarkerView} on the map.</li>
     * </ul>
     * </p>
     */
    public List<DestinationMarkerView> getAllMarkers() {
        this.checkRepresentation();
        return this.markerViews;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onClickListener as a listener to be notified when a {@link DestinationMarkerView}
     *         is clicked. Does nothing if onClickListener is null</li>
     * </ul>
     * </p>
     * @param onClickListener The listener to add.
     */
    public void setOnClickListeners(OnClickListener onClickListener) {
        this.checkRepresentation();
        if (onClickListener != null) {
            for (MapMarkerView pinButtonView : this.markerViews) {
                pinButtonView.setOnClickListener(onClickListener);
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
     *  <li><b>effects: </b>Adds the onZoomListener as a listener to be notified when the user zooms
     *         Does nothing if onZoomListener is null</li>
     * </ul>
     * </p>
     * @param onZoomListener The listener to add.
     */
    public void setOnZoomListener(ZoomEngine.Listener onZoomListener) {
        this.checkRepresentation();
        if (onZoomListener != null) {
            this.mapZoomLayout.getEngine().addListener(onZoomListener);
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adjusts the sizes of the {@link DestinationMarkerView} relative to the current zoom level.</li>
     * </ul>
     * </p>
     * @param zoom The current zoom level.
     */
    public void zoomMarkers(float zoom) {
        this.checkRepresentation();
        for (MapMarkerView pinButtonView : this.markerViews) {
            pinButtonView.changeSizeRelativeToZoom(zoom);
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Zooms out to the default level.</li>
     * </ul>
     * </p>
     */
    public void zoomOut() {
        this.checkRepresentation();
        this.mapZoomLayout.zoomTo(ZOOM_OUT, true);
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the width of this {@link MapView}.</li>
     * </ul>
     * </p>
     */
    public int getMapWidth() {
        this.checkRepresentation();
        return this.findViewById(R.id.MapConstraintLayout).getWidth();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Initializes the member variables of {@code this}.</li>
     * </ul>
     * </p>
     * @param context The application's {@link Context}, used to inflate the view and access resources.
     */
    private void initialize(Context context) {
        /* Sets the layout that the MapView should display */
        View mapView = LayoutInflater.from(context).inflate(R.layout.map_view, this, true);

        /* Initializes pin button list */
        ConstraintLayout mapConstraintLayout = mapView.findViewById(R.id.MapConstraintLayout);
        this.markerViews = new ArrayList<>();
        for (int i = 0; i < mapConstraintLayout.getChildCount(); i++) {
            if (mapConstraintLayout.getChildAt(i) instanceof ImageView) continue;
            this.markerViews.add((PinButtonView) mapConstraintLayout.getChildAt(i));
        }

        /* Initializes the zoom layout */
        this.mapZoomLayout = mapView.findViewById(R.id.MapZoomLayout);
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
        assert this.markerViews != null;
        assert this.mapZoomLayout != null;
        for (DestinationMarkerView destinationMarkerView : this.markerViews) {
            assert destinationMarkerView != null;
        }
    }
}