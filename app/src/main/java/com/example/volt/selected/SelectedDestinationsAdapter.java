/******************************************************************************
 * @file   SelectedDestinationsManager.java
 * @brief  The DestinationsRecyclerViewAdapter is a custom adapter that extends the DragDropSwipeAdapter
 *         to manage a dataset of destinations in a RecyclerView. This adapter provides the functionality
 *         for displaying destinations with a draggable and swipe-to-remove feature. Each item in the
 *         RecyclerView represents a destination, and users can drag items to reorder them or swipe to
 *         remove them. The items are styled with alternating background colors and can be interacted with using
 *         a dedicated drag handle.
 *         The class also handles the creation and binding of ViewHolders, which manage the display of
 *         each destination's name and the visual interaction elements like the drag handle. The adapter
 *         listens for swipe gestures, and when an item is swiped, it notifies an external listener about
 *         the removal of the item.
 *
 * @author Itay
 * @date   October 2024
 *****************************************************************************/

/* Project package */
package com.example.volt.selected;

/* Import statements */
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter;
import com.example.volt.model.Destination;
import com.example.volt.R;

import java.util.List;

/**
 * <h6>
 * An adapter that manages a list of {@link com.example.volt.model.Destination} objects for display
 * in a {@link androidx.recyclerview.widget.RecyclerView} with drag-and-drop reordering
 * and swipe-to-remove support.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>None</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@code SelectedDestinationsAdapter} {@code sda} represents a link between the collection of {@link com.example.volt.model.Destination}
 * to the {@link android.view.View} that displays them such that:
 * <ul>
 *  <li>{@code sda.onItemRemovedBySwipingListener} is an object that is notified when a {@link android.view.View} is removed by swiping</li>
 * </ul>
 * </p>
 */
public class SelectedDestinationsAdapter extends DragDropSwipeAdapter<Destination, SelectedDestinationsAdapter.ViewHolder> {

    private static int currentColor = 0;
    private static final List<Integer> COLORS = List.of(
            R.color.pastel_blue,
            R.color.pastel_pink,
            R.color.pastel_orange,
            R.color.pastel_light_orange,
            R.color.pastel_yellow);
    private OnItemRemovedBySwipingListener onItemRemovedBySwipingListener;
    private OnItemDraggedListener          onItemDraggedListener;
    private int initialDragPosition;

    /**
     * <h6>
     * A {@link ViewHolder} is a visual representation of a single
     * element in the {@link SelectedDestinationsAdapter}'s dataset.
     * </h6>
     * <p>
     * <p><b>Representation Invariant:</b></p>
     * <ul>
     *  <li>{@code destinationItemConstraintLayout != null}</li>
     *  <li>{@code destinationItemTextView != null}</li>
     *  <li>{@code destinationItemDragHandleImageView != null}</li>
     * </ul>
     * </p>
     * <p>
     * <p><b>Abstraction Function:</b></p>
     * {@link ViewHolder} {@code vh} represents a {@link View} that displays a single destination in
     * the {@link SelectedDestinationsAdapter}'s dataset such that:
     * <ul>
     *  <li>{@code vh.destinationItemConstraintLayout} is a container for the entire item view</li>
     *  <li>{@code vh.destinationItemTextView} displays destination's name</li>
     *  <li>{@code vh.destinationItemDragHandleImageView} is a handle for dragging gesture</li>
     * </ul>
     * </p>
     */
    public static class ViewHolder extends DragDropSwipeAdapter.ViewHolder {
        ConstraintLayout destinationItemConstraintLayout;
        TextView destinationItemTextView;
        ImageView destinationItemDragHandleImageView;

        /**
         * <h5>Specification:</h5>
         * <p>
         * <ul>
         *  <li><b>requires: </b>{@code itemView} != null</li>
         *  <li><b>modifies: </b>this</li>
         *  <li><b>effects: </b>Creates a new {@link ViewHolder}.</li>
         * </ul>
         * </p>
         * @param itemView The root view of a single item in the {@link RecyclerView}.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            this.destinationItemConstraintLayout = itemView.findViewById(R.id.DestinationsRecyclerViewItemConstraintLayout);
            this.destinationItemTextView = itemView.findViewById(R.id.DestinationsRecyclerViewItemTextView);
            this.destinationItemDragHandleImageView = itemView.findViewById(R.id.DestinationsRecyclerViewItemDragHandleImageView);
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code dataSet} != null</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new {@link SelectedDestinationsAdapter} with a copy of {@code dataSet} as its dataset.</li>
     * </ul>
     * </p>
     * @param dataSet The collection of {@link Destination}s to display.
     */
    public SelectedDestinationsAdapter(List<Destination> dataSet) {
        super(dataSet);
        this.initialDragPosition = RecyclerView.NO_POSITION;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code itemLayout} != null</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Creates a new {@link ViewHolder} that wraps the given {@code itemLayout}.</li>
     * </ul>
     * </p>
     * @param itemLayout The view representing the layout of a single item in the {@link RecyclerView}.
     * @apiNote This function converts XML to a {@link ViewHolder}
     */
    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View itemLayout) {
        return new ViewHolder(itemLayout);
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code item} != null, {@code viewHolder} != null</li>
     *  <li><b>modifies: </b>viewHolder</li>
     *  <li><b>effects: </b>Binds the data of {@code item} to the corresponding views
     *         in {@code viewHolder} and updates the background color of the item.</li>
     * </ul>
     * </p>
     * @param item       The {@link Destination} item containing the data to be displayed.
     * @param viewHolder The {@link ViewHolder} holding the views for this item.
     * @param position   The position of the item in the dataset.
     */
    @Override
    public void onBindViewHolder(Destination item, ViewHolder viewHolder, int position) {
        viewHolder.destinationItemConstraintLayout.setBackgroundColor(viewHolder.itemView.getContext().getColor(COLORS.get(currentColor)));
        currentColor = (currentColor + 1) % COLORS.size();
        viewHolder.destinationItemTextView.setText(item.getDisplayName());
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code item} != null, {@code viewHolder} != null</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the view inside {@code viewHolder} that should be
     *         touched to initiate a drag operation for {@code item}.</li>
     * </ul>
     * </p>
     * @param item       The {@link Destination} item being dragged.
     * @param viewHolder The {@link ViewHolder} holding the views for this item.
     * @param position   The position of the item in the dataset.
     */
    @Override
    public View getViewToTouchToStartDraggingItem(Destination item, ViewHolder viewHolder, int position) {
        return viewHolder.destinationItemDragHandleImageView;
    }

    @Override
    protected void onDragStarted(@NonNull Destination item, @NonNull ViewHolder viewHolder) {
        super.onDragStarted(item, viewHolder);
        this.initialDragPosition = viewHolder.getAbsoluteAdapterPosition();
    }

    @Override
    protected void onDragFinished(@NonNull Destination item, @NonNull ViewHolder viewHolder) {
        super.onDragFinished(item, viewHolder);
        if (onItemDraggedListener != null && this.initialDragPosition != RecyclerView.NO_POSITION) {
            int finalDragPosition = viewHolder.getAbsoluteAdapterPosition();
            this.onItemDraggedListener.onItemDragged(this.initialDragPosition, finalDragPosition);
            this.initialDragPosition = RecyclerView.NO_POSITION;
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the listener to be notified when an item is removed from the dataset by swiping.
     *                      Does nothing if onItemRemovedBySwipingListener is null.</li>
     * </ul>
     * </p>
     *
     * @param onItemRemovedBySwipingListener The listener to notify when an item is removed by swiping.
     */
    public void setOnItemRemovedListener(OnItemRemovedBySwipingListener onItemRemovedBySwipingListener) {
        if (onItemRemovedBySwipingListener != null) {
            this.onItemRemovedBySwipingListener = onItemRemovedBySwipingListener;
        }
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the listener to be notified when an item is dragged
     *                      Does nothing if onItemDraggedListener is null.</li>
     * </ul>
     * </p>
     *
     * @param onItemDraggedListener The listener to notify when an item is dragged.
     */
    public void setOnItemDraggedListener(OnItemDraggedListener onItemDraggedListener) {
        if (onItemDraggedListener != null) {
            this.onItemDraggedListener = onItemDraggedListener;
        }
    }
}