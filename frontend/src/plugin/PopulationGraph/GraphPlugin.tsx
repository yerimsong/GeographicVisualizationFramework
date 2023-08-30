import React from "react";
import { frontPlugin } from "../../frontPlugin";
import { GlobalData } from "../../types";
import GraphComponent from "./GraphComponent";

class GraphPlugin implements frontPlugin {
    getComponent(data: GlobalData, width: number, height: number): React.ReactNode {
        return (
            <GraphComponent data={data} width={width} height={height} />
        )
    }
    getName(): string {
        return "Line Graph";
    }

}

export { GraphPlugin }
