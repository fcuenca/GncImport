package gncimport.tests.unit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.EditableMatchingRule;
import gncimport.transfer.ScreenValue;
import gncimport.utils.ProgrammerError;

import org.junit.Test;

public class MatchingRuleTests
{	
	@Test
	public void valid_regex_makes_a_valid_rule()
	{
		MatchingRule rule = new EditableMatchingRule("rule text.*");
		
		assertThat(rule.isValid(), is(true));
		assertThat(rule.text(), is("rule text.*"));
		assertThat(rule.asScreenValue().hint(), is(""));
		assertThat(rule.asScreenValue().displayText(), is(rule.text()));
	}
	
	@Test
	public void valid_rules_are_trimmed()
	{
		MatchingRule rule = new EditableMatchingRule("   rule text    ");
		
		assertThat(rule.text(), is("rule text"));
	}
	
	@Test
	public void empty_string_makes_an_invalid_rule()
	{
		MatchingRule rule = new EditableMatchingRule("");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is(""));
		assertThat(rule.asScreenValue().displayText(), is("<<>>"));
		assertThat(rule.asScreenValue().hint(), is("Empty string is invalid"));
	}

	@Test
	public void blank_space_makes_an_invalid_rule()
	{
		MatchingRule rule = new EditableMatchingRule("   ");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is("   "));
		assertThat(rule.asScreenValue().displayText(), is("<<   >>"));
		assertThat(rule.asScreenValue().hint(), is("Empty string is invalid"));
	}
	
	@Test
	public void invalid_regex_makes_an_invalid_rule()
	{
		MatchingRule rule = new EditableMatchingRule("(missing bracket");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is("(missing bracket"));
		assertThat(rule.asScreenValue().displayText(), is("<<(missing bracket>>"));
		assertThat(rule.asScreenValue().hint(), is("Invalid regex: Unclosed group"));
	}
	
	@Test(expected=ProgrammerError.class)
	public void null_is_rejected()
	{
		new EditableMatchingRule(null); // will throw
	}
	
	@Test
	public void copying_valid_rule_definition()
	{
		MatchingRule rule = new EditableMatchingRule("rule");
		
		ScreenValue copy = rule.asScreenValue().copy();
		
		assertThat(copy.isValid(), is(true));
		assertThat(copy.text(), is(rule.text()));
		assertThat(copy.displayText(), is(rule.asScreenValue().displayText()));
		assertThat(copy.hint(), is(rule.asScreenValue().hint()));
	}
	
	@Test
	public void copying_invalid_rule_definition()
	{
		MatchingRule rule = new EditableMatchingRule("");
		
		ScreenValue copy = rule.asScreenValue().copy();
		
		assertThat(copy.isValid(), is(false));
		assertThat(copy.text(), is(rule.text()));
		assertThat(copy.displayText(), is(rule.asScreenValue().displayText()));
		assertThat(copy.hint(), is(rule.asScreenValue().hint()));
	}
	
	@Test
	public void can_determine_regex_match()
	{
		MatchingRule rule = new EditableMatchingRule("ab(c+)d.*");
		
		assertThat(rule.matches("abccccdx"), is(true));
		assertThat(rule.matches("abXd"), is(false));
	}
	
	@Test
	public void whitespace_is_ignore_when_matching_rules()
	{
		MatchingRule rule = new EditableMatchingRule("abcd");
		
		assertThat(rule.matches("  abcd   "), is(true));
	}
	
	@Test
	public void will_return_some_arbitrary_string_to_indicate_the_rule_is_a_match() //Hmmm... :-/
	{
		MatchingRule rule = new EditableMatchingRule("the rule");
		
		assertThat(rule.textForPossitiveMatch(), not(nullValue())); 
	}



}
