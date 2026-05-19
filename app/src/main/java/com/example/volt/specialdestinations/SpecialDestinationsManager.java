package com.example.volt.specialdestinations;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import com.example.volt.map.DestinationMarkerView;
import com.robotemi.sdk.Robot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <h6>
 * {@link SpecialDestinationsManager} manages navigation to a set of predefined "special destinations". It's a link layer
 * between the {@link ImageView}s that triggers the navigation and the {@link Robot} class that implements the navigation
 * algorithm.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code nearestMenToilet != null}</li>
 *  <li>{@code nearestWomenToilet != null}</li>
 *  <li>{@code nearestElevator != null}</li>
 *  <li>{@code navigationAlgorithm != null}</li>
 *  <li>{@code allLocations != null}</li>
 *  <li>{@code foreach location in allLocations: location != null}</li>
 *  <li>{@code menFilter != null}</li>
 *  <li>{@code womenFilter != null}</li>
 *  <li>{@code elevatorFilter != null}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link SpecialDestinationsManager} {@code sdm} represents a link between {@link ImageView}s and {@link Robot} such that:
 * <ul>
 *  <li>{@code sdm.nearestMenToilet} is the {@link ImageView} that triggers navigation to the nearest men's toilet.</li>
 *  <li>{@code sdm.nearestWomenToilet} is the {@link ImageView} that triggers navigation to the nearest women's toilet.</li>
 *  <li>{@code sdm.nearestElevator} is the {@link ImageView} that triggers navigation to the nearest elevator.</li>
 *  <li>{@code sdm.navigationAlgorithm} is a shortest path algorithm.</li>
 *  <li>{@code sdm.allLocations} is a collection of all locations on the map.</li>
 * </ul>
 * </p>
 */
public class SpecialDestinationsManager {
    private ImageView                           nearestMenToilet;
    private ImageView                           nearestWomenToilet;
    private ImageView                           nearestElevator;
    private ImageView                           nearestShelter;
    private SpecialDestinationAlgorithm         navigationAlgorithm;
    private Collection<DestinationMarkerView>   allDestinationViews;
    private ImageView                           currentPositionImageView;
    private static final String                 menFilter           = "men";
    private static final String                 womenFilter         = "women";
    private static final String                 elevatorFilter      = "elevator";
    private static final String                 shelterFilter       = "shelter";

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code nearestMenToilet != null},
     *                       {@code nearestWomenToilet != null},
     *                       {@code nearestElevator != null},
     *                       {@code navigationAlgorithm != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new manager.</li>
     * </ul>
     * </p>
     * @param nearestMenToilet          The {@link ImageView} that triggers navigation to the nearest men's toilet.
     * @param nearestWomenToilet        The {@link ImageView} that triggers navigation to the nearest women's toilet.
     * @param nearestElevator           The {@link ImageView} that triggers navigation to the nearest elevator toilet.
     * @param navigationAlgorithm       The shortest path algorithm.
     */
    public SpecialDestinationsManager(ImageView nearestMenToilet,
                                      ImageView nearestWomenToilet,
                                      ImageView nearestElevator,
                                      ImageView nearestShelter,
                                      Collection<DestinationMarkerView> allDestinationViews,
                                      ImageView currentPositionView,
                                      SpecialDestinationAlgorithm navigationAlgorithm) {
        this.initialize(nearestMenToilet,
                        nearestWomenToilet,
                        nearestElevator,
                        nearestShelter,
                        allDestinationViews,
                        currentPositionView,
                        navigationAlgorithm);
        this.setOnMenToiletClickListener();
        this.setOnWomenToiletClickListener();
        this.setOnElevatorClickListener();
        this.setOnShelterClickListener();
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the callback that is called each time the nearest men's toilet is clicked.
     *                      The function finds the nearest toilet using the shortest path algorithm and navigates towards it.</li>
     * </ul>
     * </p>
     */
    private void setOnMenToiletClickListener() {
        this.nearestMenToilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<DestinationMarkerView> menToilet = SpecialDestinationsManager.this.markerFilter(menFilter);
                SpecialDestinationsManager.this.navigationAlgorithm.navigate(menToilet, SpecialDestinationsManager.this.currentPositionImageView);
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the callback that is called each time the nearest women's toilet is clicked.
     *                      The function finds the nearest toilet using the shortest path algorithm and navigates towards it.</li>
     * </ul>
     * </p>
     */
    private void setOnWomenToiletClickListener() {
        this.nearestWomenToilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<DestinationMarkerView> womenToilet = SpecialDestinationsManager.this.markerFilter(womenFilter);
                SpecialDestinationsManager.this.navigationAlgorithm.navigate(womenToilet, SpecialDestinationsManager.this.currentPositionImageView);
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Sets the callback that is called each time the nearest elevator is clicked.
     *                      The function finds the nearest elevator using the shortest path algorithm and navigates towards it.</li>
     * </ul>
     * </p>
     */
    private void setOnElevatorClickListener() {
        this.nearestElevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<DestinationMarkerView> elevator = SpecialDestinationsManager.this.markerFilter(elevatorFilter);
                SpecialDestinationsManager.this.navigationAlgorithm.navigate(elevator, SpecialDestinationsManager.this.currentPositionImageView);
            }
        });
    }

    private void setOnShelterClickListener() {
        this.nearestShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<DestinationMarkerView> shelter = SpecialDestinationsManager.this.markerFilter(shelterFilter);
                SpecialDestinationsManager.this.navigationAlgorithm.navigate(shelter, SpecialDestinationsManager.this.currentPositionImageView);
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns a filtered list of locations based on a keyword.</li>
     * </ul>
     * </p>
     */
    private List<DestinationMarkerView> markerFilter(String filter) {
        List<DestinationMarkerView> result = new ArrayList<>();
        for (DestinationMarkerView destinationMarkerView : this.allDestinationViews) {
            if (destinationMarkerView.getInternalName().toLowerCase().contains(filter)) {
                result.add(destinationMarkerView);
            }
        }
        return result;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code nearestMenToilet != null},
     *                       {@code nearestWomenToilet != null},
     *                       {@code nearestElevator != null},
     *                       {@code navigationAlgorithm != null}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Initializes the member variables of this.</li>
     * </ul>
     * </p>
     */
    private void initialize(ImageView nearestMenToilet,
                            ImageView nearestWomenToilet,
                            ImageView nearestElevator,
                            ImageView nearestShelter,
                            Collection<DestinationMarkerView> allDestinationViews,
                            ImageView currentPositionView,
                            SpecialDestinationAlgorithm navigationAlgorithm) {
        this.nearestMenToilet           = nearestMenToilet;
        this.nearestWomenToilet         = nearestWomenToilet;
        this.nearestElevator            = nearestElevator;
        this.nearestShelter             = nearestShelter;
        this.allDestinationViews        = allDestinationViews;
        this.currentPositionImageView   = currentPositionView;
        this.navigationAlgorithm        = navigationAlgorithm;
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
        assert this.nearestMenToilet != null;
        assert this.nearestWomenToilet != null;
        assert this.nearestElevator != null;
        assert this.navigationAlgorithm != null;
        assert this.allDestinationViews != null;
        for (DestinationMarkerView destinationMarkerView : this.allDestinationViews) {
            assert destinationMarkerView != null;
        }
    }
}