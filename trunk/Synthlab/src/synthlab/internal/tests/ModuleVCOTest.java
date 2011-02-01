package synthlab.internal.tests;

import org.junit.Test;

import junit.framework.TestCase;

import synthlab.internal.modules.ModuleVCO;

public class ModuleVCOTest extends TestCase
{
  protected void setUp()
  {
    vco_ = new ModuleVCO();
  }

  @Test
  public void testCompute()
  {
    assertTrue(vco_.getInput("iFrequency").getValue() == 0);
    assertTrue(vco_.getInput("iConstant").getValue() == 1);
    assertTrue(vco_.getOutput("oFrequency").getValue() == 0);

    vco_.compute();

    assertTrue(vco_.getOutput("oFrequency").getValue() == 2);
  }

  
  private synthlab.internal.modules.ModuleVCO vco_;
}
