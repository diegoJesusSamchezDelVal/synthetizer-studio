package synthlab.api;

import java.util.List;

import com.google.common.collect.BiMap;

public interface ModulePool
{
  // --- Modules
  public void register(Module module);

  public void unregister(Module module);

  public boolean contains(Module module);
  
  public List<Module> getModules();
  
  // --- Links
  public void link(Port p1, Port p2);

  public void unlink(Port p1, Port p2);
  
  public void unlinkAll(Module module);
  
  public boolean linked(Port p1, Port p2);
  
  public BiMap<Port,Port> getLinks();
}
