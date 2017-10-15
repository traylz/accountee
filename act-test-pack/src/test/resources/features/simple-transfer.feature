Feature: Create account

  Background:
    Given account acc1 with initial amount 1000 is created
    And account acc2 with initial amount 2000 is created

  Scenario: Simple transfer
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : "${acc1}",
      "toId"   : "${acc2}",
      "amount" : "100"
    }
    """
    Then response should have status 200
    And account acc1 should have balance = 900
    And account acc2 should have balance = 2100

  Scenario: Transfer back and forth
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : "${acc1}",
      "toId"   : "${acc2}",
      "amount" : "100"
    }
    """
    Then response should have status 200
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : "${acc2}",
      "toId"   : "${acc1}",
      "amount" : "100"
    }
    """
    Then response should have status 200
    And account acc1 should have balance = 1000
    And account acc2 should have balance = 2000

  Scenario: Transfer of full amount
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : "${acc1}",
      "toId"   : "${acc2}",
      "amount" : "1000"
    }
    """
    Then response should have status 200
    And account acc1 should have balance = 0
    And account acc2 should have balance = 3000

