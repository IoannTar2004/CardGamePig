import React, {useState} from "react";
import {passwordEqual} from "./startUtils";
import {ajax} from "../utils/requests";

function EnterForm(props) {
    const [enterInputs, setEnterInputs] = useState({
        email: "",
        password: ""
    })
    const send = (e) => {
        e.preventDefault()
        const response = ajax("/enter", "POST", enterInputs)
        response.onload = () => {
            let text = JSON.parse(response.responseText)
            if(text.status === "not found")
                document.getElementById("error-enter").innerText = text.text
            else
                window.location.pathname = "/"
        }
    }

    return (
        <div id={"main-enter"}>
            <div id={"decorations"}>
                <img src={"../images/clubs/k.png"} className={"card minus-30"} id={"card1"} />
                <img src={"../images/diamonds/a.png"} className={"card plus-30"} id={"card2"} />
                <img src={"../images/shirt.png"} className={"card minus-30"} id={"card3"} />
                <img src={"../images/hearts/10.png"} className={"card plus-30"} id={"card4"} />

                <img src={"../images/pig.png"} id={"pig"} onClick={() => document.getElementById('pigAudio').play()}
                     title={"Нажми на меня"} />
                <audio id="pigAudio">
                    <source src={"../audio/pig_sound.mp3"} />
                </audio>
            </div>
            <div className={"form enter-block"}>
                <form onSubmit={send}>
                    <h1>Вход</h1>
                    <div id={"inputs"}>
                        <input type={"text"} className={"form-input"} placeholder={"Почта"} value={enterInputs.email}
                               onChange={(e) => setEnterInputs(prevState => ({...prevState, email: e.target.value}))} required />

                        <input type={"password"} className={"form-input"} placeholder={"Пароль"} value={enterInputs.password}
                               onChange={(e) => setEnterInputs(prevState => ({...prevState, password: e.target.value}))}
                               minLength={6} maxLength={32} required />
                    </div>
                    <h5 id={"error-enter"}></h5>
                    <input type={"submit"} className={"submit enter"} value={"Войти"} />
                </form>
                <br />
                <div id={"transition-button"} onClick={() => props.setFormType("reg")}>Зарегистрироваться</div>
            </div>
        </div>
    )
}

function RegForm(props) {
    const [regInputs, setRegInputs] = useState({
        email: "",
        name: "",
        weight: "",
        password: ""
    })

    const send = (e) => {
        e.preventDefault()
        if (!passwordEqual(regInputs.password))
            return

        const response = ajax("/registration", "POST", regInputs)
        response.onload = () => {
            let text = JSON.parse(response.responseText)
            if(text.status === "account exists")
                document.getElementById("error-email").innerText = text.text
            else
                window.location.pathname = "/"
        }
    }
    return (<div id={"main-reg"}>
        <div id="decorations">
            <img src={"../images/spades/q.png"} className="card minus-30" id="card1" />
            <img src={"../images/diamonds/q.png"} className="card plus-30" id="card2" />
            <img src={"../images/shirt.png"} className="card minus-30" id="card3" />
            <img src={"../images/shirt.png"} className="card plus-30" id="card4" />
        </div>
        <div className={"form registration-block"}>
            <form onSubmit={send}>
                <h1>Регистрация</h1>
                <div id={"inputs"}>
                    <h5 id={"error-email"}></h5>
                    <input type={"email"} className={"form-input"} placeholder={"Почта"} value={regInputs.email} maxLength={64}
                           onChange={e => setRegInputs(prevState => ({...prevState, email: e.target.value}))} required/>

                    <input type={"text"} className={"form-input"} placeholder={"Имя"} maxLength={16} value={regInputs.name}
                           onChange={e => setRegInputs(prevState => ({...prevState, name: e.target.value}))} required />

                    <input type={"number"} className={"form-input number"} placeholder={"Вес"} min={15} max="999" value={regInputs.weight}
                           onChange={e => setRegInputs(prevState => ({...prevState, weight: e.target.value}))} required/>

                    <input id={"pass"} type={"password"} className={"form-input"} placeholder={"Пароль"} minLength={6} maxLength={32}
                           value={regInputs.password} onChange={e => setRegInputs(prevState => ({...prevState, password: e.target.value}))} required />

                    <h5 id="error-passwords"></h5>
                    <input id={"repass"} type={"password"} className={"form-input"} placeholder={"Повторите пароль"} minLength={6}
                           maxLength={32} required />
                </div>
                <input type={"submit"} className={"submit reg"} value={"Зарегистрироваться"} />
            </form>
            <div id={"transition-button"} style={{margin: "-15px"}} onClick={() => props.setFormType("enter")}>Войти</div>
        </div>
    </div>)
}

export function Forms() {
    const [formType, setFormType] = useState("enter")
    if (formType === "enter")
        return <EnterForm setFormType={setFormType}/>
    else
        return <RegForm setFormType={setFormType}/>
}
