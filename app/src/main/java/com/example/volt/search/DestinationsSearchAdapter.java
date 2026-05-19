package com.example.volt.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import com.example.volt.model.Destination;
import com.example.volt.R;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <h6>
 * An adapter that manages a list of {@link Destination}s objects for display in a {@link ListView} based on
 * a filter.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code originalDestinations != null}</li>
 *  <li>{@code filteredDestinations != null}</li>
 *  <li>{@code foreach element e in filteredDestinations: e is in originalDestinations}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@code DestinationsSearchAdapter} {@code dsa} represents a bridge that links {@link Destination}s to the
 * {@link ListView} such that:
 * <ul>
 *  <li>{@code dsa.originalDestinations} represents the collection of all {@link Destination}s.</li>
 *  <li>{@code dsa.filteredDestinations} represents a collection of {@link Destination}s after applying a filter.</li>
 * </ul>
 * </p>
 */
public class DestinationsSearchAdapter extends ArrayAdapter<Destination> {

    private final List<Destination> originalDestinations;
    private List<Destination> filteredDestinations;

    /**
     * <h6>
     * A {@link ViewHolder} is a visual representation of a single
     * element in the {@link DestinationsSearchAdapter}'s dataset.
     * </h6>
     * <p>
     * <p><b>Representation Invariant:</b></p>
     * <ul>
     *  <li>{@code listViewItemTextView != null}</li>
     * </ul>
     * </p>
     * <p>
     * <p><b>Abstraction Function:</b></p>
     * {@link ViewHolder} {@code vh} represents a {@link View} that displays a single destination in
     * the {@link DestinationsSearchAdapter}'s dataset such that:
     * <ul>
     *  <li>{@code vh.listViewItemTextView} displays destination's name</li>
     * </ul>
     * </p>
     */
    public static class ViewHolder {
        TextView listViewItemTextView;

        /**
         * <h5>Specification:</h5>
         * <p>
         * <ul>
         *  <li><b>requires: </b>{@code itemView} != null</li>
         *  <li><b>modifies: </b>this</li>
         *  <li><b>effects: </b>Creates a new {@link ViewHolder}.</li>
         * </ul>
         * </p>
         * @param itemView The root view of a single item in the {@link ListView}.
         */
        public ViewHolder(View itemView) {
            this.listViewItemTextView = itemView.findViewById(R.id.ListViewItemTextView);
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code dataSet} != null</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new {@link DestinationsSearchAdapter} with a copy of {@code destinations} as its dataset.</li>
     * </ul>
     * </p>
     * @param context The context of the app.
     * @param destinations The collection of {@link Destination}s.
     */
    public DestinationsSearchAdapter(Context context, List<Destination> destinations) {
        super(context, 0, destinations);

        /* Initializes the member variables */
        this.originalDestinations = new ArrayList<>(destinations);
        /* Sorts the original list */
        this.originalDestinations.sort(Comparator.comparing(Destination::getDisplayName, String.CASE_INSENSITIVE_ORDER));
        /* The initial filtered list is full */
        this.filteredDestinations = new ArrayList<>(this.originalDestinations);
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code position >= 0 && position < this.filteredDestinations.size()},
     *                       {@code parent != null}</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>If {@code convertView} is null, inflates a new view and creates a new {@link ViewHolder} attached to it.
     *                      If {@code convertView} is not null, reuses the existing {@link ViewHolder}.</li>
     * </ul>
     * </p>
     * @param position          The position of the item within the dataset that is currently being bound to a view.
     * @param convertView       An existing View to reuse, or null if a new one needs to be created.
     * @param parent            The parent ViewGroup that holds the current item.
     * @apiNote This function converts XML to a {@link ViewHolder}
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.checkRepresentation();
        ViewHolder viewHolder;

        /* Checks if there's an existing view we can reuse */
        if (convertView == null) {
            /* Inflates a new view with the list_view_item layout */
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);

            /* Creates a new ViewHolder and bind it to the new view */
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            /* Reuses the existing ViewHolder */
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /* Sets the values of the current item */
        Destination currentDestination = getItem(position);
        viewHolder.listViewItemTextView.setText(currentDestination.getDisplayName());
        viewHolder.listViewItemTextView.setTag(currentDestination.getInternalName());

        /* Returns the prepared view */
        return convertView;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the number of displayed elements.</li>
     * </ul>
     * </p>
     */
    @Override
    public int getCount() {
        this.checkRepresentation();
        return filteredDestinations.size();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Gets the element displayed at position.</li>
     * </ul>
     * </p>
     */
    @Override
    public Destination getItem(int position) {
        this.checkRepresentation();
        return filteredDestinations.get(position);
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: Returns a {@link Filter} that can be used to filter the dataset of destinations
     *                  based on a text query</b></li>
     * </ul>
     * </p>
     */
    @NonNull
    @Override
    public Filter getFilter() {
        this.checkRepresentation();
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                DestinationsSearchAdapter.this.checkRepresentation();
                FilterResults results = new FilterResults();

                /* If no constraint is provided, returns the original list */
                if (constraint == null || constraint.length() == 0) {
                    results.count = originalDestinations.size();
                    results.values = originalDestinations;
                }

                else {
                    /* Performs filtering based on the constraint */
                    List<Destination> filteredList = new ArrayList<>();
                    for (Destination destination : originalDestinations) {
                        if (destination.getDisplayName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(destination);
                        }
                    }
                    results.count = filteredList.size();
                    results.values = filteredList;
                }

                DestinationsSearchAdapter.this.checkRepresentation();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                DestinationsSearchAdapter.this.checkRepresentation();
                /* Updates the filtered list and notifies the adapter */
                filteredDestinations = (List<Destination>) results.values;
                notifyDataSetChanged();
                DestinationsSearchAdapter.this.checkRepresentation();
            }
        };
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
        assert this.originalDestinations != null;
        assert this.filteredDestinations != null;
        for (Destination destination : this.filteredDestinations) {
            assert originalDestinations.contains(destination);
        }
    }
}