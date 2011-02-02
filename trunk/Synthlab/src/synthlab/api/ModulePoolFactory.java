package synthlab.api;

public class ModulePoolFactory
{
  public static ModulePool createDefault()
  {
    return new synthlab.internal.BasicModulePool();
  }
}
