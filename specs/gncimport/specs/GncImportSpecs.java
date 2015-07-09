package gncimport.specs;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Cucumber.Options(glue = {"gncimport.specs.steps.hypodermic"})
public class GncImportSpecs
{
}