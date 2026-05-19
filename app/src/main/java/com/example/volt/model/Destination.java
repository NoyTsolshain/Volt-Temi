package com.example.volt.model;

import androidx.annotation.Nullable;

/**
 * <h6>
 * {@link Destination} represents an immutable location in the robot's mapping space.
 * </h6>
 * <p>
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *  <li>{@code destinationDisplay != null}</li>
 *  <li>{@code destinationInternal != null}</li>
 *  <li>{@code destinationDescription != null}</li>
 *  <li>{@code destinationBuilding != null}</li>
 *  <li>{@code destinationFloor != null}</li>
 *  <li>{@code destinationImage > 0}</li>
 * </ul>
 * </p>
 * <p>
 * <p><b>Abstraction Function:</b></p>
 * {@link Destination} {@code d} represents a location in the robot's mapping space such that:
 * <ul>
 *  <li>{@code d.destinationDisplay} is the user-facing name of the location.</li>
 *  <li>{@code d.destinationInternal} is the name of the location as known to the robot.</li>
 *  <li>{@code d.destinationDescription} is a textual description of the destination.</li>
 *  <li>{@code d.destinationBuilding} is the name of the building where the destination is located.</li>
 *  <li>{@code d.destinationFloor} is the floor number or identifier within the building.</li>
 *  <li>{@code d.destinationImage} is the resource ID of the image representing this destination.</li>
 * </ul>
 * </p>
 */
public class Destination {
    private final String destinationDisplay;
    private final String destinationInternal;
    private final String destinationDescription;
    private final String destinationBuilding;
    private final String destinationFloor;
    private final int destinationImage;

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>{@code destinationDisplay != null}, {@code destinationInternal != null},
     *         {@code destinationDescription != null}, {@code destinationBuilding != null}
     *         {@code destinationFloor != null}, {@code destinationImage >= 0}</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new {@link Destination}.</li>
     * </ul>
     * </p>
     * @param destinationDisplay      The user-facing name of the location.
     * @param destinationInternal     The name of the location as known to the robot.
     * @param destinationDescription  A textual description of the destination.
     * @param destinationBuilding     The name of the building where the destination is located.
     * @param destinationFloor        The floor number or identifier within the building.
     * @param destinationImage        The resource ID of the image representing this destination.
     */
    public Destination(String destinationDisplay, String destinationInternal,
                       String destinationDescription, String destinationBuilding,
                       String destinationFloor, int destinationImage) {
        this.destinationDisplay = destinationDisplay;
        this.destinationInternal = destinationInternal;
        this.destinationDescription = destinationDescription;
        this.destinationBuilding = destinationBuilding;
        this.destinationFloor = destinationFloor;
        this.destinationImage = destinationImage;
        this.checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the user-facing name of the {@link Destination}.</li>
     * </ul>
     * </p>
     */
    public String getDisplayName() {
        this.checkRepresentation();
        return this.destinationDisplay;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the internal name of the {@link Destination}.</li>
     * </ul>
     * </p>
     */
    public String getInternalName() {
        this.checkRepresentation();
        return this.destinationInternal;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the description of the {@link Destination}.</li>
     * </ul>
     * </p>
     */
    public String getDescription() {
        this.checkRepresentation();
        return this.destinationDescription;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the name of the building where the {@link Destination} is located.</li>
     * </ul>
     * </p>
     */
    public String getBuilding() {
        this.checkRepresentation();
        return this.destinationBuilding;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns floor within the building where the {@link Destination} is located.</li>
     * </ul>
     * </p>
     */
    public String getFloor() {
        this.checkRepresentation();
        return this.destinationFloor;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the resource ID of the image representing the {@link Destination}.</li>
     * </ul>
     * </p>
     */
    public int getImageID() {
        this.checkRepresentation();
        return this.destinationImage;
    }

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>True iff obj is non-null, {@code instanceof} {@link Destination} and logically equals to this.</li>
     * </ul>
     * </p>
     */
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Destination)) return false;
        Destination other = (Destination) obj;
        return this.destinationDisplay.equals(other.destinationDisplay)
                 && this.destinationInternal.equals(other.destinationInternal)
                 && this.destinationDescription.equals(other.destinationDescription)
                 && this.destinationBuilding.equals(other.destinationBuilding)
                 && this.destinationFloor.equals(other.destinationFloor)
                 && this.destinationImage == other.destinationImage;
    }

    @Override
    public int hashCode() {
        return this.destinationDisplay.hashCode();
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
        assert this.destinationDisplay != null;
        assert this.destinationInternal != null;
        assert this.destinationDescription != null;
        assert this.destinationBuilding != null;
        assert this.destinationFloor != null;
        assert this.destinationImage > 0;
    }
}