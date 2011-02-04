package synthlab.internal;

import synthlab.api.Module;
import synthlab.api.Port;

public class BasicPort implements Port
{
  public BasicPort(String name, Integer value)
  {
    module_ = null;
    name_ = name;
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

  private String name_;
  private double value_;
  private Module module_;
}
