package gncimport.tests.data;

public class TestFiles
{
	public static final String CSV_1_TEST_FILE = getFilePath("rbc.csv");
	public static final String CSV_2_TEST_FILE = getFilePath("rbc-visa.csv");
	public static final String CFG_WITH_MATCHING_RULES = getFilePath("matching-rules.properties");
	public static final String CFG_WITH_REWRITE_RULES = getFilePath("rewrite-rules.properties");
	public static final String GNC_TEST_FILE = getFilePath("checkbook.xml");
	public static final String CFG_WITH_LAST_LOCATIONS = getFilePath("last-location.properties");
	public static final String CFG_WITH_INVALID_LOCATIONS = getFilePath("invalid-location.properties");
	public static final String CFG_WITH_MONTHLY_ACCOUNTS = getFilePath("monthly-accounts.properties");

	public static final String SPECIAL_EXPENSES_ID = "1eb826d327e81be51e663430f5b7adb9";
	public static final String SUPPLIES_FEB_ID = "2c6be57ad1474692f6f569384668c4ac";
	public static final String FEBRERO2014_ID = "882f951395a92f8ea103fe0e9dbfbda5";
	public static final String ENERO2014_ID = "454018fbf408f8c3e607bd51f22c5373";
	public static final String CHECKINGACC_ID = "64833494284bad5fb390e84d38c65a54";
	public static final String EXPENSES_FEBRERO_ID = "1edf8498fcda9b8a677160b0b6357287";
	public static final String EXPENSES_ENERO_ID = "e31486ad3b2c6cdedccf135d13538b29";
	public static final String YEAR2014_ID = "eeab2d12582428bbc3e10da3a12520fc";

	public static String getFilePath(String fileName)
	{
		return TestFiles.class.getResource(fileName).getPath();
	}
}
