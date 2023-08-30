import { Box, Card, CardContent, Divider, MenuItem, Select, Stack, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { frontPlugin } from "./frontPlugin";
import { PAMapPlugin } from "./plugin/PAMap/PAMapPlugin";
import { GraphPlugin } from "./plugin/PopulationGraph/GraphPlugin";
import { PieChartPlugin } from "./plugin/PieChartPlugin";
import { USMapPlugin } from "./plugin/USMap/USMapPlugin";
import { Coord, DataObject, GlobalData, LocationData } from "./types";

// ADD NEW VISUALIZERS HERE
const visualizers: frontPlugin[] = [new PAMapPlugin(), new USMapPlugin(), new PieChartPlugin(), new GraphPlugin()]

export default function MainSkeletonComponent() {
    const [title, setTitle] = useState("")
    const [description, setDescription] = useState("")
    const [globalData, setGlobalData] = useState<GlobalData>({
        locations: new Map(),
        globalMean: 0,
        globalMedian: 0,
        globalMin: 0,
        globalMax: 0,
        globalStdDev: 0
    })
    const [currentDataPlugin, setCurrentDataPlugin] = useState("")
    const [dataPlugins, setDataPlugins] = useState([] as string[])

    const [currentVisualizerIndex, setCurrentVisualizerIndex] = useState(0);

    /**
   * parses json file for data and calculates stats
   * @param rawJsonData json object to be parsed
   * @returns
   */
    let parseData = (rawJsonData: {dataMap: any}) => {
        let coordDataMapInput= rawJsonData.dataMap
        let jsCoordDataMap = new Map<Coord, LocationData>()

        for (let coord in coordDataMapInput) {

        let locationData: LocationData = {
            mean: 0,
            median: 0,
            stdDev: 0,
            max: 0,
            min: 0,
            rawData: []
        }

        // coord is of the form "(lat, lon)"
        coord.replaceAll(" ", "")
        let coords = coord.split(",")
        let lat = parseFloat(coords[0].slice(1))
        let lon = parseFloat(coords[1])
        let name = coords[2].slice(0, -1)

        let newCoord: Coord = {
            latitude: lat,
            longitude: lon,
            name: name
        }

        let coordsData: {time: string, value: number}[] = coordDataMapInput[coord]
        for (let dataPoint of coordsData) {
            locationData.rawData.push({
            time: new Date(dataPoint.time),
            value: dataPoint.value
            })
        }

        // calculate statistics
        let rawData = locationData.rawData
        rawData.sort((a, b) => a.value - b.value)
        locationData.median = rawData[Math.floor(rawData.length / 2)].value

        locationData.mean = rawData.reduce((acc, curr) => {
            return acc + curr.value;
        }, 0) / rawData.length

        locationData.max = rawData.reduce((acc, curr) => {
            return curr.value > acc ? curr.value : acc;
        }, 0)

        locationData.min = rawData.reduce((acc, curr) => {
            return curr.value < acc ? curr.value : acc;
        }, Number.POSITIVE_INFINITY)

        locationData.stdDev = Math.sqrt(rawData.map(x => Math.pow(x.value - locationData.mean, 2)).reduce((a, b) => a + b) / rawData.length)

        // set in Map
        jsCoordDataMap.set(newCoord, locationData);
        }
        //  calculate global statistics
        let globalData: GlobalData = {
            locations: jsCoordDataMap,
            globalMean: 0,
            globalMedian: 0,
            globalStdDev: 0,
            globalMax: 0,
            globalMin: 0,
        }

        if (jsCoordDataMap.size !== 0) {
            let allData: DataObject[] = [...jsCoordDataMap.values()].reduce((acc, curr) => {
                return acc.concat(...curr.rawData);
            }, new Array<DataObject>())
            allData.sort((a, b) => a.value - b.value)
            globalData.globalMedian = allData[Math.floor(allData.length / 2)].value

            globalData.globalMean = allData.reduce((acc, curr) => {
                return acc + curr.value;
            }, 0) / allData.length

            globalData.globalMax = allData.reduce((acc, curr) => {
                return curr.value > acc ? curr.value : acc;
            }, 0)

            globalData.globalMin = allData.reduce((acc, curr) => {
                return curr.value < acc ? curr.value : acc;
            }, Number.POSITIVE_INFINITY)

            globalData.globalStdDev = Math.sqrt(allData.map(x => Math.pow(x.value - globalData.globalMean, 2)).reduce((a, b) => a + b) / allData.length)
        }

        return globalData
    }

    /**
     * gets global data give a data plugin name
     * @param pluginName  name of data plugin
     * @returns GlobalData
     */
    async function getData(pluginName: string): Promise<GlobalData> {
        // make pluginName = "testData" to get guaranteed response from backend
        const response: Response = await fetch(`/getdata?pluginName=${pluginName}`);
        const json = await response.json();

        let data: GlobalData = parseData(json)

        return data
    }

    /**
     * gets current data title and description
     * @param pluginName
     * @returns An object of form {title: string, description: string}
     */
    async function getText(): Promise<{title: string, description: string}> {
        const response: Response = await fetch(`/getText?pluginName=${currentDataPlugin}`);
        const json = await response.json();

        return {
            title: json.title,
            description: json.desc
        }
    }

    /**
     * get names of all data plugins to store. Called at startup
     * @returns array of strings
     */
    async function getDataPluginNames(): Promise<string[]> {
        const response: Response = await fetch(`/getPluginNames`);
        const json = await response.json();

        return json.dataPluginNames
    }

    // load data on page load
    useEffect(() => {
        (async () => {
            let names = await getDataPluginNames()
            setDataPlugins(names)
            setCurrentDataPlugin(names[0])

            let data = await getData(names[0])
            setGlobalData(data)

            let text = await getText()
            setTitle(text.title)
            setDescription(text.description)
        })()
    }, [])

    // reload data if data plugin changes
    useEffect(() => {
        (async () => {

            let data = await getData(currentDataPlugin)
            setGlobalData(data)

            let text = await getText()
            setTitle(text.title)
            setDescription(text.description)
        })()
    }, [currentDataPlugin])

    return (
        <Box>
            <Card elevation={3} sx={{ ':hover': {boxShadow: 20}, m:4, padding: 2, boxShadow: 2}}>
                <CardContent>
                    <Stack spacing={3} direction="row" sx={{ marginInline: 2 }} justifyContent="space-between" alignItems="center">
                        <div>
                            <Typography variant="h4">Data Plugin: {title}</Typography>
                            <Typography>{description}</Typography>
                        </div>
                        <Select
                            label="Select Data Plugin"
                            value={currentDataPlugin}
                            onChange={(e) => setCurrentDataPlugin(e.target.value as string)}
                            sx={{m:5}}
                        >
                            {dataPlugins.map(name => (
                                <MenuItem key={name} value={name}>{name}</MenuItem>
                            ))}
                        </Select>
                    </Stack>

                    <Divider sx={{marginY: 2}}/>

                    <Stack spacing={3} direction="row" sx={{ marginInline: 2 }} justifyContent="space-between" alignItems="center">
                        <Typography variant="h4">Visualizer: {visualizers[currentVisualizerIndex].getName()}</Typography>

                        <Select
                            label="Select Visualizer Plugin"
                            value={currentVisualizerIndex}
                            onChange={(e) => setCurrentVisualizerIndex(e.target.value as number)}
                            sx={{m:5}}
                        >
                            {visualizers.map((vis, i) => (
                                <MenuItem key={vis.getName()} value={i}>{vis.getName()}</MenuItem>
                            ))}
                        </Select>
                    </Stack>
                </CardContent>
            </Card>
            {visualizers[currentVisualizerIndex].getComponent(globalData, 800, 800)}
        </Box>
    )
}
