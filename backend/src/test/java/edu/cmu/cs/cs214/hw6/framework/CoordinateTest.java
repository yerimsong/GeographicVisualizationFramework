package edu.cmu.cs.cs214.hw6.framework;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordinateTest {
    private Coordinate coordinate1 = new Coordinate(-79.9900861, 40.4416941);
    private Coordinate coordinate2 = new Coordinate(11.555, 0, "somewhere");

    @Test
    public void test1() {
        assertEquals(-79.9900861, coordinate1.getLongitude());
        assertEquals(40.4416941, coordinate1.getLatitude());
        assertEquals("", coordinate1.getName());
    }
    

    @Test
    public void test2() {
        assertEquals(11.555, coordinate2.getLongitude());
        assertEquals(0, coordinate2.getLatitude());
        assertEquals("somewhere", coordinate2.getName());
    }
}
