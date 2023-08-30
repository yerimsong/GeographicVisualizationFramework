import React from "react";
import { frontPlugin } from "../frontPlugin";
import { GlobalData } from "../types";

import Highcharts from 'highcharts'
import HighchartsReact from "highcharts-react-official";
import { arrayBuffer } from "stream/consumers";


class PieChartPlugin implements frontPlugin {

    options = {}

    getComponent(data: GlobalData, width: number, height: number): React.ReactNode {
        var chartData = new Array(data.locations.size);
        var i:number = 0;
        var sum:number = 0;

        //for each location
        for(let[coord, locationData] of data.locations){
            //reset sum value
            sum = 0;
            //sum up data for this location over time
            locationData.rawData.forEach(function(dataObject){
                sum += dataObject.value;
            })

            //add sum to chartData to be displayed
            chartData[i] = [coord.name,sum];
            //increase index so we can find the sum of the next location
            i++;
        }

        this.options = {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: ''
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            accessibility: {
                point: {
                    valueSuffix: '%'
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                    }
                }
            },
            series: [{
                name: 'Percentage',
                colorByPoint: true,
                data: chartData
            }]
        }

        return (
            <div className="App">
                <HighchartsReact highcharts={Highcharts} options={this.options}></HighchartsReact>
            </div>
        )
    }
    getName(): string {
        return "Pie Chart of total accumulated values";
    }

}

export { PieChartPlugin }
