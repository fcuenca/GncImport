Feature: Browsing Transactions
  I want to browse transactions from a bank export file


Scenario: Valid bank export file
    Given the sample file "rbc.csv" has been loaded
    Then the status bar shows a count of 20 transactions
    And the transction grid shows 20 rows
    
    
    