package edu.cmu.cs.cs214.hw6.framework.core;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The framework core implementation
 */
public class DataVisualizationFrameworkImpl implements DataVisualizationFramework {
    private List<DataPlugin> registeredPlugins;
    private DataPlugin currentPlugin;

    /**
     * Create a new DataVisualizationFramework
     */
    public DataVisualizationFrameworkImpl() {
        registeredPlugins = new ArrayList<DataPlugin>();
        currentPlugin = null;
    }

    /**
     * Registers a new {@link DataPlugin)} with the data visualization framework
     */
    public void registerPlugin(DataPlugin plugin) {
        registeredPlugins.add(plugin);
        currentPlugin = plugin;
    }

    /**
     * Returns all registered data plugins
     * @return {@code List<String>} of registered data plugin names
     */
    @Override
    public List<String> getRegisteredPluginNames() {
        return registeredPlugins.stream().map(DataPlugin::getDataSetName).collect(Collectors.toList());
    }

    @Override
    public String getDataSetName() {
        if (currentPlugin != null) {
            return currentPlugin.getDataSetName();
        }
        return "";
    }

    @Override
    public String getDataSetDescription() {
        if (currentPlugin != null) {
            return currentPlugin.getDataSetDescription();
        }
        return "";
    }
}
