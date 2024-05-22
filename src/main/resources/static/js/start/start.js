import React, {useEffect} from "react";
import {Forms} from "./forms";
import {backgroundChange} from "./startUtils";
import {createRoot} from "react-dom/client";

export function Start() {
    useEffect(() => {
        backgroundChange()
    })

    return (<div id={"main"}>
        <div id={"background"}></div>
        <div id={"lines"}>
            <div className="line up static"></div>
            <div className="line bottom static"></div>
            <div className="line up move"></div>
            <div className="line bottom move"></div>
        </div>
        <Forms />
    </div>)
}

createRoot(document.getElementById("root")).render(<Start />)