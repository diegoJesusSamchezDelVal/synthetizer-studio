package synthlab.api;

public interface Port
{
  // --- Name
  public String getName();
  
  // --- Value
  public void setValue(Integer value);
  
  public Integer getValue();
  
  // --- Module
  public Module getModule();
  
  public void setModule(Module module);
}
