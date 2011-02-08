package synthlab.internal.tests;

import org.junit.Test;

import junit.framework.TestCase;

import synthlab.api.*;
import synthlab.internal.*;
import synthlab.internal.modules.*;


public class ModuleTest extends TestCase
{
  protected void setUp()
  {
  }
/*
  @Test
  public void testCompute()
  {
    ModuleVCO vco_ = new ModuleVCO();

    assertTrue(vco_.getInput("iFrequency").getValue() == 0);
    assertTrue(vco_.getInput("iConstant").getValue() == 1);
    assertTrue(vco_.getOutput("oSignal").getValue() == 0);
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

    assertFalse(pool.linked(m1.getOutput("oSignal"), m2.getInput("iFrequency")));

    pool.link(m1.getOutput("oSignal"), m2.getInput("iFrequency"));

    assertTrue(pool.linked(m1.getOutput("oSignal"), m2.getInput("iFrequency")));

    pool.unlink(m1.getOutput("oSignal"), m2.getInput("iFrequency"));

    assertFalse(pool.linked(m1.getOutput("oSignal"), m2.getInput("iFrequency")));
  }
*/
  /*
  static public Module createModule(String name, int nbInputs, int nbOutputs)
  {
    BasicModule module = new BasicModule(name)
    {
      @Override
      public void compute()
      {
        // Accumulate all inputs and propagate to output
        for (Port p : getOutputs())
        {
          synchronized (p)
          {
            p.setValues(1);
          }
        }
      }
    };

    for (int i = 0; i < nbInputs; ++i)
    {
      Port port = new BasicPort("i" + (i + 1), 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1));
      module.addInput(port);
    }

    for (int i = 0; i < nbOutputs; ++i)
    {
      Port port = new BasicPort("o" + (i + 1), 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1));
      module.addOutput(port);
    }

    return module;
  }

  @Test
  public void testScheduler()
  {
    ModulePool pool = ModulePoolFactory.createDefault();

    Module m1 = createModule("m1", 0, 1);
    Module m2 = createModule("m2", 1, 0);

    pool.register(m1);
    pool.register(m2);

    pool.link(m1.getOutput("o1"), m2.getInput("i1"));

    Scheduler s = SchedulerFactory.createDefault();

    s.setPool(pool);

    s.play(5);
  }

  @Test
  public void testSchedulerOrdering()
  {
    // m1
    // |
    // V
    // m2
    //
    // m3 m5
    // |
    // V
    // m4 m6
    // | |
    // +---++---+
    // ||
    // VV
    // m7
    // ||
    // +---++---+
    // | |
    // |+------+|
    // V| VV
    // m8 -----> m9
    // |
    // V
    // m10 <----+
    // | | |
    // | +-----+
    // V
    // m11
    // |
    // V
    // m12 -----> m13
    // | |
    // V V
    // m15 -----> m14

    Module m1 = createModule("m1", 0, 1);
    Module m2 = createModule("m2", 1, 1);
    Module m3 = createModule("m3", 1, 1);
    Module m4 = createModule("m4", 0, 1);
    Module m5 = createModule("m5", 0, 1);
    Module m6 = createModule("m6", 1, 1);
    Module m7 = createModule("m7", 2, 2);
    Module m8 = createModule("m8", 2, 2);
    Module m9 = createModule("m9", 2, 2);
    Module m10 = createModule("m10", 3, 2);
    Module m11 = createModule("m11", 1, 2);
    Module m12 = createModule("m12", 2, 2);
    Module m13 = createModule("m13", 1, 1);
    Module m14 = createModule("m14", 1, 1);
    Module m15 = createModule("m15", 2, 0);

    ModulePool pool = ModulePoolFactory.createDefault();

    pool.register(m15);
    pool.register(m14);
    pool.register(m13);
    pool.register(m12);
    pool.register(m11);
    pool.register(m10);
    pool.register(m9);
    pool.register(m8);
    pool.register(m7);
    pool.register(m6);
    pool.register(m5);
    pool.register(m4);
    pool.register(m3);
    pool.register(m2);
    pool.register(m1);

    pool.link(m1.getOutput("o1"), m2.getInput("i1"));
    pool.link(m4.getOutput("o1"), m7.getInput("i1"));
    pool.link(m5.getOutput("o1"), m6.getInput("i1"));
    pool.link(m6.getOutput("o1"), m7.getInput("i2"));
    pool.link(m7.getOutput("o1"), m8.getInput("i1"));
    pool.link(m7.getOutput("o2"), m9.getInput("i1"));
    pool.link(m8.getOutput("o1"), m9.getInput("i2"));
    pool.link(m8.getOutput("o2"), m10.getInput("i1"));
    pool.link(m9.getOutput("o1"), m8.getInput("i2"));
    pool.link(m9.getOutput("o2"), m10.getInput("i2"));
    pool.link(m10.getOutput("o1"), m10.getInput("i3"));
    pool.link(m10.getOutput("o2"), m11.getInput("i1"));
    pool.link(m11.getOutput("o1"), m12.getInput("i1"));
    pool.link(m11.getOutput("o2"), m12.getInput("i2"));
    pool.link(m12.getOutput("o1"), m13.getInput("i1"));
    pool.link(m12.getOutput("o2"), m15.getInput("i1"));
    pool.link(m13.getOutput("o1"), m14.getInput("i1"));
    pool.link(m14.getOutput("o1"), m15.getInput("i2"));

    Scheduler s = SchedulerFactory.createDefault();

    s.setPool(pool);

    long start = System.currentTimeMillis();
    long i = 0;
    while (System.currentTimeMillis() - start < 1000)
    {
      ++i;
      s.play(1);
    }

    // Be sure we are able to compute at least 44100 samples per second
    System.out.println("We were able to compute " + (i*Scheduler.SamplingBufferSize) + " samples/second");
    // assertTrue( i>44100 );
  }
 */
  public void wait( int seconds )
  {
    long start = System.currentTimeMillis();
    while ( System.currentTimeMillis()-start < (seconds*1000) );
  }
 
  @Test
  public void testOut()
  {
    ModuleVCO vco = new ModuleVCO();
    ModuleLFO lfo = new ModuleLFO();
    ModuleOut out = new ModuleOut();
    
    ModulePool pool = ModulePoolFactory.createDefault();
    
    Scheduler s = SchedulerFactory.createDefault();
    
    pool.register(vco);
    pool.register(lfo);
    pool.register(out);
    
    pool.link(vco.getOutput("oSignal"), out.getInput("iSignal"));
    //pool.link(lfo.getOutput("oSignal"), vco.getInput("iFrequency"));
    
    s.setPool(pool);

    vco.getInput("iShape").setValues(ModuleVCO.SHAPE_SINE);
    
    s.play(0);
    wait(2); // Wait for 5 seconds: playing
    
    s.stop();
    wait(1); // Wait for 1 second: stopped
    
    vco.getInput("iShape").setValues(ModuleVCO.SHAPE_PULSE);
    s.play(0);
    wait(2); // Wait for  seconds: playing
    
    s.stop();
    wait(1); // Wait for 1 second: stopped
    
    vco.getInput("iShape").setValues(ModuleVCO.SHAPE_SAWTOOTH);
    
    s.play(0);
    wait(2); // Wait for 5 seconds: playing
    
    s.stop();
    wait(1); // Wait for 1 second: stopped
    
    vco.getInput("iShape").setValues(ModuleVCO.SHAPE_TRIANGLE);
    
    s.play(0);
    wait(2); // Wait for 5 seconds: playing
    
    s.stop();
  }
}
