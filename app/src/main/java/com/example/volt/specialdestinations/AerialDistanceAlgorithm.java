package com.example.volt.specialdestinations;

import android.widget.ImageView;

import com.example.volt.map.DestinationMarkerView;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.navigation.model.SpeedLevel;
import java.util.List;

public class AerialDistanceAlgorithm implements SpecialDestinationAlgorithm {

    @Override
    public void navigate(List<DestinationMarkerView> candidates, ImageView currentPositionImageView) {
        DestinationMarkerView nearest = null;
        double minSquaredDistance = Double.MAX_VALUE;
        double currentX = currentPositionImageView.getX();
        double currentY = currentPositionImageView.getY();
        for (DestinationMarkerView destinationMarkerView : candidates) {
            float x = destinationMarkerView.getView().getX();
            float y = destinationMarkerView.getView().getY();
            double dx = x - currentX;
            double dy = y - currentY;
            double squareDist = dx * dx + dy * dy;
            if (squareDist < minSquaredDistance) {
                minSquaredDistance = squareDist;
                nearest = destinationMarkerView;
            }
        }

        if (nearest != null) {
            Robot.getInstance().goTo(nearest.getInternalName(), true, false, SpeedLevel.HIGH);
        }
    }
}