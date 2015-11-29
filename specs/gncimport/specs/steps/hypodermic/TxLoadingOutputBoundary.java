package gncimport.specs.steps.hypodermic;

import gncimport.models.TxData;

import java.util.List;

public interface TxLoadingOutputBoundary
{
	void setResponse(List<TxData> txList);
}
