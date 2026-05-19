package com.example.volt.popup;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.volt.model.Destination;
import com.example.volt.R;
import java.util.ArrayList;
import java.util.List;

/**
 * <h6>
 * {@link SlidingPopupView} is a custom view that displays detailed information about a {@link Destination}.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code destinationNameTextView != null}</li>
 *  <li>{@code destinationDescriptionTextView != null}</li>
 *  <li>{@code destinationBuildingTextView != null}</li>
 *  <li>{@code destinationFloorTextView != null}</li>
 *  <li>{@code destinationImageView != null}</li>
 *  <li>{@code closeImageView != null}</li>
 *  <li>{@code goNowButton != null}</li>
 *  <li>{@code addToListButton != null}</li>
 *  <li>{@code onPopupViewClosedListeners != null}</li>
 *  <li>{@code onPopupViewAddToListListeners != null}</li>
 *  <li>{@code onPopupViewGoNowListeners != null}</li>
 *  <li>{@code foreach onPopupViewClosedListener in onPopupViewClosedListeners: onPopupViewClosedListener != null}</li>
 *  <li>{@code foreach onPopupViewAddToListListener in onPopupViewAddToListListeners: onPopupViewAddToListListener != null}</li>
 *  <li>{@code foreach onPopupViewGoNowListener in onPopupViewGoNowListeners: onPopupViewGoNowListener != null}</li>
 *  <li>{@code isOpen} correctly reflects whether the popup is currently visible on the screen.</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link SlidingPopupView} {@code spv} represents a popup UI component that shows
 * details for a single {@link Destination} such that:
 * <ul>
 *  <li>{@code spv.destinationNameTextView} displays the user-facing name of the destination.</li>
 *  <li>{@code spv.destinationDescriptionTextView} displays a textual description of the destination.</li>
 *  <li>{@code spv.destinationBuildingTextView} shows the building where the destination is located.</li>
 *  <li>{@code spv.destinationFloorTextView} shows the floor of the destination.</li>
 *  <li>{@code spv.destinationImageView} displays an image representing the destination.</li>
 *  <li>{@code spv.goNowButton} triggers navigation to the destination when clicked.</li>
 *  <li>{@code spv.addToListButton} adds the destination to a selected list when clicked.</li>
 *  <li>{@code spv.closeImageView} closes the popup when clicked.</li>
 *  <li>{@code spv.onPopupViewClosedListenerList}, {@code spv.onPopupViewGoNowListeners}, {@code spv.onPopupViewAddToListListeners} are the registered callbacks that are notified on the corresponding actions.</li>
 *  <li>{@code spv.isOpen} indicates whether the popup is currently open.</li>
 * </ul>
 * </p>
 */
public class SlidingPopupView extends ConstraintLayout implements PopupView {
    private TextView                                        destinationNameTextView;
    private ImageView                                       closeImageView;
    private Button                                          goNowButton;
    private Button                                          addToListButton;
    private TextView                                        destinationDescriptionTextView;
    private TextView                                        destinationBuildingTextView;
    private TextView                                        destinationFloorTextView;
    private ImageView                                       destinationImageView;
    private boolean                                         isOpen;
    private int                                             openPosition;
    private int                                             closedPosition;
    private final int                                       OPEN_CLOSE_DURATION = 500;                                    /* Duration (in milliseconds) for opening and closing animations */
    private final List<OnPopupViewClosedListener>           onPopupViewClosedListeners;
    private final List<OnPopupViewAddToListListener>        onPopupViewAddToListListeners;
    private final List<OnPopupViewGoNowListener>            onPopupViewGoNowListeners;
    private final List<OnPopupDestinationChangedListener>   onPopupDestinationChangedListeners;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new {@link SlidingPopupView}.</li>
     * </ul>
     * </p>
     * @param context The application's {@link Context}, used to inflate the view and access resources.
     * @param attrs The {@link AttributeSet} containing XML-defined attributes for this view.
     */
    public SlidingPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.onPopupViewClosedListeners = new ArrayList<>();
        this.onPopupViewAddToListListeners = new ArrayList<>();
        this.onPopupViewGoNowListeners = new ArrayList<>();
        this.onPopupDestinationChangedListeners = new ArrayList<>();
        this.isOpen = false;
        this.initializeViews(context);
        this.setOnCloseImageViewClickedListener();
        this.setOnGoNowButtonClickedListener();
        this.setOnAddToListButtonClickedListener();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Opens the {@link SlidingPopupView}. If the {@link SlidingPopupView} is already opened
     *         then changing the displayed {@link Destination}. Does nothing if destination is null.</li>
     * </ul>
     * </p>
     * @param destination The {@link Destination} whose details will be displayed in the popup.
     */
    @Override
    public void open(Destination destination) {
        this.checkRepresentation();
        if (destination != null) {
            this.populate(destination);
            if (!this.isOpen) {
                this.isOpen = true;
                this.animateTranslation(this.closedPosition, this.openPosition, this.OPEN_CLOSE_DURATION);
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
     *  <li><b>effects: </b>Closes the {@link SlidingPopupView}. Does nothing if {@link SlidingPopupView} is already closed.</li>
     * </ul>
     * </p>
     */
    @Override
    public void close() {
        this.checkRepresentation();
        if (!this.isOpen) return;
        else this.isOpen = false;
        this.animateTranslation(this.openPosition, this.closedPosition, this.OPEN_CLOSE_DURATION);
        this.notifyOnPopupViewClosed();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns true iff this {@link PopupView} is open.</li>
     * </ul>
     * </p>
     */
    @Override
    public boolean isOpen() { return this.isOpen; }

    @Override
    public void initializePosition(int widthFactor, int openPosition) {
        this.openPosition = openPosition;
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SlidingPopupView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SlidingPopupView.this.closedPosition = widthFactor * SlidingPopupView.this.getWidth();
                SlidingPopupView.this.setTranslationX(SlidingPopupView.this.closedPosition);
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onPopupViewClosedListener as a listener to be notified when the this {@link SlidingPopupView}
     *         is closed. Does nothing if onPopupViewClosedListener is null</li>
     * </ul>
     * </p>
     * @param onPopupViewClosedListener The listener to add.
     */
    @Override
    public void addOnPopupViewClosedListener(OnPopupViewClosedListener onPopupViewClosedListener) {
        this.checkRepresentation();
        if (onPopupViewClosedListener != null) {
            this.onPopupViewClosedListeners.add(onPopupViewClosedListener);
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onPopupViewGoNowListener as a listener to be notified when the goNow {@link Button}
     *         is clicked. Does nothing if onPopupViewGoNowListener is null</li>
     * </ul>
     * </p>
     * @param onPopupViewGoNowListener The listener to add.
     */
    @Override
    public void addOnPopupViewGoNowListener(OnPopupViewGoNowListener onPopupViewGoNowListener) {
        this.checkRepresentation();
        if (onPopupViewGoNowListener != null) {
            this.onPopupViewGoNowListeners.add(onPopupViewGoNowListener);
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds the onPopupViewAddToListListener as a listener to be notified when the addToList {@link Button}
     *         is clicked. Does nothing if onPopupViewAddToListListener is null</li>
     * </ul>
     * </p>
     * @param onPopupViewAddToListListener The listener to add.
     */
    @Override
    public void addOnPopupViewAddToListListener(OnPopupViewAddToListListener onPopupViewAddToListListener) {
        this.checkRepresentation();
        if (onPopupViewAddToListListener != null) {
            this.onPopupViewAddToListListeners.add(onPopupViewAddToListListener);
        }
        this.checkRepresentation();
    }

    @Override
    public void addOnPopupDestinationChangedListener(OnPopupDestinationChangedListener onPopupDestinationChangedListener) {
        this.checkRepresentation();
        if (onPopupDestinationChangedListener != null) {
            this.onPopupDestinationChangedListeners.add(onPopupDestinationChangedListener);
        }
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Inflates the popup layout and initializes references to all UI components.</li>
     * </ul>
     * </p>
     * @param context The application's context used to inflate the layout.
     */
    private void initializeViews(Context context) {
        /* Inflate the popup screen layout */
        View destinationPopup = LayoutInflater.from(context).inflate(R.layout.destination_popup, this, true);

        this.destinationNameTextView = destinationPopup.findViewById(R.id.DestinationNameTextView);
        this.closeImageView = destinationPopup.findViewById(R.id.CloseImageView);
        this.goNowButton = destinationPopup.findViewById(R.id.GoNowButton);
        this.addToListButton = destinationPopup.findViewById(R.id.AddToListButton);
        this.destinationDescriptionTextView = destinationPopup.findViewById(R.id.DestinationDescriptionTextView);
        this.destinationBuildingTextView = destinationPopup.findViewById(R.id.BuildingTextView);
        this.destinationFloorTextView = destinationPopup.findViewById(R.id.FloorTextView);
        this.destinationImageView = destinationPopup.findViewById(R.id.DestinationImageView);
        this.setVisibility(View.VISIBLE);
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the click listener for closeImageView.</li>
     * </ul>
     * </p>
     */
    private void setOnCloseImageViewClickedListener() {
        this.closeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SlidingPopupView.this.close();
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the click listener for goNowButton.</li>
     * </ul>
     * </p>
     */
    private void setOnGoNowButtonClickedListener() {
        this.goNowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SlidingPopupView.this.notifyOnPopupViewGoNow();
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the click listener for addToListButton.</li>
     * </ul>
     * </p>
     */
    private void setOnAddToListButtonClickedListener() {
        this.addToListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SlidingPopupView.this.notifyOnPopupViewAddToList();
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Notifies all registered {@link OnPopupViewClosedListener}s
     *         that this {@link SlidingPopupView} has been closed.</li>
     * </ul>
     * </p>
     */
    private void notifyOnPopupViewClosed() {
        for (OnPopupViewClosedListener onPopupViewClosedListener : this.onPopupViewClosedListeners) {
            if (onPopupViewClosedListener != null) {
                onPopupViewClosedListener.onPopupViewClosed(this);
            }
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Notifies all registered {@link OnPopupViewAddToListListener}s
     *         that the addToListButton was clicked.</li>
     * </ul>
     * </p>
     */
    private void notifyOnPopupViewAddToList() {
        for (OnPopupViewAddToListListener onPopupViewAddToListListener : this.onPopupViewAddToListListeners) {
            if (onPopupViewAddToListListener != null) {
                onPopupViewAddToListListener.onAddToList(this);
            }
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Notifies all registered {@link OnPopupViewGoNowListener}s
     *         that the goNowButton was clicked.</li>
     * </ul>
     * </p>
     */
    private void notifyOnPopupViewGoNow() {
        for (OnPopupViewGoNowListener onPopupViewGoNowListener : this.onPopupViewGoNowListeners) {
            if (onPopupViewGoNowListener != null) {
                onPopupViewGoNowListener.onGoNow(this);
            }
        }
    }

    private void notifyOnPopupDestinationChanged() {
        for (OnPopupDestinationChangedListener onPopupDestinationChangedListener : this.onPopupDestinationChangedListeners) {
            if (onPopupDestinationChangedListener != null) {
                onPopupDestinationChangedListener.onPopupDestinationChanged(this);
            }
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Updates the displayed information in the popup to show
     *  *       the details of destination. Does nothing if destination is null.</li>
     * </ul>
     * </p>
     */
    private void populate(Destination destination) {
        if (destination != null) {
            this.destinationNameTextView.setText(destination.getDisplayName());
            this.destinationDescriptionTextView.setText(destination.getDescription());
            this.destinationBuildingTextView.setText(destination.getBuilding());
            this.destinationFloorTextView.setText(destination.getFloor());
            this.destinationImageView.setImageResource(destination.getImageID());
            this.notifyOnPopupDestinationChanged();
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code duration >= 0}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Animates the horizontal translation of the view from
     *  *       {@code from} to {@code to} over the given {@code duration} in milliseconds.</li>
     * </ul>
     * </p>
     * @param from     The starting translation value (X-coordinate) of the view.
     * @param to       The ending translation value (X-coordinate) of the view.
     * @param duration The duration of the animation in milliseconds.
     */
    private void animateTranslation(float from, float to, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /* Called for each frame */
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                SlidingPopupView.this.setTranslationX((float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
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
        assert this.destinationNameTextView != null;
        assert this.destinationDescriptionTextView != null;
        assert this.destinationBuildingTextView != null;
        assert this.destinationFloorTextView != null;
        assert this.destinationImageView != null;
        assert this.closeImageView != null;
        assert this.goNowButton != null;
        assert this.addToListButton != null;
        assert this.onPopupViewClosedListeners != null;
        assert this.onPopupViewAddToListListeners != null;
        assert this.onPopupViewGoNowListeners != null;
        for (OnPopupViewClosedListener onPopupViewClosedListener : this.onPopupViewClosedListeners) {
            assert onPopupViewClosedListener != null;
        }
        for (OnPopupViewAddToListListener onPopupViewAddToListListener : this.onPopupViewAddToListListeners) {
            assert onPopupViewAddToListListener != null;
        }
        for (OnPopupViewGoNowListener onPopupViewGoNowListener : this.onPopupViewGoNowListeners) {
            assert onPopupViewGoNowListener != null;
        }
    }
}