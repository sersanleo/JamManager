Feature: Rate teams restrictions
   As a judge, I want to only be able to rate teams when a jam has finished and its results still have not been published

  Scenario Outline: I can not rate in a non-rating jam
    Given I am logged in the system as "judge1" with password "judge1"
    When I am viewing the jam <jamName> details
    Then the rate button is not present

    Examples: 
      | jamName           |
      | "In Progress Jam" |
      | "Cancelled Jam"   |
      | "Finished Jam"    |

  Scenario: I can rate in a rating jam
    Given I am logged in the system as "judge1" with password "judge1"
    When I am viewing the jam "Rating Jam" details
    Then the rate button is present
