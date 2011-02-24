package synthlab.internal.tests;

import junit.framework.TestCase;
import synthlab.internal.BasicScriptModule;

/**
 * class TestScripting for unit test of BasicScriptModule
 * */
public class TestScripting extends TestCase
{
  /**
   * constructor
   * */
  public TestScripting()
  {
    super();
  }
  
/**
 * tested functions:getName, getScript
 * */
  public void testBasicScriptModule()
  {
    final String scripting = "12345";
    final String name = "Customer";

    final BasicScriptModule script = new BasicScriptModule(name);

    assertEquals("Customer", script.getName());
    assertNull(script.getScript());
    script.setScript(scripting);
    assertEquals(script.getScript(), "12345");
  }
}
