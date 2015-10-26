Feature: Ignoring transactions
	I want to be able to ignore transacions during the import
	
	Scenario: By default nothing is ignored
	    Given transactions have been exported into "rbc.csv"   
    	When the transaction file is loaded
    	Then the number of ignored transactions is 0
		
	Scenario: Some transactions are to be ignored automatically
	    Given transactions have been exported into "rbc.csv"
	    And the following ignore rules have been defined:
	    	|MISC PAYMENT - RBC CREDIT CARD |
	    	|PAYROLL DEPOSIT.* 				|   
    	When the transaction file is loaded
    	Then the number of ignored transactions is 3
	