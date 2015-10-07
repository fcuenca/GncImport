Feature: Browsing Transactions
  I want to browse transactions from a bank export file

  Scenario: transactions were exported from RBC Online Banking site
    Given the sample CSV file "rbc.csv" has been loaded
    Then the status bar shows a count of 20 transactions
    And the transction grid shows 20 rows
    And displayed transactions match those in loaded file

