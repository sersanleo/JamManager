Feature: Cancel Jams
   As a jam organizator, I want jams to be cancelled when the minimum amount of teams has not been reached

  Scenario: A jam is cancelled
    Given I am not logged in the system
    When I am viewing the jam "Cancelled Jam" details
    Then I see the jam is cancelled

  Scenario Outline: A jam is not cancelled when the minimum has been reached
    Given I am not logged in the system
    When I am viewing the jam <jamName> details
    Then I see the jam is not cancelled

    Examples: 
      | jamName           |
      | "In Progress Jam" |
      | "Rating Jam"      |
      | "Finished Jam"    |
