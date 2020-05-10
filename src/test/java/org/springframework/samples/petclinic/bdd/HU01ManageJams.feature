Feature: Jam management
   I want to create Jams and edit/delete them (as long as they are still in inscription)

  Scenario: I can not create a Jam when not logged in
    Given I am not logged in the system
    When I list jams
    Then the create button is not present

  Scenario Outline: I create a Jam
    Given I am logged in the system as "jamOrganizator1" with password "jamOrganizator1"
    When I create a new Jam with name <name>, description <description>, difficulty <difficulty>, inscription deadline <inscriptionDeadline>,	max team size <maxTeamSize>, min. teams <minTeams>, max. teams <maxTeams>, start <start> and end <end>
    Then the jam <name> is created

    Examples: 
      | name               | description    | difficulty | inscriptionDeadline | maxTeamSize | minTeams | maxTeams | start             | end               |
      | "Test Jam"         | "A test"       | "5"        | "2030-6-15 12:00"   | "5"         | "5"      | "5"      | "2030-6-17 12:00" | "2030-6-18 12:00" |
      | "Another Test Jam" | "Another test" | "1"        | "2035-6-15 12:00"   | "1"         | "10"     | "15"     | "2035-6-17 12:00" | "2035-6-18 12:00" |

  Scenario Outline: I edit a Jam
    Given I am logged in the system as "jamOrganizator1" with password "jamOrganizator1"
    When I change the jam name <oldName> to <newName>
    Then the name is changed from <oldName> to <newName>

    Examples: 
      | oldName    | newName     |
      | "Test Jam" | "Test Jams" |

  Scenario: I can not edit a Jam when not logged in
    Given I am not logged in the system
    When I am viewing the jam "Test Jams" details
    Then the edit button is not present

  Scenario Outline: I can not edit a Jam which is not in inscription
    Given I am logged in the system as "jamOrganizator1" with password "jamOrganizator1"
    When I am viewing the jam <jamName> details
    Then the edit button is not present

    Examples: 
      | jamName           |
      | "Pending Jam"     |
      | "In Progress Jam" |
      | "Rating Jam"      |

  Scenario Outline: I delete a Jam
    Given I am logged in the system as "jamOrganizator1" with password "jamOrganizator1"
    When I delete the jam named <name>
    Then the jam <name> is deleted

    Examples: 
      | name        |
      | "Test Jams" |

  Scenario: I can not delete a Jam when not logged in
    Given I am not logged in the system
    When I am viewing the jam "Inscription Jam" details
    Then the delete button is not present

  Scenario Outline: I can not delete a Jam which is not in inscription
    Given I am logged in the system as "jamOrganizator1" with password "jamOrganizator1"
    When I am viewing the jam <jamName> details
    Then the delete button is not present

    Examples: 
      | jamName           |
      | "Pending Jam"     |
      | "In Progress Jam" |
      | "Rating Jam"      |
