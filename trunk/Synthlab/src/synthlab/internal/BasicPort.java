package synthlab.internal;

import synthlab.api.Module;
import synthlab.api.Port;

public class BasicPort implements Port
{
  public BasicPort(String name, Integer value)
  {
    module_ = null;
    name_ = name;
    linked_ = false;
    setValue(value);
  }

  @Override
  public String getName()
  {
    return name_;
  }

  @Override
  public void setValue(double value)
  {
    value_ = value;
  }

  @Override
  public double getValue()
  {
    return value_;
  }

  @Override
  public Module getModule()
  {
    return module_;
  }

  @Override
  public void setModule(Module module)
  {
    module_ = module;
  }
  
  @Override
  public boolean isInput()
  {
    // Sanity check
    if ( getModule()==null || getModule().getInputs()==null )
      return false;
    
    return getModule().getInputs().contains(this);
  }
  
  @Override
  public boolean isOutput()
  {
    // Sanity check
    if ( getModule()==null || getModule().getOutputs()==null )
      return false;
    
    return getModule().getOutputs().contains(this);
  }
  
  @Override
  public boolean isLinked()
  {
    return linked_;
  }
  
  @Override
  public void setLinked( boolean linked )
  {
    linked_ = linked;
  }

  private boolean linked_;
  private String name_;
  private double value_;
  private Module module_;
}
