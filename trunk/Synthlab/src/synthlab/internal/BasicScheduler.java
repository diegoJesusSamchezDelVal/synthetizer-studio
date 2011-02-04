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
    tasks_ = null;
    pool_ = null;
    links_ = null;
  }

  @Override
  public void play(int count)
  {
    // Sanity check
    if (pool_ == null || tasks_ == null || links_ == null)
      return;

    //System.out.println();
    //System.out.println(">>> Wave #0");
    //printStatus();
    for (int i = count; i > 0; --i)
    {
      long start = System.currentTimeMillis();
      // Execute all tasks
      for (Module module : tasks_)
      {
        module.compute();
      }
      // Propagate all outputs to inputs
      for (Map.Entry<Port, Port> link : links_.entrySet())
      {
        link.getValue().setValue(link.getKey().getValue());
      }
      //System.out.println();
      //System.out.println(">>> Wave #"+((count-i)+1));
      //printStatus();
    }
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
    tasks_ = pool_.getModules();
    links_ = pool_.getLinks();
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
  public List<Module> getTasks()
  {
    return tasks_;
  }
  
  private void printStatus()
  {
    for (Module module : tasks_)
    {
      System.out.println(" - Module "+module.getName());
      for ( Port port : module.getInputs() )
        System.out.println("   -> "+port.getName()+" = "+port.getValue());
      for ( Port port : module.getOutputs() )
        System.out.println("      "+port.getName()+" = "+port.getValue()+" ->");
    }
  }

  private ModulePool        pool_;
  private List<Module>      tasks_;
  private BiMap<Port, Port> links_;
}
