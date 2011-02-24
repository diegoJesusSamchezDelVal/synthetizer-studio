package synthlab.internal.tests;

import junit.framework.TestCase;

import org.junit.Test;

import synthlab.api.ModulePool;
import synthlab.api.ModulePoolFactory;
import synthlab.internal.BasicModule;
import synthlab.internal.modules.ModuleVCO;
/**
 * class testModulePool for unit test of pool
 * */
public class TestModulePool extends TestCase
{
/**
 * constructor
 * */
  public TestModulePool()
  {
    super();
  }
/**
 * tested functions: register, unregister, link, unlink, unlinkAll, linked
 * */
  @Test
  public void testBasicModulePool()
  {
    final BasicModule module1 = new ModuleVCO();
    final BasicModule module2 = new ModuleVCO();
    final BasicModule module3 = new ModuleVCO();
    final ModulePool pool = ModulePoolFactory.createDefault();
    assertFalse(pool.contains(module1));

    pool.register(module1);
    assertTrue(pool.contains(module1));

    pool.unregister(module1);
    assertFalse(pool.contains(module1));

    pool.register(module1);
    assertNotNull(module1.getOutput("oSignal"));
    assertNotNull(module2.getInput("iFrequency"));

    pool.link(module1.getOutput("oSignal"), module2.getInput("iFrequency"));
    assertFalse(pool.linked(module1.getOutput("oSignal"),
        module2.getInput("iFrequency")));

    pool.register(module2);
    pool.register(module3);
    pool.link(module1.getOutput("oSignal"), module2.getInput("iFrequency"));
    assertTrue(pool.linked(module1.getOutput("oSignal"),
        module2.getInput("iFrequency")));

    pool.unlink(module1.getOutput("oSignal"), module2.getInput("iFrequency"));
    assertFalse(pool.linked(module1.getOutput("oSignal"),
        module2.getInput("iFrequency")));

    pool.link(module1.getOutput("oSignal"), module2.getInput("iFrequency"));
    pool.link(module1.getOutput("oSignal"), module3.getInput("iFrequency"));
    assertFalse(pool.linked(module1.getOutput("oSignal"),
        module3.getInput("iFrequency")));

    pool.link(module3.getOutput("oSignal"), module2.getInput("iFrequency"));
    assertFalse(pool.linked(module3.getOutput("oSignal"),
        module2.getInput("iFrequency")));

    pool.link(module2.getOutput("oSignal"), module3.getInput("iFrequency"));
    assertTrue(pool.linked(module2.getOutput("oSignal"),
        module3.getInput("iFrequency")));
    pool.unlinkAll(module1);
    assertFalse(pool.linked(module1.getOutput("oSignal"),
        module2.getInput("iFrequency")));
    assertTrue(pool.linked(module2.getOutput("oSignal"),
        module3.getInput("iFrequency")));

  }

}
