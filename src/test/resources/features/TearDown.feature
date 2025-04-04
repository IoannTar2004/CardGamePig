Feature: Tear down

  Background:
    * def baseUrl = 'http://localhost:8080'

  Scenario: Remove test player
    Given url baseUrl
    And path '/removeTestPlayer'
    And request {"email": "carate@mail.ru", "password": "admin"}
    When method post
    Then status 200