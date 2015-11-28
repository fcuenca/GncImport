package gncimport.specs.steps.hypodermic;

import gncimport.models.TxData;

import java.util.List;

public interface ImportOutputBoundary
{
	void setResponse(List<TxData> txList);
}
