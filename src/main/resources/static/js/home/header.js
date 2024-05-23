import {BiPencil} from "react-icons/bi";
import { IoMdExit } from "react-icons/io";
import React, {useEffect, useState} from "react";
import {ajax} from "../utils/requests";

export function Header({data, setData}) {
    const [isProfileClicked, setProfileClicked] = useState(false)

    const exit = () => {
        const response = ajax("/exit_home", "GET", {})
        response.onload = () => {
            window.location.pathname = "/"
        }
    }

    useEffect(() => {
        const response = ajax("/get_client", "GET", {})
        response.onload = () => {
            let text = JSON.parse(response.responseText)
            setData(text)
        }

        document.body.addEventListener("click", (e) => {
            if (e.target.id !== "exit" && e.target.id !== "profile") {
                setProfileClicked(false)
            }
        })
    }, [])

    return (
        <header>
            {isProfileClicked ? <div id={"exit"}>
                <div id={"exit-text"} onClick={exit}>Выйти <IoMdExit /></div>
            </div> : ""}
            <img id={"profile"} src={"../images/profile.png"} onClick={() => setProfileClicked(true)}/>
            <div id={"player-data"}>
                <div id={"name-weight"}>
                        <h1 id={"name"}>{data.name}</h1>
                    <h1 id={"weight"}>Вес: {data.weight} кг</h1>
                </div>
                <h5 id={"email"}>{data.email}</h5>
            </div>
            <div id={"rating"}>
                <img className={"cup"} src={"../images/cup.png"}/>
                <h1>Рейтинг: {data.rating}</h1>
                <img className={"cup"} src={"../images/cup.png"}/>
            </div>
        </header>)
}