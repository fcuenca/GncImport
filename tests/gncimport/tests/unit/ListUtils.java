package gncimport.tests.unit;

import java.util.Arrays;
import java.util.List;

public class ListUtils
{
	public static <T> List<T> list_of(T... elements)
	{
		return Arrays.<T> asList(elements);
	}
}