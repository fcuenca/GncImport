Feature: Browsing Transactions
  I want to browse transactions from a bank export file

  Scenario: transactions were exported from RBC Online Banking site
    Given transactions have been exported into "rbc.csv"   
    When the transaction file is loaded
    Then the app displays 20 transactions
    And displayed transactions match those in loaded file

