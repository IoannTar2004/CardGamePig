import React, {useEffect, useState} from "react";
import { GiPodiumWinner } from "react-icons/gi";
import {send, subscribe} from "./connection";

export function Finish({data}) {
    const yourData = data.players.filter(i => i.position === data.position)[0]
    const index = data.players.findIndex(i => i.position === data.position)

    const [lose, setLose] = useState(index !== 0)

    useEffect(() => send({}, "ratingIsChangedInform"))
    if (lose)
        return (<div>
            <div id={"blur"}></div>
            <PigLose size={data.players.length} index={index} weight={yourData.weight} setLose={setLose}/>
        </div>)
    else
        return (<div>
            <PlayersTable players={data.players}/>
        </div>)
}

function getPlayersView(players) {
    function getDifference(player) {
        const difference = player.rating - player.lastRating

        if (difference > 0)
            return <span style={{color: "green"}}> +{difference}</span>
        else if (difference < 0)
            return <span style={{color: "red"}}> {difference}</span>
        return <span> +0</span>
    }

    let totalPlayers = []
    for (let i = 0; i < players.length; i++) {
        totalPlayers.push(<tr>
            <td>
                <div className={"player-stat"}>
                    <div>
                        <h3>{i + 1}. {players[i].name}</h3>
                        <div id={"rating-text"}>Рейтинг: {players[i].rating} {getDifference(players[i])}</div>
                    </div>
                    {i > 0 ? <div>
                        <img id={"pig" + players[i].position} className={"pig-finish"} src={"../images/pig_finish.png"}/>
                    </div> : ""}
                    {i > 0 ? <div id={"pork-per-year"}>
                        <h3>{players[i].weight} кг свинины в год</h3>
                    </div> : ""}
                </div>
            </td>
        </tr>)
    }
    return totalPlayers
}

function pigSound(img, onEnded) {
    img.src = "../images/pig_finish2.png"
    setTimeout(() => img.src = "../images/pig_finish.png", 100)

    const audio = new Audio("../audio/final_pig_sound.mp3")
    audio.onended = onEnded
    audio.play()
}

function PlayersTable({players}) {
    const [canContinue, setCanContinue] = useState(false)
    useEffect(() => {
        subscribe("/players/game/pigSound",
        (data) => pigSound(document.getElementById("pig" + JSON.parse(data).position), null))

        subscribe("/players/game/reload", () => location.reload())

        subscribe("/players/game/punishEnough", () => setCanContinue(true))

        send({}, "checkReadyPlayers")
    }, [])

    return (<div id={"finish-window"}>
        <h1><GiPodiumWinner color={"green"}/> {players[0].name} победил! <GiPodiumWinner color={"green"}/></h1>
        <table id={"players-stat"}>
            <tbody>
                {getPlayersView(players).map(e => e)}
            </tbody>
        </table>
        <button className={"continue"} disabled={!canContinue} onClick={() => send({}, "continue")}><b>Продолжить</b></button>
    </div>)
}

function PigLose({size, index, weight, setLose}) {
    const [clickCount, setClickCount] = useState(10 - 5 * (size - 1 - index))
    const [canClick, setCanClick] = useState(true)

    const clickOnPig = (img) => {
        if (canClick) {
            send({}, "clickOnPig")
            setCanClick(false)
            pigSound(img.target, () => setCanClick(true))
            setClickCount(clickCount - 1)
        }
    }

    useEffect(() => {
        if (clickCount === 0) setLose(false)
    }, [clickCount])

    return (<div id={"finish-window"}>
        <h2>Борька, {weight} кг свинины в год!</h2>
        <img id={"pig-lose"} src={"../images/pig_finish.png"} title={"Нажимай на меня"} onClick={e => clickOnPig(e)}/>
        <h3>Осталось кликов: {clickCount}</h3>
    </div>)
}