import React, {useEffect, useState} from "react";
import {Card, ClickOnCardDeck, setYourMove} from "./cardManager";
import Events, {Frame} from "./events";
import {MoveSubscriptions} from "./subscriptions";
import {send} from "./connection";

export function Distribution({common, move, pos, players}) {
    const [state, setState] = useState({
        clicked: false,
        clickedCard: null,
        current: move,
        position: pos,
    })
    const [turn, setTurn] = useState(false)
    const [displays, setDisplays] = useState({
        upsideCard: [false, false, false]
    })
    const events = new Events(state, setState)
    MoveSubscriptions.display = displays

    const createDeck = () => {
        let cards = []
        let offset = 21
        for (let i = 0; i < 3; i++) {
            cards.push(<Card id={"card" + i} className={"deck click-card"} image={"shirt"} top={"50%"} left={`${offset}%`}
             onClick={(el) => events.click(el)}
             onMouseEnter={(el) => events.mouseEnterOnCardDeck(el)}
             onMouseLeave={(el) => events.mouseLeaveFromCardDeck(el)}/>)
            offset = offset + 1.57
        }
        return cards
    }

    useEffect(() => MoveSubscriptions.setSubscriptions(setState, setDisplays, players.length),[])
    useEffect(() => setTurn(events.isTurn()), [state.clicked])
    useEffect(() => {setYourMove(state, players.length)}, [state])

    return (
        <div id={"main"}>
            <div id={"top"}>
                {displays.upsideCard[1] ? <Card id={"upside1"} image={"shirt"} top={"55%"} left={"40%"}/> : ""}
                <Card id={"1"} image={"shirt"} top={"55%"} left={"50%"} display={"none"} />
                <Frame id={"frame1"} top={"55%"} left={"50%"} clicked={state.clicked} onClick={(frame) => events.put(frame)} state={state}/>

                <h2 id={"name1"} className={"game-name"} style={{left: "57%", top: "0%"}}>{players[(pos + 1) % players.length]["name"]}</h2>
            </div>
            <div id={"center"}>
                {players.length === 3 ? <div>
                    <h2 id={"name2"} className={"game-name"} style={{left: "1%", top: "10%"}}>{players[(pos + 2) % players.length]["name"]}</h2>

                    {displays.upsideCard[2] ? <Card id={"2"} image={"shirt"} top={"50%"} left={"5%"} /> : ""}
                    <Frame id={"frame2"} top={"50%"}
                       left={"5%"} clicked={state.clicked} onClick={(frame) => events.put(frame)} state={state}/>
                    <Card id={"upside2"} image={"shirt"} top={"95%"} left={"5%"} display={"none"}/>
                </div> : ""}
                {createDeck().map((e) => e)}

                <Card id={"3"} image={common} top={"50%"} left={"84%"} />
                <Frame id={"frame3"} top={"50%"} left={"84%"} clicked={state.clicked} onClick={(frame) => events.put(frame)} state={state}/>
            </div>
            <div id={"bottom"}>
                {turn ? <button id={"flip-over"} onClick={() => {events.turn(); setTurn(false)}}>Перевернуть</button> : ""}
                {displays.upsideCard[0] ? <Card id={"upside0"} className={"click-card your-move"} image={"shirt"} top={"46%"} left={"40%"}
                                                onClick={() => events.clickOnPlayerDeck()}/>: ""}

                <Card id={"0"} className={"click-card"} image={"shirt"} top={"46%"} left={"50%"} display={"none"} onClick={() => events.take()}/>
                <Frame id={"frame0"} top={"46%"} left={"50%"} clicked={state.clicked} onClick={(frame) => events.put(frame)} state={state}/>

                <h2 id={"name0"} className={"game-name"} style={{left: "57%"}}>{players[pos].name}</h2>
            </div>
            <ClickOnCardDeck state={state}/>
        </div>)
}