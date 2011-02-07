package synthlab.api;

import java.nio.ByteBuffer;

public interface Port
{
  // --- Name
  public String getName();
  
  // --- Value
  public void setValues(ByteBuffer value);
  public void setValues(double value);
  
  public ByteBuffer getValues();
  
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
