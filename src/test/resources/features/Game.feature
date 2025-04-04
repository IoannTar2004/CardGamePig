Feature: Game testing

  Background:
    * def baseUrl = 'http://localhost:8080'

  Scenario: Game
    * configure driver = { type: 'chrome', headless: false, showDriverLog: true, args: ['--window-size=1920,1080']}

    Given driver baseUrl
    * maximize()
    * input("input[placeholder=Почта]", "ivan@ya.ru")
    * input("input[placeholder=Пароль]", "121212")
    * click("input[type=submit]")
    * delay(1000)

    * click("//button[text()='Создать игру']")
    * click("//button[text()='Создать']")
    * delay(1000)
    * reload()

    * configure retry = { count: 15, interval: 1000 }
    * waitFor(".logic-button.start-button:enabled").click()
    * click("#card34.your-move")
    * click("#frame0")
    * delay(5000)
    * karate.repeat(33, function(i) {click(".click-card"); click("#frame1"); delay(40)})
    * click("#bottom img")
    * click("#frame1")
    * waitFor(".continue:enabled").click()
    * click(".logic-button.leave-button")
    * delay(5000)