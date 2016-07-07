package gncimport.tests.unit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import gncimport.transfer.MatchingText;
import gncimport.transfer.UserEnteredMatchingText;
import gncimport.utils.ProgrammerError;

import org.junit.Test;

public class MatchingTextTests
{	
	@Test
	public void valid_regex_makes_a_valid_rule()
	{
		MatchingText rule = new UserEnteredMatchingText("rule text.*");
		
		assertThat(rule.isValid(), is(true));
		assertThat(rule.text(), is("rule text.*"));
		assertThat(rule.hint(), is(""));
		assertThat(rule.displayText(), is(rule.text()));
	}
	
	@Test
	public void valid_rules_are_trimmed()
	{
		MatchingText rule = new UserEnteredMatchingText("   rule text    ");
		
		assertThat(rule.text(), is("rule text"));
	}
	
	@Test
	public void empty_string_makes_an_invalid_rule()
	{
		MatchingText rule = new UserEnteredMatchingText("");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is(""));
		assertThat(rule.displayText(), is("<<>>"));
		assertThat(rule.hint(), is("Empty string is invalid"));
	}

	@Test
	public void blank_space_makes_an_invalid_rule()
	{
		MatchingText rule = new UserEnteredMatchingText("   ");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is("   "));
		assertThat(rule.displayText(), is("<<   >>"));
		assertThat(rule.hint(), is("Empty string is invalid"));
	}
	
	@Test
	public void invalid_regex_makes_an_invalid_rule()
	{
		MatchingText rule = new UserEnteredMatchingText("(missing bracket");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is("(missing bracket"));
		assertThat(rule.displayText(), is("<<(missing bracket>>"));
		assertThat(rule.hint(), is("Invalid regex: Unclosed group"));
	}
	
	@Test(expected=ProgrammerError.class)
	public void null_is_rejected()
	{
		new UserEnteredMatchingText(null); // will throw
	}
	
	@Test
	public void copying_valid_rule_definition()
	{
		MatchingText rule = new UserEnteredMatchingText("rule");
		
		MatchingText copy = rule.copy();
		
		assertThat(copy.isValid(), is(true));
		assertThat(copy.text(), is(rule.text()));
		assertThat(copy.displayText(), is(rule.displayText()));
		assertThat(copy.hint(), is(rule.hint()));
	}
	
	@Test
	public void copying_invalid_rule_definition()
	{
		MatchingText rule = new UserEnteredMatchingText("");
		
		MatchingText copy = rule.copy();
		
		assertThat(copy.isValid(), is(false));
		assertThat(copy.text(), is(rule.text()));
		assertThat(copy.displayText(), is(rule.displayText()));
		assertThat(copy.hint(), is(rule.hint()));
	}
	
	@Test
	public void can_determine_regex_match()
	{
		MatchingText rule = new UserEnteredMatchingText("ab(c+)d.*");
		
		assertThat(rule.matches("abccccdx"), is(true));
		assertThat(rule.matches("abXd"), is(false));
	}
	
	@Test
	public void whitespace_is_ignore_when_matching_rules()
	{
		MatchingText rule = new UserEnteredMatchingText("abcd");
		
		assertThat(rule.matches("  abcd   "), is(true));
	}


}
