import React from "react";
import {send} from "./connection";
import {MoveSubscriptions} from "./subscriptions";

class Events {
    state
    setState

    constructor(state, setState) {
        this.state = state
        this.setState = setState
        MoveSubscriptions.state = state
    }

    mouseEnterOnCardDeck(el) {
        const style = el.target.style
        if (!this.state.clicked && this.state.current === this.state.position)
            style.top = `${parseInt(style.top) - 2}%`
    }

    mouseLeaveFromCardDeck(el) {
        const style = el.target.style
        if (!this.state.clicked && this.state.current === this.state.position)
            style.top = `${parseInt(style.top) + 2}%`
    }

    click(cardId) {
        let id = cardId.target.id
        if (!this.state.clicked && this.state.current === this.state.position)
            send({cardPos: id.substring(4)}, "click")
    }

    put(frame) {
        if (this.state.clicked && this.state.current === this.state.position) {
            let id = frame.target.id.substring(5)
            send({player: id, card: this.state.clickedCard}, "put")
        }
    }

    take() {
        if (!this.state.clicked && this.state.current === this.state.position)
            send({}, "take")
    }

    isTurn() {
        return document.querySelectorAll(".deck").length === 0 && this.state.current === this.state.position
            && document.getElementById("upside0") === null && this.state.clickedCard === null;
    }

    turn() {
        send({}, "turn")
    }

    clickOnPlayerDeck() {
        if (!this.state.clicked && this.state.current === this.state.position)
            send({}, "clickOnPlayerDeck")
    }
}

export function Frame(props) {
    if (props.state.clicked && props.state.current === props.state.position) {
        return (<div id={props.id} className={"card frame"} style={{top: String(props.top), left: String(props.left)}}
                     onClick={props.onClick}></div>)
    }
}
export default Events