Feature: Protection from duplicate transfers

  Background:
    Given account acc1 with initial amount 1000 is created
    And account acc2 with initial amount 2000 is created

  Scenario: Duplicate transfer should be rejected
    When user generates unique id transferId1
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : ${acc1},
      "toId"   : ${acc2},
      "amount" : "100",
      "transferId" : "${transferId1}"
    }
    """
    Then response should have status 200
    And account acc1 should have balance = 900
    And account acc2 should have balance = 2100

    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : ${acc1},
      "toId"   : ${acc2},
      "amount" : "100",
      "transferId" : "${transferId1}"
    }
    """
    Then response should have status 400
    And response should have body:
    """
    Bad request : Transfer '${transferId1}' was already performed
    """
    And account acc1 should have balance = 900
    And account acc2 should have balance = 2100

  Scenario: Duplicate transfer of full amount should be rejected as duplicate and not by
    When user generates unique id transferId1
    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : ${acc1},
      "toId"   : ${acc2},
      "amount" : "1000",
      "transferId" : "${transferId1}"
    }
    """
    Then response should have status 200
    And account acc1 should have balance = 0
    And account acc2 should have balance = 3000

    When user calls POST /transfer/perform with following body:
    """
    {
      "fromId" : ${acc1},
      "toId"   : ${acc2},
      "amount" : "1000",
      "transferId" : "${transferId1}"
    }
    """
    Then response should have status 400
    And response should have body:
    """
    Bad request : Transfer '${transferId1}' was already performed
    """
    And account acc1 should have balance = 0
    And account acc2 should have balance = 3000
