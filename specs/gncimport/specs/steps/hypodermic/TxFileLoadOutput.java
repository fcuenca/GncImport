package gncimport.specs.steps.hypodermic;

import gncimport.models.TxData;

import java.util.List;

public interface TxFileLoadOutput
{
	void setResponse(List<TxData> txList);
}
