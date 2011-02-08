package synthlab.internal;

import com.google.common.collect.BiMap;
import java.util.*;
import synthlab.api.*;

public class BasicScheduler implements Scheduler
{
  public BasicScheduler()
  {
    tasks_ = null;
    pool_ = null;
    links_ = null;
    playThread_ = new PlayThread();
  }

  @Override
  public void play(int count)
  {
    // Sanity check
    if (pool_ == null || tasks_ == null || links_ == null)
      return;

    playThread_ = new PlayThread();
    running_ = true;
    playThread_.start();
  }

  @Override
  public void stop()
  {
    running_ = false;
    try
    {
      playThread_.join();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
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

  private class PlayThread extends Thread
  {
    public void run()
    {
      Audio.openLine();
      Audio.startLine();

      long start;

      // TODO should add mutexes here
      while (running_)
      {
        start = System.currentTimeMillis();

        // Execute all tasks
        synchronized (pool_)
        {
          for (Module module : tasks_)
            module.compute();

          // Propagate all outputs to inputs
          for (Map.Entry<Port, Port> link : links_.entrySet())
            link.getValue().setValues(link.getKey().getValues());
        }

        // Synchronize scheduler: always keep 2ms ahead
        while ((System.currentTimeMillis() - start) < (1000. / (44100. / Scheduler.SamplingBufferSize) - 0))
          ;
      }

      Audio.stopLine();
      Audio.closeLine();
    }
  }

  private ModulePool        pool_;
  private List<Module>      tasks_;
  private BiMap<Port, Port> links_;
  private boolean           running_;
  private PlayThread        playThread_;
}
