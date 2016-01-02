Feature: Editing Configuration Properties
	I want to edit the configuration properties for the application
	
	
	Scenario: editing ignore rules
		Given the properties file is initially empty
		When properties are edited
		Then the app displays empty ignore rule list
		And ignore rules are set to:
		   	|MISC PAYMENT - RBC CREDIT CARD |
	    	|PAYROLL DEPOSIT.* 				|
	    Then the properties file now contains 2 ignore rules   
	    When properties are edited
	    Then the app displays existing ignore rules:
		   	|MISC PAYMENT - RBC CREDIT CARD |
	    	|PAYROLL DEPOSIT.* 				|
	    
	    
		