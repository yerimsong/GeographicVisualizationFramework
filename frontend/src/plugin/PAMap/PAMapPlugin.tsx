import React from "react";
import { frontPlugin } from "../../frontPlugin";
import { GlobalData } from "../../types";
import PAMapComponent from "./PAMapComponent";

class PAMapPlugin implements frontPlugin {
    getComponent(data: GlobalData, width: number, height: number): React.ReactNode {
        return (
            <PAMapComponent data={data} width={width} height={height} />
        )
    }
    getName(): string {
        return "Pennsylvania Map";
    }

}

export { PAMapPlugin }
