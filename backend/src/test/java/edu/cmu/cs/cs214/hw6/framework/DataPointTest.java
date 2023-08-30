package edu.cmu.cs.cs214.hw6.framework;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

public class DataPointTest {
    private LocalDate date = LocalDate.of(1, 1, 1);
    private DataPoint dp1 = new DataPoint(date, 99.999);
    private DataPoint dp2 = new DataPoint(date, -1);

    @Test
    public void test1() {
        LocalDate newDate = LocalDate.of(1, 1, 1);
        assertEquals(dp1.getValue(), 99.999);
        assertEquals(dp1.getDate().toString(), newDate.toString());
    }

    @Test
    public void test2() {
        LocalDate newDate = LocalDate.of(1, 1, 1);
        assertEquals(dp2.getValue(), -1);
        assertEquals(dp2.getDate().toString(), newDate.toString());
    }
}
