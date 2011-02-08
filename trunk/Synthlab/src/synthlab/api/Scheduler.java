package synthlab.api;

import java.util.List;

public interface Scheduler
{
  // --- Scheduling
  public void play(int count);

  public void stop();

  public void reorder();

  public List<Module> getTasks();

  // --- ModulePool
  public void setPool(ModulePool pool);

  public ModulePool getPool();
  
  // --- Configuration
  public final int SamplingBufferSize = 441;
}
