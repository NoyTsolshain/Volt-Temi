package com.example.volt.commnad;

/**
 * <h6>
 * {@link Command} represents an operation.
 * </h6>
 */
public interface Command<T> {

    /**
     * <h5>Specification:</h5>
     * <p>
     * <ul>
     *  <li><b>requires: </b>None</li>
     *  <li><b>modifies: </b>None</li>
     *  <li><b>effects: </b>Executes the operations associated with the command.</li>
     * </ul>
     * </p>
     * @param arg Additional data needed for execution.
     */
    boolean execute(T arg);
}