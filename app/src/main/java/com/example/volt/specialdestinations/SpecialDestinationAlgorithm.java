package com.example.volt.specialdestinations;

import android.widget.ImageView;

import com.example.volt.map.DestinationMarkerView;
import java.util.List;

public interface SpecialDestinationAlgorithm {
    void navigate(List<DestinationMarkerView> candidates, ImageView currentPositionImageView);
}