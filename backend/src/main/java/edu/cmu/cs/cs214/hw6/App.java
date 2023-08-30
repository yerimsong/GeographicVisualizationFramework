package edu.cmu.cs.cs214.hw6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import edu.cmu.cs.cs214.hw6.framework.core.DataVisualizationFrameworkImpl;
import edu.cmu.cs.cs214.hw6.framework.Coordinate;
import edu.cmu.cs.cs214.hw6.framework.DataPoint;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private DataVisualizationFrameworkImpl dataSet;
    private List<DataPlugin> plugins;

    /**
     * Start the server at :8080 port.
     * @throws IOException
     */
    public App() throws IOException {
        super(8080);

        this.dataSet = new DataVisualizationFrameworkImpl();
        plugins = loadPlugins();
        // System.out.println(plugins.get(0).getDataSetName());
        System.out.println("number plugins: " + plugins.size());

        for (DataPlugin p: plugins){
            dataSet.registerPlugin(p);
        }

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();


        //if called returns a JSON containing all the names of loaded DataPlugins to the front
        if (uri.equals("/getPluginNames")){
            String result = "{ \"dataPluginNames\": [";
            for (String name : dataSet.getRegisteredPluginNames()) {
                result += "\"" + name + "\",";
            }
            result = result.substring(0, result.length() - 1);

            result += "]}";

            return newFixedLengthResponse(result);
        }
        //if called returns a JSON containing the title and description of the given DataPlugin
        else if (uri.equals("/getText")){
            String pluginName = params.get("pluginName");

            DataPlugin current = null;
            for(DataPlugin plugin: plugins) {
                if(plugin.getDataSetName().equals(pluginName)) {
                    current = plugin;
                    break;
                }
            }
            if (current != null) {
                return newFixedLengthResponse("{ \"title\": \""+current.getDataSetName() + "\","
                                              +"\"desc\": \""+current.getDataSetDescription() + "\"}");
            }
        }
        //if called returns a JSON containing the data from the given DataPlugin in the form of Coordiante, DataPoint List pairs 
        else if (uri.equals("/getdata")) {
            String pluginName = params.get("pluginName");

            DataPlugin current = null;
            for(DataPlugin plugin: plugins) {
                if(plugin.getDataSetName().equals(pluginName)) {
                    current = plugin;
                    break;
                }
            }
            if (current != null) {
                Map<Coordinate, List<DataPoint>> data = current.getData();
                return newFixedLengthResponse(dataToJson(data));
            }
        }

        return newFixedLengthResponse("{}");
    }

    /**
     * Load plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<DataPlugin> loadPlugins() {
        ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getDataSetName());
            result.add(plugin);
        }
        return result;
    }

    /**
     * translates a hashmap of Map<Coordinate, List<DataPoint>> into a String so it can be turned into a JSON file later
     * @param data a hashmap of type Map<Coordinate, List<DataPoint>> that contains all the data from a DataPlugin
     * @return a string to be turned into JSON file later
     */
    public String dataToJson(Map<Coordinate, List<DataPoint>> data) {
        String returnString = "{ \"dataMap\": {";

        for (Map.Entry<Coordinate, List<DataPoint>> entry : data.entrySet()) {
            Coordinate coord = entry.getKey();
            List<DataPoint> coordDataPoints = entry.getValue();

            String dataString = "\"(" + coord.getLatitude() + "," + coord.getLongitude() + "," + coord.getName() + ")\": [";
            for (DataPoint dataPoint : coordDataPoints) {
                String dataPointString = "{" +
                                            "\"time\": \"" + dataPoint.getDate().toString() + "\"," +
                                            "\"value\": " + dataPoint.getValue() +
                                         "},";

                dataString += dataPointString;
            }
            dataString = dataString.substring(0, dataString.length() - 1);

            dataString += "],";

            returnString += dataString;
        }

        returnString = returnString.substring(0, returnString.length() - 1);

        returnString += "}}";

        return returnString;
    }
}

