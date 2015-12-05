Feature: Browsing Transactions
  I want to browse transactions from a bank export file

  Scenario: transactions were exported from RBC Online Banking site
    Given transactions have been exported into "rbc.csv"   
    When the transaction file is loaded
    Then the app displays 20 transactions
    And displayed transactions match those in loaded file
    And transaction data displayed includes:
    	| date 		| description 						| amount 	|
    	| 1/2/2014 	| MISC PAYMENT - IMH POOL I LP 		| -1635.00 	|
    	| 1/6/2014  | MONTHLY FEE - 					| -4.00		|
    	| 1/9/2014	| PAYROLL DEPOSIT - WSIB			| 2283.26	|
    	| 1/22/2014 | Withdrawal - PTB WD --- TA689283	| -100.00	|
    	

