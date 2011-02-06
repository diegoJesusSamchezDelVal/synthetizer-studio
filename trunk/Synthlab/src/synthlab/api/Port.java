package synthlab.api;

public interface Port
{
  // --- Name
  public String getName();
  
  // --- Value
  public void setValue(double value);
  
  public double getValue();
  
  // --- Input/Output
  public boolean isInput();
  
  public boolean isOutput();
  
  // --- Status
  public boolean isLinked();
  
  public void setLinked( boolean linked );
  
  // --- Module
  public Module getModule();
  
  public void setModule(Module module);
}
