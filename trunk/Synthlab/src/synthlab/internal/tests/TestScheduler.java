package synthlab.internal.tests;

import junit.framework.TestCase;
import synthlab.api.ModulePool;
import synthlab.api.ModulePoolFactory;
import synthlab.api.Scheduler;
import synthlab.api.SchedulerFactory;
import synthlab.internal.modules.ModuleOut;
import synthlab.internal.modules.ModuleVCO;

/**
 * class TestScheduler for unit test of BasicScheduler
 * */
public class TestScheduler extends TestCase
{
  /**
   * constructor
   * */
  public TestScheduler()
  {
    super();
  }

  /**
   * tested functions: getPool, getTasks, son--> sound card
   * */
  public void testBasicScheduler()
  {
    final ModulePool pool = ModulePoolFactory.createDefault();
    final Scheduler scheduler = SchedulerFactory.createDefault();

    scheduler.setPool(pool);
    assertEquals(pool, scheduler.getPool());

    scheduler.reorder();
    assertEquals(pool.getModules(), scheduler.getTasks());

    final ModuleVCO vco = new ModuleVCO();
    final ModuleOut out = new ModuleOut();

    pool.register(vco);
    pool.register(out);

    pool.link(vco.getOutput("oSignal"), out.getInput("iSignal"));
    vco.getInput("iShape").setValues(ModuleVCO.SHAPE_SINE);

    scheduler.play(0);
    final long start = System.currentTimeMillis();
    while (System.currentTimeMillis() - start < (2000))
    {
    }
    scheduler.stop();
  }

}
