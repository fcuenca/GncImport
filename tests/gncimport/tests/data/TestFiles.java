package gncimport.tests.data;

public class TestFiles
{
	public static final String CSV_1_TEST_FILE = getFilePath("rbc.csv");
	public static final String CSV_2_TEST_FILE = getFilePath("rbc-visa.csv");
	public static final String CFG_WITH_MATCHING_RULES = getFilePath("matching-rules.properties");
	public static final String GNC_TEST_FILE = getFilePath("checkbook.xml");
	public static final String CFG_WITH_LAST_LOCATIONS = getFilePath("last-location.properties");
	public static final String CFG_WITH_INVALID_LOCATIONS = getFilePath("invalid-location.properties");

	private static String getFilePath(String fileName)
	{
		return TestFiles.class.getResource(fileName).getPath();
	}
}
