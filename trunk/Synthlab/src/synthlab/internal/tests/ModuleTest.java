package synthlab.internal.tests;

import org.junit.Test;

import junit.framework.TestCase;

import java.util.List;

import synthlab.api.*;
import synthlab.internal.*;
import synthlab.internal.modules.*;

public class ModuleTest extends TestCase
{
  protected void setUp()
  {
  }

  @Test
  public void testCompute()
  {
    ModuleVCO vco_ = new ModuleVCO();

    assertTrue(vco_.getInput("iFrequency").getValue() == 0);
    assertTrue(vco_.getInput("iConstant").getValue() == 1);
    assertTrue(vco_.getOutput("oFrequency").getValue() == 0);

    vco_.compute();

    assertTrue(vco_.getOutput("oFrequency").getValue() == 2);
  }

  @Test
  public void testModulePool()
  {
    ModulePool pool1 = ModulePoolFactory.createDefault();
    ModulePool pool2 = ModulePoolFactory.createDefault();

    ModuleVCO m1 = new ModuleVCO();
    ModuleVCO m2 = new ModuleVCO();
    ModuleVCO m3 = new ModuleVCO();

    pool1.register(m1);
    pool1.register(m2);
    pool2.register(m3);

    assertTrue(pool1.getModules().size() == 2);
    assertTrue(pool2.getModules().size() == 1);

    System.out.println("Pool 1 modules:");
    for (Module m : pool1.getModules())
    {
      System.out.println(" - Module: " + m.getName());
    }

    System.out.println("Pool 2 modules:");
    for (Module m : pool2.getModules())
    {
      System.out.println(" - Module: " + m.getName());
    }

    assertTrue(pool1.contains(m1));
    assertTrue(pool1.contains(m2));
    assertTrue(pool2.contains(m3));
    assertFalse(pool2.contains(m2));

    pool1.register(m1);

    assertTrue(pool1.getModules().size() == 2);

    pool1.unregister(m2);

    assertTrue(pool1.getModules().size() == 1);

    pool1.unregister(m2);

    assertTrue(pool1.getModules().size() == 1);
    assertTrue(pool1.contains(m1) == true);
    assertTrue(pool1.contains(m2) == false);

    pool1.unregister(m1);

    assertTrue(pool1.getModules().size() == 0);
    assertTrue(pool1.contains(m1) == false);
    assertTrue(pool1.contains(m2) == false);
  }
  
  @Test
  public void testLinks()
  {
    ModulePool pool = ModulePoolFactory.createDefault();
    
    ModuleVCO m1 = new ModuleVCO();
    ModuleVCO m2 = new ModuleVCO();
    
    pool.register(m1);
    pool.register(m2);
    
    assertFalse(pool.linked(m1.getOutput("oFrequency"), m2.getInput("iFrequency")));
    
    pool.link(m1.getOutput("oFrequency"), m2.getInput("iFrequency"));
    
    assertTrue(pool.linked(m1.getOutput("oFrequency"), m2.getInput("iFrequency")));
    
    pool.unlink(m1.getOutput("oFrequency"), m2.getInput("iFrequency"));
    
    assertFalse(pool.linked(m1.getOutput("oFrequency"), m2.getInput("iFrequency")));
  }
}
