package edu.cmu.cs.cs214.hw6.framework;

import java.util.Objects;

/**
 * This class contains a pair of numbers, longitude and latitude, along with an optional name
 */
public class Coordinate {

    private double longitude;
    private double latitude;
    private String name = "";

    /**
     * constructor for the Coordinate class
     * @param longitude longitude value
     * @param latitude latitude value
     * @param name name of the location/coordinate
     */
    public Coordinate(double longitude, double latitude, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    /**
     * constructor for the Coordinate class with no name
     * @param longitude
     * @param latitude
     */
    public Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the coordinate
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets the latitude of the coordinate
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /** Gets the location name at the coordinate */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representing the Coordinate class
     */
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }

    /**
     * function that returns true if this Coordinate object has the same values as the object given and false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }

        Coordinate otherCoord = (Coordinate) other;
        return otherCoord.getLatitude() == latitude && otherCoord.getLongitude() == longitude;
    }

    /**
     * returns a hash code based on the coordinate's latitude and longitude values
     */
    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
