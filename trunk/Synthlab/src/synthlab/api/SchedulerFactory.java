package synthlab.api;

import synthlab.internal.BasicScheduler;

public class SchedulerFactory
{
  /**
   * This method will return the provided default scheduler.
   * @return a default scheduler
   */
  static public Scheduler createDefault()
  {
    return new BasicScheduler();
  }
}
