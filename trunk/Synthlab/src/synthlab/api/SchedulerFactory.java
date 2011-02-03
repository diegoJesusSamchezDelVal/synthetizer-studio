package synthlab.api;

import synthlab.internal.BasicScheduler;

public class SchedulerFactory
{
  static public Scheduler createDefault()
  {
    return new BasicScheduler();
  }
}
