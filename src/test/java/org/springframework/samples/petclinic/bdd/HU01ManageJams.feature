Feature: Jam management
   I want to create Jams and edit/remove them (as long as they are still in inscription)

  Scenario: I cannot create a Jam
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
      | oldName           | newName            |
      | "Inscription Jam" | "Inscription Jams" |

  Scenario Outline: I remove a Jam
    Given I am logged in the system as "jamOrganizator1" with password "jamOrganizator1"
    When I delete the jam named <name>
    Then the jam <name> is removed

    Examples: 
      | name               |
      | "Inscription Jams" |
