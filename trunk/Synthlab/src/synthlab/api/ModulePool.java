package synthlab.api;

import java.util.List;

import com.google.common.collect.BiMap;

/**
 * A Module pool is responsible of storing modules and links. This is the root of the
 * synthesizer. You can register new modules and link ports together using link() and
 * register().
 */
public interface ModulePool
{
  //================================================ Modules
  /**
   * This will register a new module in the pool. Note that the module will not
   * be copied, but rather shared. So you can still edit it after registration.
   * Take care while changing you module to synchronize all accesses to its port
   * values!
   * If the module was already in the pool, nothing happens.
   */
  public void register(Module module);

  /**
   * This will effectively remove a module from the pool. If the module was not present,
   * nothing happens.
   * @param module
   */
  public void unregister(Module module);

  /**
   * This will return true if the module is present in the pool, false otherwise. Note
   * that this method will check the module itself, not the type of module (you can
   * safely have multiple "VCO" modules in a pool, but not the same VCO instance).
   * @param module the module to be searched
   * @return true if the module is present, false otherwise
   */
  public boolean contains(Module module);
  
  /**
   * This will retrieve the list of all modules currently contained in the pool.
   * @return the list of all modules currently contained in the pool.
   */
  public List<Module> getModules();
  
  //================================================ Links
  /**
   * This will link the specified port p1 with p2. You can provide p1 and p2 in any order
   * (input->output or output->input). If either of the two ports were already linked the
   * previous link will be destroyed. If p1 or p2 does not belong to a module contained in
   * this pool, the result is undefined. If either p1 or p2 are null, the result is also
   * undefined.
   */
  public void link(Port p1, Port p2);

  /**
   * This will destroy the link between p1 and p2. If either p1 or p2 does not belong to
   * a module contained by this pool the result is undefined. If either p1 or p2 are null the
   * result is undefined. If p1 and p2 were not previously linked together this method has no
   * effect.
   * @param p1
   * @param p2
   */
  public void unlink(Port p1, Port p2);
  
  /**
   * This method will remove all link to its input ports, and from its output ports. This is
   * usefull if you plan to remove a module from the pool.
   * @param module the module to be completely unlinked
   */
  public void unlinkAll(Module module);
  
  /**
   * This method will return true if the ports p1 and p2 are linked in this pool. If either p1
   * or p2 belong to modules outside of this pool, the results are undefined.
   * @param p1
   * @param p2
   * @return
   */
  public boolean linked(Port p1, Port p2);
  
  /**
   * This will return all the links currently active between the ports of the modules of this pool.
   * The order in which ports are mapped is not mandatory, but should be output->input in the default
   * implementation.
   * Do NOT change the links!
   * @return the mapping between ports (links)
   */
  public BiMap<Port,Port> getLinks();
}
