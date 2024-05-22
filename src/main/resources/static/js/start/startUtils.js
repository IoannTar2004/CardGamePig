export function backgroundChange() {
    let color = 1

    function changeBackground() {
        let colors = ["yellow", "black", "blue", "green", "yellow"]
        let back = document.getElementById("background")
        let j = 0
        for (let i = 0; i <= 200; i++) {
            setTimeout(() => {
                if (i < 100)
                    j++
                back.style.backgroundImage = `linear-gradient(to top, ${colors[color]} ${-100 + i}%, ${colors[color-1]} ${j}%)`
            },20 * i)
        }
        setTimeout(() => {
            color++
            if (color === colors.length)
                color = 1
        }, 4000)
    }

    function changeLines() {
        let colors = ["pink", "red", "yellow", "black"]
        let lines = document.querySelectorAll(".line.move")
        lines.forEach(e => e.style.backgroundColor = colors[color - 1])
        for (let i = 0; i <= 100; i++) {
            setTimeout(() => {
                lines.forEach(e => e.style.width = `${i}vw`)
            },40 * i)
        }
        setTimeout(() => {
            document.querySelectorAll(".line.static").forEach(e => e.style.backgroundColor = colors[color - 1])
        }, 3999)
    }

    changeLines()
    changeBackground()

    setInterval(changeBackground, 7000)
    setInterval(changeLines, 7000)

    document.body.addEventListener("click", e => {
        if (!e.target.classList.contains("submit")) {
            document.querySelectorAll("h5").forEach(e => e.innerText = "")
        }
    })
}

export function passwordEqual(pass) {
    let equal = pass === document.getElementById("repass").value
    if (!equal)
        document.getElementById("error-passwords").innerText = "Пароли не совпадают!"
    return equal
}


