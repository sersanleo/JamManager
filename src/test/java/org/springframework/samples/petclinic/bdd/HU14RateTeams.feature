Feature: Rate teams
   As a judge, I want to be able to rate teams

  Scenario: I can not rate when not logged in
    Given I am not logged in the system
    When I am viewing the jam "Rating Jam" details
    Then the rate button is not present

  Scenario: I rate a team in a rating jam
    Given I am logged in the system as "judge1" with password "judge1"
    When I am viewing the jam "Rating Jam" details
    And rate a team with a value of "10" and comments "Great work"
    Then the rate button is not present
