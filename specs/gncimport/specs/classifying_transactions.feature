Feature: Classifying Transactions
	I want transactions classified in different spending accounts
	
	Background:
		Given the default account is "Miscelaneous"
	
	Scenario: Default classification
		Given the sample GNC file "checkbook.xml" has been loaded
		And the target account hierarchy has been set to "February 2014"
	    And the sample CSV file "rbc.csv" has been loaded		
		Then all transactions are associated with "Miscelaneous"	
	
	Scenario: Pattern-based automatic classification
		Given transaction matching rules are defined:
			|txDescription 		            |account |
			|MISC PAYMENT - GOODLIFE CLUBS  |Health  |
			|MISC PAYMENT - IMH POOL I LP   |Housing |
		And the sample GNC file "checkbook.xml" has been loaded
		And the target account hierarchy has been set to "February 2014"
	    And the sample CSV file "rbc.csv" has been loaded		
		Then all transactions matching "MISC PAYMENT - GOODLIFE CLUBS" are associated with the account "Health"
		And all transactions matching "MISC PAYMENT - IMH POOL I LP" are associated with the account "Housing"
		And all unmatched transactions are associated with "Miscelaneous"
	
	Scenario: Manual reclassification