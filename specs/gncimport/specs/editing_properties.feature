Feature: Editing Configuration Properties
	I want to edit the configuration properties for the application
	
	
	Scenario: editing ignore rules
		Given the properties file is initially empty
		When properties are displayed
		Then the app displays empty ignore rule list
		When ignore rules are set to:
		   	|MISC PAYMENT - RBC CREDIT CARD |
	    	|PAYROLL DEPOSIT.* 				|
	    Then the properties file now contains 2 ignore rules   
	    When properties are displayed
	    Then the app displays existing ignore rules:
		   	|MISC PAYMENT - RBC CREDIT CARD |
	    	|PAYROLL DEPOSIT.* 				|
	    	    
	Scenario: editing account override rules
		Given the properties file is initially empty
		When properties are displayed
		Then the app displays empty account override rule list
		When account override rules are set to:
			|Description                     |Account |                    
			|MISC PAYMENT - IMH POOL I LP    |Housing |
			|MISC PAYMENT - GOODLIFE CLUBS   |Health  |
			|SAN CRISTOBAL SEG 1146ROSARIO.* |Casa Cordoba|
	    Then the properties file now contains 3 account override rules   
	    When properties are displayed
		Then the app displays existing account override rules:
			|Description                     |Account |                    
			|MISC PAYMENT - IMH POOL I LP    |Housing |
			|MISC PAYMENT - GOODLIFE CLUBS   |Health  |
			|SAN CRISTOBAL SEG 1146ROSARIO.* |Casa Cordoba|
			
	Scenario: editing transaction rewrite rules
		Given the properties file is initially empty
		When properties are displayed
		Then the app displays empty transaction rewrite rule list
		When transaction override rules are set to:
			|Description                       |Override    |                    
			|MISC PAYMENT - IMH POOL I LP      |Monthly Rent|
			|COINAMATIC - (IDP PURCHASE - \\d+)|Laundry - $1|
	    Then the properties file now contains 2 transaction override rules 
	    When properties are displayed
	 	Then the app displays existing transaction override rules:
			|Description                       |Override    |                    
			|MISC PAYMENT - IMH POOL I LP      |Monthly Rent|
			|COINAMATIC - (IDP PURCHASE - \\d+)|Laundry - $1|
	      
	Scenario: editing monthly account hierarchy template
		Given the properties file is initially empty
		When properties are displayed
		Then the app displays an empty monthly account hierarchy template
		When the monthly account hierarchy template is set to:
			|Order | Account       |
			| 1    | Misc Expenses |
			| 2    | Groceries     |
			| 3    | Car           |                   
	    Then the properties file now contains 3 monthly accounts 
	    When properties are displayed
	 	Then the app displays existing monthly account hierarchy template:
			|Order | Account       |
			| 1    | Misc Expenses |
			| 2    | Groceries     |
			| 3    | Car           |                   
		
			