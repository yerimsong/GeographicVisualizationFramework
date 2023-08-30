import React, { ReactComponentElement } from 'react';
import { GlobalData } from './types';

interface frontPlugin{

    /**
     * 
     * @param data the data of the current DataPlugin to be visualized by this plugin
     * @param width represents a width value the plugin can use to set scale
     * @param height represents a height value the plugin can use to set scale
     */
    getComponent(data: GlobalData, width: number, height: number) : React.ReactNode

    /**
     * returns the name of the visualizer the plugin is implementing (ex. Pie Chart, Line Graph, Pennsylvania Map, etc.)
     */
    getName(): string
}

export type { frontPlugin }