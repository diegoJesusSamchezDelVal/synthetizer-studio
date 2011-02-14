package synthlab.internal;

import java.util.*;
import synthlab.api.Module;
import synthlab.api.ModuleRegistry;

/**
 * This is the default implementation of the module registry
 */
public class BasicModuleRegistry implements ModuleRegistry
{
  public BasicModuleRegistry()
  {
    // We store our prototype in an array list
    prototypes_ = new ArrayList<Module>();
  }
  
  @Override
  public void register(Module module)
  {
    // Check that the prototype is not already present
    if (registered(module))
      return;

    // Add it to the list 
    prototypes_.add(module);
  }

  @Override
  public void unregister(Module module)
  {
    // Check that this prototype is currently present
    if (!registered(module))
      return;

    // Remove it from the list 
    prototypes_.remove(module);
  }

  @Override
  public boolean registered(Module module)
  {
    // Check if we already have a prototype with the same name
    for ( Module m : prototypes_ )
      if ( m.getName()==module.getName() )
        return true;
    return false;
  }
  
  @Override
  public List<Module> getModules()
  {
    // Return the list of prototypes we have
    return prototypes_;
  }

  /**
   * Our list of prototypes
   */
  List<Module> prototypes_;
}
