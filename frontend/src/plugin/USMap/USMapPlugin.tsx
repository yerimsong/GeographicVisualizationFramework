import React from "react";
import { frontPlugin } from "../../frontPlugin";
import { GlobalData } from "../../types";
import USMapComponent from "./USMapComponent";

class USMapPlugin implements frontPlugin {
    getComponent(data: GlobalData, width: number, height: number): React.ReactNode {
        return (
            <USMapComponent data={data} width={width} height={height} />
        )
    }
    getName(): string {
        return "US Map";
    }

}

export { USMapPlugin }
