package edu.cmu.cs.cs214.hw6.plugins;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.cs.cs214.hw6.framework.Coordinate;
import edu.cmu.cs.cs214.hw6.framework.DataPoint;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;

public class FakeDataPlugin implements DataPlugin {
    public String getDataSetName() {
        return "name";
    }

    public String getDataSetDescription() {
        return "description";
    }

    public HashMap<Coordinate, List<DataPoint>> getData() {
        HashMap<Coordinate, List<DataPoint>> data = new HashMap<>();
        Coordinate coordinate = new Coordinate(0, 0);
        DataPoint dp = new DataPoint(LocalDate.of(1, 1, 1), 1.0);
        List<DataPoint> listdp= new ArrayList<>();

        listdp.add(dp);
        data.put(coordinate, listdp);

        return data;
    }
}
