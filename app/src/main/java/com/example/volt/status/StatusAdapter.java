package com.example.volt.status;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volt.R;
import com.example.volt.status.message.BatteryMessage;
import com.example.volt.status.message.Message;
import com.robotemi.sdk.BatteryData;
import com.robotemi.sdk.Robot;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {
    private List<Message>               dataset;
    private static final int            DEFAULT_BATTERY = 100;

    /**
     * <h6>
     * A {@link ViewHolder} is a visual representation of a single
     * element in the {@link StatusAdapter}'s dataset.
     * </h6>
     * <p>
     * <p><b>Representation Invariant:</b></p>
     * <ul>
     *  <li>{@code constraintLayout != null}</li>
     *  <li>{@code textView != null}</li>
     *  <li>{@code imageView != null}</li>
     * </ul>
     * </p>
     * <p>
     * <p><b>Abstraction Function:</b></p>
     * {@link ViewHolder} {@code vh} represents a {@link View} that displays a single message in
     * the {@link StatusAdapter}'s dataset such that:
     * <ul>
     *  <li>{@code vh.constraintLayout} is a container for the entire item view</li>
     *  <li>{@code vh.textView} displays the message's text</li>
     *  <li>{@code vh.imageView} displays the messages' image</li>
     * </ul>
     * </p>
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        TextView         textView;
        ImageView        imageView;

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
            this.constraintLayout   = itemView.findViewById(R.id.WarningsRecyclerViewConstraintLayout);
            this.textView           = itemView.findViewById(R.id.WarningsRecyclerViewWarningTextView);
            this.imageView          = itemView.findViewById(R.id.WarningRecyclerViewImageView);
        }
    }

    public StatusAdapter(List<Message> dataset) {
        super();
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.warnings_recycler_view_item, parent, false);
        return new StatusAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapter.ViewHolder viewHolder, int position) {
        Message message = dataset.get(position);
        viewHolder.imageView.setImageResource(message.getIcon());
        int backgroundColor = viewHolder.itemView.getContext().getColor(message.getColor());
        ((GradientDrawable)(viewHolder.constraintLayout.getBackground())).setColor(backgroundColor);

        String text;
        if (message.getKey().equals(BatteryMessage.KEY)) {
            int batteryLevel = this.getBatteryPercentage();
            String batteryText = viewHolder.itemView.getContext().getString(message.getText());
            text = String.format(batteryText, batteryLevel);
        }
        else {
            text = viewHolder.itemView.getContext().getString(message.getText());
        }
        viewHolder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

    private int getBatteryPercentage() {
        BatteryData batteryData = Robot.getInstance().getBatteryData();
        if (batteryData != null) {
            return batteryData.getBatteryPercentage();
        }
        else {
            return StatusAdapter.DEFAULT_BATTERY;
        }
    }
}