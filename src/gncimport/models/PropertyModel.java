package gncimport.models;

import java.util.List;

public interface PropertyModel
{
	void replaceIgnoreRules(List<String> rules);
	void copyIgnoreRules(List<String> rules);
}
