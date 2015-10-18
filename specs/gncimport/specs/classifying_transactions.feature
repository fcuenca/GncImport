Feature: Classifying Transactions
	I want transactions classified in different spending accounts
	
	Background:
	    Given transactions have been exported into "rbc.csv" 
		And accounting data file "checkbook.xml" with default account "Miscelaneous"
	
	Scenario: Default classification
		When the accounting file is loaded
		And the target account hierarchy is set to "February 2014"
	    And the transaction file is loaded		
		Then all transactions are associated with "Miscelaneous"	
	
	Scenario: Pattern-based automatic classification
		Given the following account override rules have been defined:
			|desc                          |override |
			|MISC PAYMENT - GOODLIFE CLUBS |Health   |
			|MISC PAYMENT - IMH POOL I LP  |Housing  |
		When the accounting file is loaded
		And the target account hierarchy is set to "February 2014"
	    And the transaction file is loaded		
		Then all transactions matching "MISC PAYMENT - GOODLIFE CLUBS" are associated with the account "Health"
		And all transactions matching "MISC PAYMENT - IMH POOL I LP" are associated with the account "Housing"
		And all other transactions are associated with "Miscelaneous"
	
	Scenario: Manual reclassification