package com.example.volt.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.volt.R;
import com.example.volt.model.Destination;

/**
 * <h6>
 * {@link PinButtonView} is a custom {@link ConstraintLayout} that visually represents a
 * {@link Destination} on a map. It can dynamically appear either as a small location
 * pin or as a clickable button, depending on the zoom level of the map.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code button != null}</li>
 *  <li>{@code pinImageView != null}</li>
 *  <li>{@code displayName != null}</li>
 *  <li>{@code internalName != null}</li>
 *  <li>{@code description != null}</li>
 *  <li>{@code building != null}</li>
 *  <li>{@code floor != null}</li>
 *  <li>{@code imageResId > 0}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * A {@link PinButtonView} {@code pbv} represents the location of a single {@link Destination} on the map such that:
 * <ul>
 *  <li>{@code spv.button} displays the destination’s {@code displayName} when the zoom level is high enough,
 *                         allowing the user to click for more details</li>
 *  <li>{@code spv.pinImageView} shows a simplified pin icon when the zoom level is low.</li>
 *  <li>{@code spv.originalXValue} is the original x coordinate of the view.</li>
 *  <li>{@code spv.displayName} is the user-facing name of the location.</li>
 *  <li>{@code spv.internalName}, is the name of the location as known to the robot.</li>
 *  <li>{@code spv.description} is a textual description of the destination.</li>
 *  <li>{@code spv.building} is the name of the building where the destination is located.</li>
 *  <li>{@code spv.floor} is the floor number or identifier within the building.</li>
 *  <li>{@code spv.imageResId} is the resource ID of the image representing this destination.</li>
 * </ul>
 * </p>
 */
public class PinButtonView extends ConstraintLayout implements DestinationMarkerView {

    //-------------------------------------- Member Variables --------------------------------------//

    private Button                  button;
    private ImageView               pinImageView;
    private static final float      PIVOT_X = 0.5f;                         /* The x coordinate of the center of the scaling point */
    private static final float      PIVOT_Y = 1f - ((float)(0.33 / 3.18));  /* The y coordinate of the center of the scaling point */
    private static final float      HIGHLIGHT_FACTOR = 1.6f;                /* The scaling factor for highlighting */
    private static final float      REMOVE_HIGHLIGHT_FACTOR = 1f;           /* The default scaling factor */
    private static final float      ZOOM_THRESHOLD = 1.6f;                  /* The boundary between displaying an image and a button */
    private float                   originalXValue;
    private String                  displayName;
    private String                  internalName;
    private String                  description;
    private String                  building;
    private String                  floor;
    private int                     imageResId;


    //-------------------------------------- Constructors --------------------------------------//

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new {@link PinButtonView}.</li>
     * </ul>
     * </p>
     * @param context The application's {@link Context}, used to inflate the view and access resources.
     * @param attrs The {@link AttributeSet} containing XML-defined attributes for this view.
     */
    public PinButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context, attrs);
        this.checkRepresentation();
    }

    //-------------------------------------- DestinationAttributes Implementation --------------------------------------//

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the display name of the {@link Destination} that {@code this} represents.</li>
     * </ul>
     * </p>
     */
    @Override
    public String getDisplayName() {
        this.checkRepresentation();
        return this.displayName;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the internal name of the {@link Destination} that {@code this} represents.</li>
     * </ul>
     * </p>
     */
    @Override
    public String getInternalName() {
        this.checkRepresentation();
        return this.internalName;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the description of the {@link Destination} that {@code this} represents.</li>
     * </ul>
     * </p>
     */
    @Override
    public String getDescription() {
        this.checkRepresentation();
        return this.description;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the building of the {@link Destination} that {@code this} represents.</li>
     * </ul>
     * </p>
     */
    @Override
    public String getBuilding() {
        this.checkRepresentation();
        return this.building;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the floor of the {@link Destination} that {@code this} represents.</li>
     * </ul>
     * </p>
     */
    @Override
    public String getFloor() {
        this.checkRepresentation();
        return this.floor;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the resource ID of the image of the {@link Destination} that {@code this} represents.</li>
     * </ul>
     * </p>
     */
    @Override
    public int getImageID() {
        this.checkRepresentation();
        return this.imageResId;
    }


    //-------------------------------------- MapMarkerView Implementation --------------------------------------//

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Highlights {@code this}.</li>
     * </ul>
     * </p>
     */
    @Override
    public void highlight() {
        this.checkRepresentation();
        this.setSize(HIGHLIGHT_FACTOR);
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Removes the highlighting from {@code this}.</li>
     * </ul>
     * </p>
     */
    @Override
    public void removeHighlight() {
        this.checkRepresentation();
        this.setSize(REMOVE_HIGHLIGHT_FACTOR);
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Scales {@code this}.</li>
     * </ul>
     * </p>
     */
    @Override
    public void changeSizeRelativeToZoom(float zoom) {
        this.checkRepresentation();
        this.zoomPinButton(zoom, ZOOM_THRESHOLD);
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Gets the original x coordinate of {@code this}.</li>
     * </ul>
     * </p>
     */
    @Override
    public float getOriginalX() {
        this.checkRepresentation();
        return this.originalXValue;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Gets the {@link View} of {@code this}.</li>
     * </ul>
     * </p>
     */
    @Override
    public View getView() {
        this.checkRepresentation();
        return this;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onClickListener as a listener to be notified when the {@code this} is clicked.
     *         Does nothing if onClickListener is null</li>
     * </ul>
     * </p>
     * @param onClickListener The listener to add.
     */
    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.checkRepresentation();
        if (onClickListener != null) {
            this.button.setOnClickListener(onClickListener);
        }
        this.checkRepresentation();
    }

    //-------------------------------------- Helper Functions --------------------------------------//

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Changes the size of {@code this} around the tip of the pin.</li>
     * </ul>
     * </p>
     * @param scalingFactor The scaling factor to be applied.
     */
    private void setSize(float scalingFactor) {
        /* Scales the image around the tip of the pin */
        this.pinImageView.setPivotX(this.pinImageView.getWidth() * PIVOT_X);
        this.pinImageView.setPivotY(this.pinImageView.getHeight() * PIVOT_Y);
        this.pinImageView.setScaleX(scalingFactor);
        this.pinImageView.setScaleY(scalingFactor);
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b><ul>
     *          <li>If {@code zoom > threshold}, scales {@code button} by {@code 1/zoom}
     *              in both X and Y directions, sets its visibility to {@link View#VISIBLE},
     *              and sets {@code pinImageView} visibility to {@link View#INVISIBLE}.</li>
     *          <li>Otherwise, sets {@code button} visibility to {@link View#INVISIBLE}
     *              and {@code pinImageView} visibility to {@link View#VISIBLE}.</li>
     *          </ul></li>
     * </ul>
     * </p>
     * @param zoom current map zoom level.
     * @param threshold zoom level at which the button replaces the pin image.
     */
    private void zoomPinButton(float zoom, float threshold) {
        /* If zoom level is large enough, shows the button and hides the pin image */
        if (zoom > threshold) {
            float inverseZoom = (1 / zoom);
            this.button.setScaleX(inverseZoom);
            this.button.setScaleY(inverseZoom);
            this.button.setVisibility(View.VISIBLE);
            this.pinImageView.setVisibility(View.INVISIBLE);
        }

        /* If zoom level is too small, shows the pin image and hides the button */
        else {
            this.button.setVisibility(View.INVISIBLE);
            this.pinImageView.setVisibility(View.VISIBLE);
        }
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
     * @param attrs The {@link AttributeSet} containing XML-defined attributes for this view.
     */
    private void initialize(Context context, AttributeSet attrs) {
        /* Sets the layout of the view */
        View pinButtonView = LayoutInflater.from(context).inflate(R.layout.pin_button_view, this, true);

        /* Initializes the child views (button and pin image) */
        this.button = pinButtonView.findViewById(R.id.PinButtonButton);
        this.pinImageView = pinButtonView.findViewById(R.id.PinButtonImageView);

        /* Extracts custom attributes and initializes the Destination object */
        TypedArray customAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.pin_button_view, 0, 0);
        this.displayName = customAttributes.getString(R.styleable.pin_button_view_display_name);
        this.internalName = customAttributes.getString(R.styleable.pin_button_view_internal_name);
        this.description = customAttributes.getString(R.styleable.pin_button_view_description);
        this.building = customAttributes.getString(R.styleable.pin_button_view_building);
        this.floor = customAttributes.getString(R.styleable.pin_button_view_floor);
        this.imageResId = customAttributes.getResourceId(R.styleable.pin_button_view_image, 0);

        this.button.setText(this.displayName);

        /* Saves the initial x position after the layout is fully defined */
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /**
             * This method is called when the layout of the view is completed or updated.
             */
            @Override
            public void onGlobalLayout() {
                /* Saves the initial x position relative to the parent layout */
                originalXValue = getX();
            }
        });
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
        assert this.button != null;
        assert this.pinImageView != null;
        assert this.displayName != null;
        assert this.internalName != null;
        assert this.description != null;
        assert this.building != null;
        assert this.floor != null;
        assert this.imageResId > 0;
    }
}