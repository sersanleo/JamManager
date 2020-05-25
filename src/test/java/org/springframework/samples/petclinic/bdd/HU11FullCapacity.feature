Feature: Full Capacity
   As a jam organizator, I want the system does not register more groups when the Jam is Full
   
  Scenario: I can not register a group when the Jam is Full
    Given I am logged in the system as "member1" with password "member1"
    When I am viewing the jam "Full Jam" details
    Then the join button is not present