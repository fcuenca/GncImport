Feature: Classifying Transactions
	I want transactions classified in different spending accounts
	
	Background:
		Given the sample GNC file "checkbook.xml" has been loaded
		And the target account hierarchy has been set to "February 2014"
	    And the sample CSV file "rbc.csv" has been loaded
		
	Scenario: Default classification
		Then all transactions are associated with "Miscelaneous"	
	
	Scenario: Pattern-based automatic classification
	
	
	Scenario: Manual reclassification