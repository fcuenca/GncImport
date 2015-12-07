Feature: Creating accounts
	I want to be able to create the standard set of accounts used to classify monthly spending
	
	Scenario: creating standard account set
		Given accounting data file "checkbook.xml" with default account "Miscelaneous"	
		And default sub-account list:
			| order | account 		|
			| 1 	| Miscelaneous	|
			| 2		| Housing	 	|
			| 3		| Groceries 	| 
			| 4		| Clothing		|
			| 5		| Supplies		|
			| 6		| Entertainment	|
			| 7		| Casa Cordoba	|
			| 8		| Books			|
			| 9		| Health		|
			| 10	| Car			|
			| 11	| Expenses		|
		And the accounting file is loaded
		When accounts for "March" are created under "Monthly Expenses, Year 2014" with the name "March 2014"
		Then a new account hierarchy "March 2014" is created under "Monthly Expenses, Year 2014" with code "420140300" and subaccounts:
			| code 		| account 		|
			| 420140301	| Miscelaneous	|
	