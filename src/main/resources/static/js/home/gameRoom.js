import React, {useState} from "react";
import { IoIosCloseCircleOutline } from "react-icons/io";
import {ajax} from "../utils/requests";

export function GameRoom({gameType, setGameType}) {
    let gameWindow;
    if (gameType === "create")
        gameWindow = <CreateGame />
    else if (gameType === "join")
        gameWindow = <JoinGame />

    return (<div id={"game-create"}>
            <div id={"blur"}></div>
            <div className={"game-window"}>
                <div id={"close"} onClick={() => setGameType("none")}>
                    <IoIosCloseCircleOutline size={25} color={"red"}/>
                </div>
                {gameWindow}
            </div>
        </div>
    )
}

function CreateGame() {
    const [playersCount, setPlayersCount] = useState(2)
    const changeCount = (e) => {
        let elementProps = e.target.getBoundingClientRect()

        if ((e.pageX - elementProps.x) / elementProps.width < 0.5) {
            setPlayersCount(2)
            document.querySelector(".two-players").classList.add("selected")
            document.querySelector(".three-players").classList.remove("selected")
        }
        else {
            setPlayersCount(3)
            document.querySelector(".three-players").classList.add("selected")
            document.querySelector(".two-players").classList.remove("selected")
        }
    }

    const createGame = () => {
        const response = ajax("/createGame", "POST", {count: playersCount})
        response.onload = () => window.location.pathname = "/"
    }
    return (<div>
        <div onClick={e => changeCount(e)} id={"players"}>
            <div className={"player-count-selector two-players selected"}>2 игрока</div>
            <div className={"player-count-selector three-players"}>3 игрока</div>
        </div>
        <button className={"transfer-button"} style={{width: "35%"}} onClick={createGame}>Создать</button>
    </div>)
}

function JoinGame() {
    const[gameIdJoin, setGameIdJoin] = useState(null)
    const [error, setError] = useState("")

    const joinGame = (e) => {
        e.preventDefault()
        const response = ajax("/joinGame", "POST", {gameId: gameIdJoin})
        response.onload = () => {
            const text = JSON.parse(response.responseText)
            if (text.status === "ok")
                window.location.pathname = "/"
            setError(text.status)
        }
    }

    return (<form onSubmit={(e) => joinGame(e)}>
        <h3>Введите id игры</h3>
        <input type={"number"} id={"find-game"} min={1} max={Math.pow(2, 63) - 1}
               onChange={(e) => setGameIdJoin(e.target.value)} required/>
        <input type={"submit"} className={"transfer-button"} style={{width: "55%"}} value={"Присоединиться"} />
        {error !== "" ? <JoinError error={error} setError={setError}/> : ""}
    </form>)
}

function JoinError({error, setError}) {
    return <div className={"game-window"}>
        <h1 style={{marginTop: "20%"}}>{error === "not found" ? "Игра не найдена" : "Игра заполнена!"}</h1>
        <button className={"transfer-button"} onClick={() => setError("")}>Ок</button>
    </div>
}