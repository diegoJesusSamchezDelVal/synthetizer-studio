package synthlab.api;

import java.util.List;

public interface ModulePool
{
  public void register(Module module);

  public void unregister(Module module);

  public boolean contains(Module module);

  public void link(Port p1, Port p2);

  public void unlink(Port p1, Port p2);
  
  public boolean linked(Port p1, Port p2);

  public List<Module> getModules();
}
