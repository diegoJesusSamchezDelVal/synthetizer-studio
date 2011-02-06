package synthlab.api;

import java.util.List;

public interface ModuleRegistry
{
  public void register( Module moduleClass );
  
  public void unregister( Module moduleClass );
  
  public boolean registered( Module moduleClass );
  
  public List<Module> getModules();
}
