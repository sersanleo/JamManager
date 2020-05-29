Feature: Show results
   As a judge, I want to show everyone the results of a Jam whose winner has already been selected
   
  Scenario: I can see the results of a finished Jam
    Given I am not logged in the system
    When I am viewing the jam "Finished Jam" details
    Then I see the winner is present
   
  Scenario: I can not see the results of an unfinished Jam
    Given I am not logged in the system
    When I am viewing the jam "Inscription Jam" details
    Then I see the winner is not present