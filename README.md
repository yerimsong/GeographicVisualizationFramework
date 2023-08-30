## Overview
This framework allows users to easily create visualizations from location based time series data. With any data that has time and location associated with it, this framework allows users to transform it into various comprehensible graphical representations and perform statistical calculations as well. For example, some applications for this framework include generating rainfall changes in Pennsylvania across the years or graphing the number of covid deaths per county. This framework provides built-in functionality in the UI to select which plugins to use.

## Running
In `./frontend`, run `npm install` and then `npm start`.  In `./backend` run `mvn site` then `mvn exec:exec`.

## Design Explanation
This framework requires the user to implement at least two plugins: one backend data plugin and one frontend visualization plugin. With more plugins, the data and visualization plugins can be mixed and matched to create several different visualizations.

### Backend
The data plugin involves retrieving data from a source and parsing it. The plugin can use whatever file types or URL it wants as long as it parses that data into a standard format. The interface is quite simple and grants lots of flexibility to the implementation. The user only needs to provide the data set name, description, and actual data. The type of the data is a hashmap with Coordinates and a list of DataPoints as keys and values respectively. Coordinates contain a longitude, latitude, and optionally a location name, depending on the data given. If given a location name, the longitude and latitude can be found using the GeoCoding API implemented in this framework. Each DataPoint includes a date and the data value.

### Frontend
The frontend plugin interface is also quite minimal, only asking for one React component back from any frontend plugin.  The component is passed the data that a user selects and uses it in their visualization.  For example, the Pennsylvania map frontend plugin is in charge of creating a map, but the frontend skeleton controls which data is passed into that plugin.

## Adding Plugins
### Backend
In `/backend/src/main/java/edu/cmu/cs/cs214/hw6/plugins`, create a new class that implements the `DataPlugin` interface.  Then, in `backend\src\main\resources\META-INF\services\edu.cmu.cs.cs214.hw6.framework.core.DataPlugin` add a new line that contains the classpath for your new plugin.

### Frontend
In `frontend\src\plugin`, add a new class that implements the `frontPlugin` interface.  Then, in `frontend\src\MainSkeletonComponent.tsx`, change the `visualizers` array near the top of the file to be initialized with a new instantiation of your plugin.

