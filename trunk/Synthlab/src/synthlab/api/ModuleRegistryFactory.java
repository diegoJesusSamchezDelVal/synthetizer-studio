package synthlab.api;

import synthlab.internal.BasicModuleRegistry;

public class ModuleRegistryFactory
{
  public static ModuleRegistry createDefault()
  {
    return new BasicModuleRegistry();
  }
}
