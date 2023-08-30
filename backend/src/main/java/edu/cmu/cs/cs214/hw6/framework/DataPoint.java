package edu.cmu.cs.cs214.hw6.framework;

import java.time.LocalDate;

/**
 * A singular datapoint for at a specific time
 */
public class DataPoint {
    private LocalDate date;
    private double value;

    /**
     * Constructor for DataPoint class
     * @param date the date at which the value is relevant to
     * @param value some data value
     */
    public DataPoint(LocalDate date, double value) {
        this.date = date;
        this.value = value;
    }

    /**
     * Gets the date at the data point
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Gets the value of the data point
     */
    public double getValue() {
        return this.value;
    }
}
