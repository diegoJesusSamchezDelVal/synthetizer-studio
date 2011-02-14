package synthlab.api;

import java.util.List;

import synthlab.internal.BasicScriptModule;


public class ModuleFactory
{
  public static Module createFromScript(String name, List<Port> inputs, List<Port> outputs, String script)
  {
    if ( name.isEmpty() || script.isEmpty() )
      return null;
    
    BasicScriptModule module = new BasicScriptModule(name);
    
    for ( Port p : inputs )
      module.addInput(p);
    
    for ( Port p : outputs )
      module.addOutput(p);
    
    module.setScript(script);
    
    return module;
  }
  
  public static Module createFromXML(String filename)
  {
    //TODO
    return null;
  }
  
  public static Module createFromAnnotated(Object object)
  {
    //TODO
    return null;
  }
  
  public static Module createFromPrototype( Module m )
  {
    try
    {
      return m.clone();
    }
    catch (CloneNotSupportedException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
