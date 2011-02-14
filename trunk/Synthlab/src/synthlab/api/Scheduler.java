package synthlab.api;

import java.util.List;

/**
 * This class is at the core of the synthesizer. It is responsible of asking to the modules
 * of a module pool to do their computation, and to propagate the results from output ports
 * to input ports.
 * Once created, you can attach a scheduler to a module pool using setPool().
 */
public interface Scheduler
{
  //================================================ Scheduling
  /**
   * This will start the scheduler.
   * The playing will be done in an other thread so this call is not blocking.
   * You can provide a maximum number of propagations before the play stops, or
   * just provide 0 for an infinite play().
   * If the scheduler was not set to a valid module pool, the results are undefined.
   */
  public void play(int count);

  /**
   * This will effectively stop the scheduler. The playing thread will stop.
   */
  public void stop();

  //TODO delete this
  public void reorder();

  //TODO delete this
  public List<Module> getTasks();

  //================================================ ModulePool
  /**
   * This will bind this scheduler to the provided module pool.
   */
  public void setPool(ModulePool pool);

  /**
   * This will retrieve the pool this scheduler is currently bound to, or 
   * null if no module pool was previsouly bound.
   * @return
   */
  public ModulePool getPool();
  
  //================================================ Configuration
  /**
   * This is the amount of samples that this scheduler will ask the port to use.
   * The default sampling rate is 44100 (CD quality), so using a sampling buffer size
   * of 441 means that we will work with small buffers of 10ms.
   * If you computer cannot handle the amount of propagation that it generates, try increasing
   * this value under the following conditions:
   * - be aware that the more this value is increased, the slower the reaction time will be when
   * changing port values (responsiveness)
   * - always keep this value an integer multiple of the sampling rate (44100) or results are undefined.
   */
  public final int SamplingBufferSize = 441;
}
