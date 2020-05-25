Feature: Invitations Restrictions
   As a jam organizator, I want the system does not accept invitations after the inscription deadline is passed.
   
  Scenario Outline: I can not accept an invitation from a jam that is not an "Incription Jam"
    Given I am logged in the system as "member1" with password "member1"
    When I list invitations
    Then the invitation from the jam <jamName> is not present
    
    Examples: 
      | jamName           |
      | "Pending Jam"     |
      | "In Progress Jam" |
      | "Rating Jam"      |