package gncimport.ui;

import gncimport.models.AccountData;

import java.util.ArrayList;
import java.util.List;

public class CandidateAccList
{
	public static final AccountData OTHER_ACC_PLACEHOLDER = new AccountData("<< OTHER >>", "-1");

	public static List<AccountData> build(List<AccountData> theAccList)
	{
		ArrayList<AccountData> candidateAccs = new ArrayList<AccountData>();
		
		candidateAccs.addAll(theAccList);
		candidateAccs.add(CandidateAccList.OTHER_ACC_PLACEHOLDER);
		
		return candidateAccs;
	}
}