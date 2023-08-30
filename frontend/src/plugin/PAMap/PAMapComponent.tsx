import { MenuItem, Select, Slider, Stack, Tooltip, Typography } from "@mui/material";
import { Box } from "@mui/system";
import React, { useEffect, useState } from "react";
import { ComposableMap, Geographies, Geography, Marker, ZoomableGroup } from "react-simple-maps";
import { GlobalData, LocationData } from "../../types";

export default function PAMapComponent(props: {data: GlobalData, width: number, height: number}) {

    const {data, width, height} = props
    const [markers, setMarkers] = useState<{coordinates: [number, number], name: string, value: number}[]>([])

    let getValue = (locData: LocationData) => locData.mean;

    // Create markers that show up on the map
    let createMarkers = () => {
        setMarkers([])
        for (let [coord, locationData] of data.locations) {
            setMarkers(prevMarkers => [...prevMarkers, {
                coordinates: [coord.longitude, coord.latitude],
                name: coord.name,
                value: getValue(locationData)
            }])
        }
    }

    const [dataSize, setDataSize] = useState(4);
    const [dataScale, setDataScale] = useState(1);
    const [statistic, setStatistic] = useState("mean");

    // change with the dropdown
    useEffect(() => {
        if (statistic === "mean") {
            getValue = (locData: LocationData) => locData.mean
        } else if (statistic === "median") {
            getValue = (locData: LocationData) => locData.median
        } else if (statistic === "min") {
            getValue = (locData: LocationData) => locData.min
        } else if (statistic === "max") {
            getValue = (locData: LocationData) => locData.max
        } else if (statistic === "stdDev") {
            getValue = (locData: LocationData) => locData.stdDev
        }
        createMarkers()
    }, [statistic])

    // re-create markers if data changes
    useEffect(() => {
        createMarkers()
    }, [data])

    return (
        <Box sx={{width: width, height: height}}>
            <ComposableMap projection="geoAlbers" projectionConfig={{scale: 4000}}>
                <ZoomableGroup center={[-77, 41]}>
                    <Geographies geography="/PA-42-pennsylvania-counties.json">
                        {({ geographies }) =>
                        geographies.map((geo) => (
                            <Geography
                                key={geo.rsmKey}
                                geography={geo}
                                fill="#EAEAEC"
                                stroke="#D6D6DA"
                            />
                        ))
                        }
                    </Geographies>
                    {markers.map(({ coordinates, name, value }) => (
                        <Marker key={coordinates.toString()} coordinates={coordinates}>
                            <Tooltip title={`${name}: ${value}`}>
                                <circle r={Math.pow(value, dataScale) / Math.pow((statistic === "stdDev" ? data.globalStdDev : data.globalMean), dataScale) * dataSize} fill="#00F" stroke="#fff" strokeWidth={0} />
                            </Tooltip>
                        </Marker>
                    ))}
                </ZoomableGroup>
            </ComposableMap>
            <Stack spacing={3} direction="row" justifyContent="space-evenly" alignItems="center">
                <Box width={"100%"}>
                    <Stack spacing={3} direction="row" sx={{ marginInline: 2 }} justifyContent="space-between" alignItems="center">
                        <Typography>Point Size</Typography>
                        <Slider sx={{marginInline: 5}} min={.5} max={10} step={.1} aria-label="dataSize" value={dataSize} onChange={(e, newVal) => setDataSize(newVal as number)}/>
                    </Stack>
                    <Stack spacing={3} direction="row" sx={{ marginInline: 2 }} justifyContent="space-between" alignItems="center">
                        <Typography>Point Relative Scale</Typography>
                        <Slider sx={{marginInline: 5}} valueLabelDisplay="auto" min={0.1} max={5} step={.1} aria-label="dataScale" value={dataScale} onChange={(e, newVal) => setDataScale(newVal as number)}/>
                    </Stack>
                </Box>
                <Select
                    label="Select Statistic After Choosing Data Plugin"
                    value={statistic}
                    onChange={(e) => setStatistic(e.target.value as string)}
                >
                    <MenuItem value="mean">Mean</MenuItem>
                    <MenuItem value="median">Median</MenuItem>
                    <MenuItem value="min">Minimum</MenuItem>
                    <MenuItem value="max">Maximum</MenuItem>
                    <MenuItem value="stdDev">Standard Deviation</MenuItem>
                </Select>
            </Stack>
        </Box>
    )
}
