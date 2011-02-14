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
    // We need rocket speed priority ;)
    playThread_.setPriority(Thread.MAX_PRIORITY);
    running_ = false;
  }

  @Override
  public void play(int count)
  {
    // Sanity chec: check that we are not already playing
    if (pool_ == null || tasks_ == null || links_ == null || running_)
      return;

    // Create a new thread to start scheduling the modules.
    // This is required since the play() method should not be blocking!
    playThread_ = new PlayThread();
    playThread_.setPriority(Thread.MAX_PRIORITY);
    running_ = true;
    playThread_.start();
  }

  @Override
  public void stop()
  {
    // Check if we are not already stopped
    if ( !running_ || playThread_==null )
      return;
    
    // This will cleanly make the playthread loop to stop
    running_ = false;
    
    try
    {
      // Wait patiently for our play thread to end cleanly
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
    //TODO: This is no longer useful and should be removed
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
      // Open and start an audio data line
      Audio.openLine();
      Audio.startLine();

      // This will be our starting point for scheduling 
      long start = System.currentTimeMillis();

      // TODO should add mutexes here
      while (running_)
      {
        // Scheduler timing: spin lock for 10ms
        while ((System.currentTimeMillis() - start) < (1000. / (44100. / Scheduler.SamplingBufferSize) - 0))
          ;
        start = System.currentTimeMillis();

        // Execute all tasks
        synchronized (pool_)
        {
          // Ask all modules to do their computation
          for (Module module : tasks_)
            module.compute();

          // Propagate all outputs to inputs
          for (Map.Entry<Port, Port> link : links_.entrySet())
            link.getValue().setValues(link.getKey().getValues());
        }
      }

      // Stop and close the audio line
      Audio.stopLine();
      Audio.closeLine();
    }
  }

  /**
   * This is the module pool we were bound to
   */
  private ModulePool        pool_;
  
  /**
   * This is the list of tasks we have to do (i.e. the list of modules to ask for computation)
   */
  private List<Module>      tasks_;
  
  /**
   * This is the list of the links we have to propagate
   */
  private BiMap<Port, Port> links_;
  
  /**
   * Are we currently running ?
   */
  private boolean           running_;
  
  /**
   * The play thread
   */
  private PlayThread        playThread_;
}
