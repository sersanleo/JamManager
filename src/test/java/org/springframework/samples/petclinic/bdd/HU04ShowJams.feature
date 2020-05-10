Feature: Show Jams
   As a jam organizator, I want people to see every past or future jams details

  Scenario Outline: I can see a jam
    Given I am not logged in the system
    When I list jams
    Then the jam <jamName> exists

    Examples: 
      | jamName           |
      | "Inscription Jam" |
      | "In Progress Jam" |
      | "Finished Jam"    |
      | "Cancelled Jam"   |
