package synthlab.internal;

import java.util.*;
import synthlab.api.Module;
import synthlab.api.ModuleRegistry;

public class BasicModuleRegistry implements ModuleRegistry
{
  public BasicModuleRegistry()
  {
    prototypes_ = new ArrayList<Module>();
  }
  
  @Override
  public void register(Module module)
  {
    if (registered(module))
      return;

    prototypes_.add(module);
  }

  @Override
  public void unregister(Module module)
  {
    if (!registered(module))
      return;

    prototypes_.remove(module);
  }

  @Override
  public boolean registered(Module module)
  {
    for ( Module m : prototypes_ )
      if ( m.getName()==module.getName() )
        return true;
    return false;
  }
  
  @Override
  public List<Module> getModules()
  {
    return prototypes_;
  }

  List<Module> prototypes_;
}
