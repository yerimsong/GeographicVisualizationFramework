package edu.cmu.cs.cs214.hw6.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.cmu.cs.cs214.hw6.framework.Coordinate;
import edu.cmu.cs.cs214.hw6.framework.DataPoint;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;

public class CovidPluginTest {
    @Disabled
    @Test
    public void testRegister() {
        DataPlugin rainfall = new CovidPlugin();

        HashMap<Coordinate, List<DataPoint>> data = rainfall.getData();

        for (Map.Entry<Coordinate, List<DataPoint>> entry : data.entrySet()) {
            List<DataPoint> points = entry.getValue();
            Coordinate coord = entry.getKey();
            System.out.println(points.size());
            System.out.println(coord.toString());

            if (points.size() > 0) {
                System.out.println(points.get(0).getValue());
                System.out.println(points.get(0).getDate().toString());
            }
        }
    }
}
