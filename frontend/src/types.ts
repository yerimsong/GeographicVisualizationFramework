/**
 * This object represents a location by containing its longitude and latitude
 *      values along with the location's name.
 * This object is then paired with LocationData to represent a list of datapoints
 *      along with the time and place the datapoints are each related to
 */
interface Coord {
    longitude: number;
    latitude: number;
    name: string;
}

/**
 * This object holds some number value representing data
 *      and a Date object that represents the time this value is related to.
 * This information is also related to a place, which is represented by the fact 
 *      that LocationData holds an array of DataObjects and that LocationData is 
 *      paired with a Coord object in GlobalData.
 * For example, the number of inches of rain that fell on 11/29/2022 in Pittsburgh
 */
interface DataObject {
    time: Date;
    value: number;
}

/**
 * This is the object that holds all the information about a specific location
 * It is paired with a coordinate object in GlobalData via a hashmap 
 * The object contains an array of DataObjects
 *      This array represents all the data about this location over time
 * The object also contains some pre-calculated statistics for the visualizer to use if wanted
 */
interface LocationData{
    mean : number;
    median : number;
    stdDev : number;
    max : number;
    min : number;
    rawData : DataObject[];
}

/**
 * This is the object that holds all the information a DataPlugin provides us.
Specifically, the data is stored in a hashmap of type Map<Coord, LocationData>
    So for every coordinate/location, there is a LocationData which contains an 
    array of data points relating to that location across time, along with some 
    statistics about the location
Also includes a couple pre-calculated statistics for visualizers to use if wanted
*/
interface GlobalData{
    locations : Map<Coord, LocationData>;
    globalMean : number;
    globalMedian : number;
    globalStdDev : number;
    globalMax : number;
    globalMin : number;
}

export type { Coord, DataObject, LocationData, GlobalData }
