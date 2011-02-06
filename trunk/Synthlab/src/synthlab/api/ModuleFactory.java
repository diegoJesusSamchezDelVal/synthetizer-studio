package synthlab.api;


public class ModuleFactory
{
  public Module createFromDescription(String description)
  {
    //TODO
    return null;
  }
  
  public Module createFromXML(String filename)
  {
    //TODO
    return null;
  }
  
  public Module createFromAnnotated(Object object)
  {
    //TODO
    return null;
  }
  
  public Module createFromPrototype( Module m )
  {
    try
    {
      return m.getClass().newInstance();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
