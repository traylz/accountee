Feature: Transfer negative scenarios - validations etc

  Background:
    Given account acc1 with initial amount 1000 is created
    And account acc2 with initial amount 2000 is created

  Scenario: Transfer of negative amount
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : ${acc1},
      "toId"   : ${acc2},
      "amount" : "-10"
    }
    """
    Then response should have status 400
    And response should have body:
    """
    Bad request : Transfer amount should be positive, but got -10
    """
    And account acc1 should have balance = 1000
    And account acc2 should have balance = 2000


  Scenario: Insufficient funds
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : ${acc1},
      "toId"   : ${acc2},
      "amount" : "1001"
    }
    """
    Then response should have status 400
    And response should have body:
    """
    Bad request : Not enough funds on account to transfer 1001
    """
    And account acc1 should have balance = 1000
    And account acc2 should have balance = 2000

  Scenario: Non-existen account
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : ${acc1},
      "toId"   : -1,
      "amount" : "100"
    }
    """
    Then response should have status 400
    And response should have body:
    """
    Bad request : Cannot find account by id -1
    """
    And account acc1 should have balance = 1000
