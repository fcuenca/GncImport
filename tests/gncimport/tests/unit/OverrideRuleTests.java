package gncimport.tests.unit;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import gncimport.transfer.OverrideRule;

import org.junit.Test;

public class OverrideRuleTests
{	
	@Test
	public void is_valid_when_text_and_override_are_not_blank()
	{
		OverrideRule rule = new OverrideRule("some text", "some override");
		
		assertThat(rule.isValid(), is(true));
	}
	
	@Test
	public void is_invalid_when_text_is_blank()
	{
		OverrideRule rule = new OverrideRule("", "some override");
		
		assertThat(rule.isValid(), is(false));
	}
	
	@Test
	public void is_invalid_when_override_is_blank()
	{
		OverrideRule rule = new OverrideRule("some text", "");
		
		assertThat(rule.isValid(), is(false));
	}
		
	@Test
	public void matching_is_done_against_text()
	{
		OverrideRule rule = new OverrideRule("abc", "xyz");
		
		assertThat(rule.matches("abc"), is(true));
		assertThat(rule.matches("xyz"), is(false));
	}
	
	@Test
	public void replaces_occurrences_of_matchint_text_with_override_text_when_is_applied()
	{
		OverrideRule rule = new OverrideRule("abc", "xyz");
		
		String text = "some.abc.text.abc.end";
		
		assertThat(rule.applyOverrideTo(text), is("some.xyz.text.xyz.end"));
	}
	
	@Test
	public void will_return_the_override_text_when_the_rule_is_a_match()
	{
		OverrideRule rule = new OverrideRule("matching text", "override");
		
		assertThat(rule.textForPossitiveMatch(), is("override"));
	}



}
