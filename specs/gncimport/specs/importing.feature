Feature: Importing transactions
	I want to import transactions into the accounting file
	
	Scenario: Default import
	    Given transactions have been exported into "rbc.csv"   
		And accounting data file "checkbook.xml" with default account "Miscelaneous"
		And the accounting file is loaded
		And the target account hierarchy is set to "February 2014"
		And the source account is set to "Checking Account"
	    And the transaction file is loaded	
	    When transactions are imported
	    Then the "Miscelaneous" subaccount of "February 2014"  receives 20 new transactions	
	