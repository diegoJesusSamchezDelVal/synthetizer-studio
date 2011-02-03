package synthlab.internal;

import java.util.List;
import com.google.common.collect.BiMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import synthlab.api.Module;
import synthlab.api.ModulePool;
import synthlab.api.Port;
import synthlab.api.Scheduler;

public class BasicScheduler implements Scheduler
{
  public BasicScheduler()
  {
    layers_ = new ArrayList<List<Module>>();
  }

  @Override
  public void play(int count)
  {
    // TODO
  }

  @Override
  public void stop()
  {
    // TODO
  }

  @Override
  public void reorder()
  {
    // Following is a topological sort. When we encounter a cycle, we group all
    // nodes in the same layer and resume the sort.
    List<Module> modules = pool_.getModules();
  }
  
  @Override
  public ModulePool getPool()
  {
    return pool_;
  }

  @Override
  public void setPool(ModulePool pool)
  {
    stop();
    pool_ = pool;
    reorder();
  }

  @Override
  public List<List<Module>> getLayers()
  {
    return layers_;
  }

  private ModulePool         pool_;
  private List<List<Module>> layers_;
}
