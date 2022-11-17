Feature: health check

  Background:
    * url url + '/example/'

  Scenario: Display health-check status when application is UP
    Given path 'health'
    When method get
    Then status 200
    * print response
    And match response.status == "UP"