package synthlab.api;

public interface Port
{
  // --- Name
  public void setName(String name);

  public String getName();
  
  // --- Value
  public void setValue(Integer value);
  
  Integer getValue();
  
  // --- Link
  public void setLink( Port port );
  
  public Port getLink();
  
  // --- Manipulation
  public void refresh();
}
