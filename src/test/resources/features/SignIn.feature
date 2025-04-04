Feature: sign in

  Background:
    * def baseUrl = 'http://localhost:8080'

  Scenario: Registration
    Given url baseUrl
    And path '/registration'
    And request {"email": "carate@mail.ru", "password": "121212", "name": "carate", "weight": "75"}
    When method post
    Then match response.status == 'ok'

  Scenario: Login
    Given url baseUrl
    And path '/enter'
    And request {"email": "carate@mail.ru", "password": "121212"}
    When method post
    Then match response.status == 'ok'

  Scenario: Registration failed
    Given url baseUrl
    And path '/registration'
    And request {"email": "carate@mail.ru", "password": "121213", "name": "sumo", "weight": "150"}
    When method post
    Then match response.status == 'account exists'

  Scenario: Login failed
    Given url baseUrl
    And path '/enter'
    And request {"email": "sumo@mail.ru", "password": "121212"}
    When method post
    Then match response.status == 'not found'