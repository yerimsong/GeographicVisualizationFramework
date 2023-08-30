package edu.cmu.cs.cs214.hw6.framework.core;

import java.util.List;

/**
 * The interface by which {@link DataPlugin} instances can directly interact
 * with the data visualization framework.
 */
public interface DataVisualizationFramework {
    /**
     * Get the name of data set.
     */
    String getDataSetName();

    /**
     * Get the description of the data set.
     */
    String getDataSetDescription();

    /**
     * Get the names of all data plugins
     */
    List<String> getRegisteredPluginNames();
}
