import React, {useEffect, useState} from "react";
import {send, subscribe, webSocketConnect} from "../game/connection";

export function Table({playerId, show}) {
    const [players, setPlayers] = useState({top: [], my: [], index: 0})
    const [hook, setHook] = useState(false)

    const getPlayersTable = () => {
        let totalPlayers = []
        const list = players[show]
        for (let i = 0; i < list.length; i++) {
            totalPlayers.push(<tr id={show === "my" && list[i].playerId === playerId ? "your-row" : ""}>
                <td>{show === "top" ? i + 1 : i + players.index}</td>
                <td>{list[i].name}</td>
                <td>{list[i].weight}</td>
                <td>{list[i].rating.toLocaleString()}</td>
                <td>{calculateLastGameTime(list[i].lastGame)}</td>
            </tr>)
        }
        return totalPlayers
    }

    useEffect(() => {
        const updatePlayers = (data) => {
            data = JSON.parse(data)
            setPlayers({top: data.top.reverse(), my: data.my.reverse(), index: data.index})
        }
        webSocketConnect(playerId, () => {
            subscribe("/players/home/getClients", (data) => {
                updatePlayers(data)
            })
            subscribe("/home/ratings", (data) => {
                updatePlayers(data)
            })
            send({}, "getClients")
        })

        const interval = setInterval(() => setHook(prevState => !prevState), 1000);
        return () => clearInterval(interval)

    }, [])

    return (
        <table className={"rating-table"}>
            <thead>
            <tr>
                <th>№</th>
                <th style={{width: "200px"}}>Игрок</th>
                <th style={{width: "80px"}}>Вес</th>
                <th style={{width: "130px"}}>Рейтинг</th>
                <th style={{width: "200px"}}>Последняя игра</th>
            </tr>
            </thead>
            <tbody>
                {getPlayersTable().map(e => e)}
            </tbody>
        </table>
    )
}

function calculateLastGameTime(time) {
    const s = 1000, m = 60 * 1000, h = 60 * m, d = 24 * h, w = 7 * d, mon = 30 * d, y = 12 * mon
    const last = Date.now() - new Date(time).getTime()

    if (last >= 0 && last < m) {
        const seconds = Math.floor(last / s)
        return seconds + " " + wordForm("секунда", seconds) + " назад"
    }
    if (last >= m && last < h) {
        const minutes = Math.floor(last / m)
        return minutes + " " + wordForm("минута", minutes) + " назад"
    }
    if (last >= h && last < d) {
        const hours = Math.floor(last / h)
        return hours + " " + wordForm("час", hours) + " назад"
    }
    if (last >= d && last < w) {
        const days = Math.floor(last / d)
        return days + " " + wordForm("день", days) + " назад"
    }
    if (last >= w && last < mon) {
        const weeks = Math.floor(last / w)
        return weeks + " " + wordForm("неделя", weeks) + " назад"
    }
    if (last >= mon && last < y) {
        const months = Math.floor(last / mon)
        return months + " " + wordForm("месяц", months) + " назад"
    }
    if (last >= y)
        return "Давно"
}

function wordForm(word, count) {
    if (count % 10 === 1 && count !== 11)
        return word

    const forms = {
        "секунда": ["секунды", "секунд"],
        "минута": ["минуты", "минут"],
        "час": ["часа", "часов"],
        "день": ["дня", "дней"],
        "неделя": ["недели"],
        "месяц": ["месяца", "месяцев"],
    }

    const index = count % 10 >= 2 && count % 10 < 5 && (count < 11 || count > 15) ? 0 : 1
    return forms[word][index]
}