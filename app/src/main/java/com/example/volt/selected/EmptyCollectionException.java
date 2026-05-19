package com.example.volt.selected;

/**
 * <h6>
 * An exception thrown when an operation is attempted on an empty collection of destinations.
 * </h6>
 * <p>
 * <b>Representation Invariant:</b>
 * <ul>
 *  <li>None</li>
 * </ul>
 * </p>
 * <p>
 * <b>Abstraction Function:</b>
 * EmptyCollectionException e represents an exception that is thrown when an operation is attempted on
 * an empty collection of destinations.
 * </p>
 */
public class EmptyCollectionException extends RuntimeException {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>this</li>
     *  <li><b>effects: </b>Creates a new exception.</li>
     * </ul>
     * </p>
     * @param message Details explaining why the exception was thrown.
     */
    public EmptyCollectionException(String message) {
        super(message);
    }
}
