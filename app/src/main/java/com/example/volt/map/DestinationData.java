package com.example.volt.map;

/**
 * <h6>
 * {@link DestinationData} represents the data of a location in the robot's mapping space.
 * </h6>
 */
public interface DestinationData {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the user-facing name of the {@link DestinationData}.</li>
     * </ul>
     * </p>
     */
    String getDisplayName();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the internal name of the {@link DestinationData}.</li>
     * </ul>
     * </p>
     */
    String getInternalName();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the description of the {@link DestinationData}.</li>
     * </ul>
     * </p>
     */
    String getDescription();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the name of the building where the {@link DestinationData} is located.</li>
     * </ul>
     * </p>
     */
    String getBuilding();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns floor within the building where the {@link DestinationData} is located.</li>
     * </ul>
     * </p>
     */
    String getFloor();

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Returns the resource ID of the image representing the {@link DestinationData}.</li>
     * </ul>
     * </p>
     */
    int getImageID();
}