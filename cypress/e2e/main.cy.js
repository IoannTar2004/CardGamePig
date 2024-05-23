const URL = 'http://localhost:8080/'
const delay = 1000

describe('menu testing', () => {
  beforeEach(() => {
    cy.viewport(1600, 900)
    cy.session("enter",() => {
      cy.visit(URL)

      cy.get('input[placeholder="Почта"').type("ivan@ya.ru")
      cy.get('input[placeholder="Пароль"').type("121212")
      cy.get('input[type="submit"]').click()
    })
  })

  it('enter', () => {
    cy.visit(URL)
    cy.get("#name").should("have.text", "IoannTar2004")
  })

  it('create game', () => {
    cy.visit(URL)
    cy.wait(delay)
    cy.get(".menu-button.create-game").click()
    cy.wait(delay)
    cy.get(".transfer-button").click()
    cy.wait(delay)
    cy.get(".player.connected", {timeout: 60000}).should("have.length", 2)
    cy.wait(2 * delay)
    cy.get(".logic-button.start-button").click()
    cy.get("#name0").should("have.text", "IoannTar2004")

    cy.get("#card34").click()
    cy.get("#card34").should("not.exist")
    cy.get("#frame1").should("exist")
    cy.wait(delay / 2)
    cy.get("#frame1").click()
    cy.get("#frame1").should("not.exist")
    cy.get("#1").should("exist")

    cy.wait(delay / 2)
    cy.get("#card33").click()
    cy.get("#card33").should("not.exist")
    cy.wait(delay / 2)
    cy.get("#frame3").click()
    cy.get("#frame3").should("not.exist")

    cy.wait(delay / 2)
    cy.get("#card32").click()
    cy.wait(delay / 2)
    cy.get("#frame0").click()

    for (let i = 0; i < 33; i++) {
      cy.get("#move-card", {timeout: 30000}).should("exist")
      cy.get("#move-card", {timeout: 30000}).should("not.exist")
    }

    cy.get("#pig-lose").should("exist")
    for (let i = 0; i < 10; i++) {
      cy.get("#pig-lose").click()
      cy.wait(delay)
    }
    cy.get(".player-stat").should("exist")
    cy.wait(delay)
    cy.reload()
    cy.wait(delay)
    cy.get(".logic-button.leave-button").click()
  })

  it("join", () => {
    cy.visit(URL)
    cy.wait(delay)
    cy.get(".menu-button.join-game").click()
    cy.get("#find-game").type("1")
    cy.wait(delay)
    cy.get("input[type='submit']").click()
    cy.get(".game-window").children("h1").should("have.text", "Игра не найдена")
  })

  it("rating", () => {
    cy.visit(URL)
    cy.get(".menu-button.my-rating").click()
    cy.get(".menu-button.my-rating").should("have.text", "Топ-рейтинг")
    cy.wait(delay)
    cy.get(".menu-button.my-rating").click()
    cy.get(".menu-button.my-rating").should("have.text", "Мой рейтинг")
  })

  it("close", () => {
    cy.visit(URL)
    cy.wait(delay)
    cy.get("#profile").click()
    cy.wait(delay)
    cy.get("#exit-text").click()
    cy.url().should('eq', "http://localhost:8080/templates/index.html")
  })
})