package com.example.volt.search;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import com.example.volt.commnad.Command;
import com.example.volt.commnad.DestinationSelectedCommand;
import com.example.volt.map.MapManager;
import com.example.volt.model.Destination;
import com.example.volt.popup.PopupManager;
import java.util.List;

/**
 * <h6>
 * {@link DestinationsSearchManager} is a link layer between the UI of the search mechanism and the other managers.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code searchView != null}</li>
 *  <li>{@code listView != null}</li>
 *  <li>{@code adapter != null}</li>
 *  <li>{@code destinationSelectedCommand != null }</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link DestinationsSearchManager} {@code dsm} represents a link between the UI of the search mechanism and the
 * other managers such that:
 * <ul>
 *  <li>{@code dsm.searchView} represents the {@link SearchView} that accepts queries.</li>
 *  <li>{@code dsm.listView} represents the {@link ListView} displaying all destinations or a filtered list according to the search query.</li>
 *  <li>{@code dsm.adapter} represents the bridge between the destination data and the {@code listView}, providing filtering and display logic.</li>
 *  <li>{@code dsm.destinationSelectedCommand} represents the {@link Command} to execute when a {@link Destination} is selected from the list.</li>
 * </ul>
 * </p>
 */
public class DestinationsSearchManager {
    private SearchView searchView;
    private ListView listView;
    private DestinationsSearchAdapter adapter;
    private Command<Destination> destinationSelectedCommand;


    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code context != null}, {@code destinationsSearchView != null},
     *                       {@code destinationsListView != null}, {@code allDestinations != null},
     *                       {@code mapManager != null}, {@code popupManager != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new manager.</li>
     * </ul>
     * </p>
     * @param context                   The {@link Context} of the app.
     * @param destinationsSearchView    The {@link SearchView}.
     * @param destinationsListView      The {@link ListView} of all {@link Destination}s.
     * @param allDestinations           The collection of all {@link Destination}s.
     * @param mapManager                The {@link MapManager}.
     * @param popupManager              The {@link PopupManager}.
     */
    public DestinationsSearchManager(Context context, SearchView destinationsSearchView,
                                     ListView destinationsListView, List<Destination> allDestinations,
                                     MapManager mapManager, PopupManager popupManager) {
        this.initialize(context, destinationsSearchView, destinationsListView, allDestinations, mapManager, popupManager);

        /* Defines searching and clicking behavior */
        this.setupSearchBehavior();
        this.setupItemClickBehavior();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns true iff the point is within the search view's boundaries.</li>
     * </ul>
     * </p>
     * @param x The x coordinate of the search bar.
     * @param y The y coordinate of the search bar.
     */
    public boolean isPointInsideSearch(double x, double y) {
        this.checkRepresentation();
        int[] location = new int[2];
        this.searchView.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + this.searchView.getWidth();
        int bottom = top + this.searchView.getHeight();
        this.checkRepresentation();
        return (x >= left && x <= right && y >= top && y <= bottom);
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Removes the focus from the search bar.</li>
     * </ul>
     * </p>
     */
    public void removeFocus() {
        this.checkRepresentation();
        this.searchView.clearFocus();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code context != null}, {@code destinationsSearchView != null},
     *                       {@code destinationsListView != null}, {@code allDestinations != null},
     *                       {@code mapManager != null}, {@code popupManager != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Initializes the member variables of {@code this}.</li>
     * </ul>
     * </p>
     */
    private void initialize(Context context, SearchView destinationsSearchView,
                            ListView destinationsListView, List<Destination> allDestinations,
                            MapManager mapManager, PopupManager popupManager) {
        this.searchView = destinationsSearchView;
        this.listView = destinationsListView;
        /* Creates the adapter with the destinations list as a dataset */
        this.adapter = new DestinationsSearchAdapter(context, allDestinations);
        this.listView.setAdapter(this.adapter);
        this.destinationSelectedCommand = new DestinationSelectedCommand(mapManager, popupManager);
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds listeners to search-related events.</li>
     * </ul>
     * </p>
     */
    private void setupSearchBehavior() {
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /* Handles submitting */
            @Override
            public boolean onQueryTextSubmit(String s) {
                /* Does nothing and returns false to indicate that someone else should handle this event */
                return false;
            }

            /* Handles changes */
            @Override
            public boolean onQueryTextChange(String s) {
                /* Filters the list by the query */
                DestinationsSearchManager.this.adapter.getFilter().filter(s);
                return true;
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Adds listeners to click-related events.</li>
     * </ul>
     * </p>
     */
    private void setupItemClickBehavior() {
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Destination selected = DestinationsSearchManager.this.adapter.getItem(position);
                if (selected != null) {
                    DestinationsSearchManager.this.destinationSelectedCommand.execute(selected);
                }
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
        assert this.searchView != null;
        assert this.listView != null;
        assert this.adapter != null;
        assert this.destinationSelectedCommand != null;
    }
}