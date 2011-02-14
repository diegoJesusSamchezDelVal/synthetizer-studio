package synthlab.api;

import java.util.List;

/**
 * A registry is a module prototype container. It is useful to store the modules that
 * are currently available to the user for inserting into a module pool.
 */
public interface ModuleRegistry
{
  /**
   * This will register a new prototype in the module registry. If the prototype was
   * already in the registry, the registration will be ignored.
   * @param moduleClass the module prototype
   */
  public void register( Module moduleClass );
  
  /**
   * This will unregister the specified module from the registry. If the module was
   * not already present in the registry the call is ignored.
   * @param moduleClass
   */
  public void unregister( Module moduleClass );
  
  /**
   * This will return true if the provide prototype is already present in the registry,
   * false otherwise.
   * @param moduleClass the prototype to be checked
   * @return true if the prototype is present, false otherwise
   */
  public boolean registered( Module moduleClass );
  
  /**
   * This will return the list of all prototype that are currently registered in this
   * module registry.
   * @return the list of all registered modules.
   */
  public List<Module> getModules();
}
