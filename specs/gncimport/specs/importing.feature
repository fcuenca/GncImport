Feature: Importing transactions
	I want to import transactions into the accounting file
	
	Background:
	    Given transactions have been exported into "rbc.csv" 
		And accounting data file "checkbook.xml" with default account "Miscelaneous"	
		And the accounting file is loaded
		And the target account hierarchy is set to "February 2014"
		And the source account is set to "Checking Account"
		And the transaction file is loaded	
	
	Scenario: Default import
	    When transactions are imported
	    Then the "Miscelaneous" subaccount of "February 2014" receives 20 new transactions with "Checking Account" as source account
	
	Scenario: editing descriptions before import
		When the description for transaction reading "MONTHLY FEE" is changed to "Bank Fee"
	    And transactions are imported
	    Then a transaction with description "Bank Fee" is imported
	    And no transaction has the description "MONTHLY FEE"

	Scenario: reclassifying transaction before import
		When account for transactions matching "GOODLIFE CLUBS" is set to "Health"
		And transactions are imported 
		Then the "Miscelaneous" subaccount of "February 2014" receives 18 new transactions with "Checking Account" as source account
		And the "Health" subaccount of "February 2014" receives 2 new transactions with "Checking Account" as source account
		
	Scenario: manually ignoring transactions before import
		When transactions matching "MISC PAYMENT" are set to be ignored
		And transactions are imported
	    Then the "Miscelaneous" subaccount of "February 2014" receives 15 new transactions with "Checking Account" as source account
		And no transaction has the description "MISC PAYMENT"
		