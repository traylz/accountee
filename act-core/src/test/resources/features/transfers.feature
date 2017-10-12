Feature: Transfer from one account to another

  Background:
    Given Database contains following accounts:
      | id | amount |
      | 1  | 100000 |
      | 2  | 200000 |


  Scenario: Simple transfer of 10k
    When Transfer from account #1 to account #2 for amount 10000 is performed
    Then Transfer should be performed successfully
    And Database should contain following accounts:
      | id | amount |
      | 1  | 90000  |
      | 2  | 210000 |

  Scenario: Transfer of full amount
    When Transfer from account #1 to account #2 for amount 100000 is performed
    Then Transfer should be performed successfully
    And Database should contain following accounts:
      | id | amount |
      | 1  | 0      |
      | 2  | 300000 |

  Scenario: Transfer of negative amount
    When Transfer from account #1 to account #2 for amount -100000 is performed
    Then Transfer should fail with exception com.gsobko.act.ActUserException and message:
      """
      Transfer amount should be positive, but got -100000.0
      """

  Scenario: Insufficient funds
    When Transfer from account #1 to account #2 for amount 110000 is performed
    Then Transfer should fail with exception com.gsobko.act.ActUserException and message:
      """
      Not enough funds on account to transfer 110000.0
      """

  Scenario: Duplicate transfer should be rejected
    When Transfer from account #1 to account #2 for amount 10000 is performed with transferId="someUniqueId"
    Then Transfer should be performed successfully
    And Database should contain following accounts:
      | id | amount |
      | 1  | 90000  |
      | 2  | 210000 |
    When Transfer from account #1 to account #2 for amount 10000 is performed with transferId="someUniqueId"
    Then Transfer should fail with exception com.gsobko.act.ActUserException and message:
      """
      Transfer 'someUniqueId' was already performed
      """