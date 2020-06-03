Feature: Show teams
   As a judge, I want to be able to see the details of every team participating in a jam
   
  Scenario: I can not see the details of a team when not logged in
    Given I am not logged in the system
    When I am viewing the jam "Rating Jam" details
    And I show the team "Grupo 1" details
    Then I can not see its deliveries and marks
   
  Scenario: I can not see the details of a team when logged in as a judge
    Given I am logged in the system as "judge1" with password "judge1"
    When I am viewing the jam "Rating Jam" details
    And I show the team "Grupo 1" details
    Then I can see its deliveries and marks