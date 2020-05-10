Feature: Show Jam Resources
   As a jam organizator, I want the jam resources to only be shown when said jam is in progress

  Scenario Outline: I can see the resources of jams
    Given I am logged in the system as "jamOrganizator1" with password "jamOrganizator1"
    When I am viewing the jam <jamName> details
    Then I can see the resources

    Examples: 
      | jamName           |
      | "Inscription Jam" |
      | "Pending Jam"     |
      | "Finished Jam"    |

  Scenario Outline: I can not see the resources of not in progress jams when not logged in
    Given I am not logged in the system
    When I am viewing the jam <jamName> details
    Then I can not see the resources

    Examples: 
      | jamName           |
      | "Inscription Jam" |
      | "Pending Jam"     |
      | "Finished Jam"    |

  Scenario: I can see the resources of in progress jams when not logged in
    Given I am not logged in the system
    When I am viewing the jam "In Progress Jam" details
    Then I can see the resources
