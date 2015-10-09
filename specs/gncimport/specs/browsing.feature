Feature: Browsing Transactions
  I want to browse transactions from a bank export file

  Scenario: transactions were exported from RBC Online Banking site
    Given the sample CSV file "rbc.csv" has been loaded
    Then the app displays 20 transactions
    And displayed transactions match those in loaded file

