package gncimport.transfer;


public class OverrideRule implements TransactionRule
{
	public final MatchingRule textToMatch;
	public final MatchingRule override;
	
	public OverrideRule(String matchingTxt, String overrideTxt)
	{
		this.textToMatch = new UserEnteredMatchingRule(matchingTxt);
		this.override = new UserEnteredMatchingRule(overrideTxt);
	}
	
	public OverrideRule(MatchingRule textToMatch, MatchingRule overrideText)
	{
		this.textToMatch = textToMatch;
		this.override = overrideText;
	}

	@Override
	public boolean isValid()
	{
		return textToMatch.isValid() && override.isValid();
	}
	
	@Override
	public boolean matches(String someText)
	{
		return textToMatch.matches(someText);
	}
	
	public String applyOverrideTo(String txDescription)
	{
		String trimmedTxDesc = txDescription.trim();
		return trimmedTxDesc.replaceAll(textToMatch.text(), override.text());		
	}

	@Override
	public String toString()
	{
		return "OverrideRule [textToMatch=" + textToMatch + ", override=" + override + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((override == null) ? 0 : override.hashCode());
		result = prime * result + ((textToMatch == null) ? 0 : textToMatch.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OverrideRule other = (OverrideRule) obj;
		if (override == null)
		{
			if (other.override != null)
				return false;
		}
		else if (!override.equals(other.override))
			return false;
		if (textToMatch == null)
		{
			if (other.textToMatch != null)
				return false;
		}
		else if (!textToMatch.equals(other.textToMatch))
			return false;
		return true;
	}
}