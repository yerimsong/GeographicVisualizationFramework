import React from "react";
import { GlobalData, LocationData } from "../../types";

import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export const options = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top' as const,
    },
    title: {
      display: true,
      text: 'Chart.js Line Chart',
    },
},
};

export default function GraphComponent(props: {data: GlobalData, width: number, height: number}) {

    const labels = []
    const oneLocation = Array.from(props.data.locations.values())[0]
    console.log(oneLocation.rawData)
    for (var point of oneLocation.rawData) {
        let date = point.time.getMonth() + "/" + point.time.getDate() + "/" + point.time.getFullYear()
        labels.push(date)
    }

    let datasets = []

    // change the data into a format the graph can understand
    for (let [coord, locData] of props.data.locations) {
      // I'm pretty sure sorting is required inc ase rawData is not processed in-order
      locData.rawData.sort((a, b) => a.time.getTime() - b.time.getTime())
      let valueData = locData.rawData.map(dataObject => dataObject.value)

      let randomColor = `rgb(${Math.floor(Math.random()*255)}, ${Math.floor(Math.random()*255)}, ${Math.floor(Math.random()*255)})`

      datasets.push({
        label: coord.name,
        data: valueData,
        borderColor: randomColor,
        backgroundColor: randomColor,
      })
    }

    const graphData = {
      labels,
      datasets: datasets
    };

    return (
        <Line options={options} data={graphData} />
    )
}
