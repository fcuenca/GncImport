package gncimport.tests.unit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import gncimport.transfer.RuleDefinition;
import gncimport.transfer.UserEnteredRuleDefinition;
import gncimport.utils.ProgrammerError;

import org.junit.Test;

public class RuleDefinitionTests
{
	@Test
	public void valid_regex_makes_a_valid_rule()
	{
		RuleDefinition rule = new UserEnteredRuleDefinition("rule text.*");
		
		assertThat(rule.isValid(), is(true));
		assertThat(rule.text(), is("rule text.*"));
		assertThat(rule.hint(), is(""));
	}
	
	@Test
	public void empty_string_makes_an_invalid_rule()
	{
		RuleDefinition rule = new UserEnteredRuleDefinition("");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is("<<>>"));
		assertThat(rule.hint(), is("Empty string is invalid"));
	}

	@Test
	public void blank_space_makes_an_invalid_rule()
	{
		RuleDefinition rule = new UserEnteredRuleDefinition("   ");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is("<<   >>"));
		assertThat(rule.hint(), is("Empty string is invalid"));
	}
	
	@Test
	public void invalid_regex_makes_an_invalid_rule()
	{
		RuleDefinition rule = new UserEnteredRuleDefinition("(missing bracket");
		
		assertThat(rule.isValid(), is(false));
		assertThat(rule.text(), is("<<(missing bracket>>"));
		assertThat(rule.hint(), is("Invalid regex: Unclosed group"));
	}
	
	@Test(expected=ProgrammerError.class)
	public void null_is_rejected()
	{
		new UserEnteredRuleDefinition(null); // will throw
	}
	
	@Test
	public void copying_valid_rule_definition()
	{
		RuleDefinition rule = new UserEnteredRuleDefinition("rule");
		
		RuleDefinition copy = rule.copy();
		
		assertThat(copy.isValid(), is(true));
		assertThat(copy.text(), is(rule.text()));
		assertThat(copy.hint(), is(rule.hint()));
	}
	
	@Test
	public void copying_invalid_rule_definition()
	{
		RuleDefinition rule = new UserEnteredRuleDefinition("");
		
		RuleDefinition copy = rule.copy();
		
		assertThat(copy.isValid(), is(false));
		assertThat(copy.text(), is(rule.text()));
		assertThat(copy.hint(), is(rule.hint()));
	}
	
}
