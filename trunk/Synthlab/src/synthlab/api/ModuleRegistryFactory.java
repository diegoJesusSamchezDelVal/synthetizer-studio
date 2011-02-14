package synthlab.api;

import synthlab.internal.BasicModuleRegistry;

public class ModuleRegistryFactory
{
  /**
   * This will return a new instance of the default provided module registry.
   * @return
   */
  public static ModuleRegistry createDefault()
  {
    return new BasicModuleRegistry();
  }
}
