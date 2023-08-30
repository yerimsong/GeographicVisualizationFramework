package edu.cmu.cs.cs214.hw6.framework.Geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object from the Geocoding API, used in deserialization
 */
public class GeocodingResponse {
    private final String name;
    private final double lat;
    private final double lon;
    private final String country;

    /**
     * Create new GeocodingResponse
     * @param name Name of location
     * @param lat Latitude
     * @param lon Longitude
     * @param country Country of location
     */
    public GeocodingResponse(
        @JsonProperty("name") String name,
        @JsonProperty("lat") double lat,
        @JsonProperty("lon") double lon,
        @JsonProperty("country") String country
    ) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    public String getName() {
        return name;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public String getCountry() {
        return country;
    }
}
