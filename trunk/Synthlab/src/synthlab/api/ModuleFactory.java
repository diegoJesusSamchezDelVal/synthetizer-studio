package synthlab.api;


public class ModuleFactory
{
  public static Module createFromDescription(String description)
  {
    //TODO
    return null;
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
      return m.getClass().newInstance();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
