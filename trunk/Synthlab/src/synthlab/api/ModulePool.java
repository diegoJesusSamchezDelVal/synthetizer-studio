package synthlab.api;

import java.util.Map;


public class ModulePool
{
  public void register(Module module)
  {
    if (exists(module.getName()))
      return;

    modules_.put(module.getName(), module);
  }

  public void unregister(Module module)
  {
    unregister(module.getName());
  }

  public void unregister(String name)
  {
    if (!exists(name))
      return;

    modules_.remove(name);
  }

  public boolean exists(String name)
  {
    return modules_.containsKey(name);
  }

  public Module retrieve(String name)
  {
    if (!exists(name))
      return null;

    return modules_.get(name);
  }

  private Map<String, Module> modules_;
}
