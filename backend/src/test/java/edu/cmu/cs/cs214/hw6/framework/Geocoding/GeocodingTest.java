package edu.cmu.cs.cs214.hw6.framework.Geocoding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.cmu.cs.cs214.hw6.framework.Coordinate;

public class GeocodingTest {

    private Geocoder geocoder = new Geocoder("c065675cf9e3047274192ccc55de0479");

    @Test
    @Disabled
    public void testOnePlace() {
        Coordinate result = geocoder.nameToCoord("Pittsburgh");

        assertEquals(40.4416941, result.getLatitude());
        assertEquals(-79.9900861, result.getLongitude());
    }

    @Test
    @Disabled
    public void testOnePlaceFails() {
        Coordinate result = geocoder.nameToCoord("random place that doesn't exist");

        assertEquals(null, result);
    }

    @Test
    @Disabled
    public void testMultiple() {
        ArrayList<String> failed = new ArrayList<String>();
        ArrayList<String> input = new ArrayList<String>();

        input.add("Fox Chapel");
        input.add("Pittsburgh");
        input.add("Pittsburgh,Pennsylvania");
        input.add("Pittsburgh,PA");
        input.add("Random words here should fail");

        List<Coordinate> result = geocoder.namesToCoords(input, failed);
        assertEquals(3, result.size());

        assertEquals(40.522924, result.get(0).getLatitude());
        assertEquals(-79.8913193, result.get(0).getLongitude());
        assertEquals(40.4416941, result.get(1).getLatitude());
        assertEquals(-79.9900861, result.get(1).getLongitude());
        assertEquals(40.4416941, result.get(2).getLatitude());
        assertEquals(-79.9900861, result.get(2).getLongitude());

        assertTrue(failed.get(0).equals("Pittsburgh,PA"));
        assertTrue(failed.get(1).equals("Random words here should fail"));
    }
}
