package edu.cmu.cs.cs214.hw6.framework.core;

import edu.cmu.cs.cs214.hw6.framework.Coordinate;
import edu.cmu.cs.cs214.hw6.framework.DataPoint;
import java.util.HashMap;
import java.util.List;

/**
 * The data plug-in interface that plug-ins use to implement and register data
 * with the {@link DataVisualizationFramework}.
 */
public interface DataPlugin {

    /**
     * Gets the name of the plug-in data set.  This name *must* have no spaces
     */
    String getDataSetName();

    /**
     * Gets the description of the plug-in data set.
     */
    String getDataSetDescription();

    /**
     * Gets the data from the plug-in data set.
     */
    HashMap<Coordinate, List<DataPoint>> getData();
}
