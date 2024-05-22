import {subscribe} from "./connection";
import {getRelativeDeck, removeCard} from "./cardManager";

export class MoveSubscriptions {
    static state
    static display

    static setSubscriptions(setState, setDisplays, count) {
        subscribe("/players/game/click", (data) => {
            data = JSON.parse(data)
            setState(prevState => ({...prevState, clicked: true, clickedCard: data["card"]}))    //клик по карте в колоде
            removeCard(data["cardPos"])
        })

        subscribe("/players/game/put",(data) => {
            data = JSON.parse(data)
            const finalPos = getRelativeDeck(data["gamePos"], this.state.position, count)

            const playerCard = document.getElementById(String(finalPos))    //положить в чью-то колоду
            playerCard.style.display = ""
            playerCard.src = `../../images/${this.state.clickedCard}.png`

            if (data["gamePos"] - this.state.current === 0)
                setState(prevState => ({...prevState, current: (this.state.current + 1) % count}))

            setState(prevState => ({...prevState, clicked: false, clickedCard: null}))
        })

        subscribe("/players/game/take",(data) => {   //взять карту из своей колоды
            data = JSON.parse(data)
            const finalPos = getRelativeDeck(this.state.current, this.state.position, count)
            const deck = document.getElementById(finalPos)
            setState(prevState => ({...prevState, clicked: true, clickedCard: data["card"]}))

            if(data["subCard"] === "none")
                deck.style.display = "none"
            else
                deck.src = `../../images/${data["subCard"]}.png`
        })

        subscribe("/players/game/turn", () => {
            const finalPos = getRelativeDeck(this.state.current, this.state.position, count)
            this.display.upsideCard[finalPos] = true
            setDisplays({upsideCard: this.display.upsideCard})
            document.getElementById(finalPos).style.display = "none"
        })

        subscribe("/players/game/clickOnPlayerDeck", (data) => {
            data = JSON.parse(data)
            setState(prevState => ({...prevState, clicked: true, clickedCard: data["card"]}))    //клик по карте в перевернутой колоде игрока
            const finalPos = getRelativeDeck(this.state.current, this.state.position, count)
            if (data["last"]) {
                this.display.upsideCard[finalPos] = false
                setDisplays({upsideCard: this.display.upsideCard})
            }
        })
    }
}