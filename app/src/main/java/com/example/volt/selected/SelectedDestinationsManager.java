package com.example.volt.selected;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView;
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener;
import com.example.volt.commnad.Command;
import com.example.volt.model.Destination;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.navigation.model.SpeedLevel;

import java.util.List;

/**
 * <h6>
 * {@link SelectedDestinationsManager} is a link layer between {@link DestinationCollection} and
 * {@link SelectedDestinationsAdapter}.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code recyclerView != null}</li>
 *  <li>{@code recyclerViewAdapter != null}</li>
 *  <li>{@code destinationCollection != null}</li>
 *  <li>{@code goButton != null}</li>
 *  <li>{@code goButton.enabled iff !destinationCollection.isEmpty()}</li>
 *  <li>{@code destinationCollection.getDestinations == recyclerViewAdapter.getDataSet()}</li>
 *  <li>{@code goToCommand != null}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link SelectedDestinationsManager} {@code sdm} represents a link between {@link DestinationCollection} and
 * {@link SelectedDestinationsAdapter} such that:
 * <ul>
 *  <li>{@code sdm.recyclerView} is the {@link DragDropSwipeRecyclerView} that displays {@link Destination}s.</li>
 *  <li>{@code sdm.recyclerViewAdapter} is the {@link SelectedDestinationsAdapter} that links between the {@link DestinationCollection} and {@link DragDropSwipeRecyclerView}.</li>
 *  <li>{@code sdm.destinationCollection} is the collection of {@link Destination}s to display.</li>
 *  <li>{@code sdm.goButton} is the {@link Button} that starts the robot's movement.</li>
 *  <li>{@code sdm.goToCommand} is the {@link Command} that represents the operation of sending the robot to a location.</li>
 * </ul>
 * </p>
 */
public class SelectedDestinationsManager implements OnItemDraggedListener,
                                                    OnItemAddedListener,
                                                    OnGoToLocationStatusChangedListener {
    private DragDropSwipeRecyclerView   recyclerView;
    private SelectedDestinationsAdapter recyclerViewAdapter;
    private DestinationCollection       destinationCollection;
    private Button                      goButton;
    private Command<String>             goToCommand;
    private boolean                     isNavigatingFromList;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code recyclerView != null}, {@code DestinationCollection != null}, {@code goButton != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new manager.</li>
     * </ul>
     * </p>
     * @param recyclerView          The {@link androidx.recyclerview.widget.RecyclerView} displaying the selected {@link Destination}s.
     * @param destinationCollection The The collection of selected {@link Destination}s.
     * @param goButton              The {@link Button} to start to start navigation.
     * @param goToCommand           The {@link Command} that represents the operation of sending the robot to a location.
     */
    public SelectedDestinationsManager(DragDropSwipeRecyclerView recyclerView,
                                       DestinationCollection destinationCollection,
                                       Button goButton,
                                       Command<String> goToCommand) {
        this.initialize(recyclerView, destinationCollection, goButton, goToCommand);
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Update the dataset by moving the item at {@code initialPosition} to {@code finalPosition}.</li>
     * </ul>
     * </p>
     * @param initialPosition   The initial position of the item.
     * @param finalPosition     The new position of the item.
     */
    @Override
    public void onItemDragged(int initialPosition, int finalPosition) {
        this.destinationCollection.updateDestinationPosition(initialPosition, finalPosition);
        this.updateGoButtonState();
        this.checkRepresentation();
    }

    @Override
    public void onItemAdded(Destination destination) {
        this.recyclerViewAdapter.insertItem(this.recyclerViewAdapter.getItemCount(), destination);
        this.updateGoButtonState();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>A callback that is called each time the go to status changes. Removes the next destination
     *         from the collection.</li>
     * </ul>
     * </p>
     * @param location      The name of the location TEMI is navigating to.
     * @param status        Navigation status (start, calculating, going, complete, abort, reposing).
     * @param descriptionID Numerical code that reflects the description of the status.
     * @param description   Verbose more informative description of the navigation status (such as obstacle info).
     */
    @Override
    public void onGoToLocationStatusChanged(@NonNull String location, @NonNull String status, int descriptionID, @NonNull String description) {
        if (status.equals(COMPLETE) && this.isNavigatingFromList) {
            this.destinationCollection.removeNextDestination();
            this.recyclerViewAdapter.removeItem(0);
            this.updateGoButtonState();
        }
        else if (status.equals(ABORT)) {
            this.isNavigatingFromList = false;
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Updates the state of the goButton.</li>
     * </ul>
     * </p>
     */
    private void updateGoButtonState() {
        /* The goButton is enabled iff destinationCollection isn't empty */
        this.goButton.setEnabled(!this.destinationCollection.isEmpty());
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code recyclerView != null}, {@code DestinationCollection != null}, {@code goButton != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Initializes the member variables of this.</li>
     * </ul>
     * </p>
     * @param recyclerView          The {@link androidx.recyclerview.widget.RecyclerView} displaying the selected {@link Destination}s.
     * @param destinationCollection The The collection of selected {@link Destination}s.
     * @param goButton              The {@link Button} to start to start navigation.
     * @param goToCommand           The {@link Command} that represents the operation of sending the robot to a location.
     */
    private void initialize(DragDropSwipeRecyclerView recyclerView,
                            DestinationCollection destinationCollection,
                            Button goButton,
                            Command<String> goToCommand) {
        this.recyclerView = recyclerView;
        this.destinationCollection = destinationCollection;
        this.goButton = goButton;
        this.goToCommand = goToCommand;
        this.isNavigatingFromList = false;

        this.recyclerViewAdapter = new SelectedDestinationsAdapter(this.destinationCollection.getAllDestinations());
        /* Sets a linear layout manager for arranging items in a vertical list */
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.recyclerView.getContext()));
        /* Assigns the adapter to the RecyclerView */
        this.recyclerView.setAdapter(recyclerViewAdapter);
        /* Sets the orientation for dragging items vertically */
        this.recyclerView.setOrientation(DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING);
        /* Adds a callback to be called when an item is dragged */
        this.recyclerViewAdapter.setOnItemDraggedListener(this);

        this.setOnSwipeListener();
        this.setOnGoButtonClickedListener();
        this.updateGoButtonState();
    }

    private void setOnSwipeListener() {
        this.recyclerView.setSwipeListener(new OnItemSwipeListener<Destination>() {
            @Override
            public boolean onItemSwiped(int position, @NonNull OnItemSwipeListener.SwipeDirection direction, Destination item) {
                SelectedDestinationsManager.this.destinationCollection.removeDestinationAt(position);
                SelectedDestinationsManager.this.updateGoButtonState();
                /* Returns false to let the adapter remove the item */
                return false;
            }
        });
    }

    private void setOnGoButtonClickedListener() {
        this.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Destination target = SelectedDestinationsManager.this.destinationCollection.getNextDestination();
                SelectedDestinationsManager.this.goToCommand.execute(target.getInternalName());
                SelectedDestinationsManager.this.isNavigatingFromList = true;
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
        assert this.recyclerView != null;
        assert this.recyclerViewAdapter != null;
        assert this.destinationCollection != null;
        assert this.goButton != null;
        assert this.goButton.isEnabled() == !destinationCollection.isEmpty();
        List<Destination> collection = this.destinationCollection.getAllDestinations();
        List<Destination> adapter = this.recyclerViewAdapter.getDataSet();
        assert collection.size() == adapter.size();
        for (int i = 0; i < collection.size(); i++) {
            assert collection.get(i).equals(adapter.get(i));
        }
    }
}