package synthlab.internal;

import java.nio.ByteBuffer;
import java.util.HashMap;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;

public class BasicPort implements Port
{
  public BasicPort(String name, double value, ValueType type, ValueUnit unit, ValueRange range )
  {
    module_ = null;
    name_ = name;
    linked_ = false;
    type_ = type;
    unit_ = unit;
    range_ = range;
    values_ = ByteBuffer.allocate(Scheduler.SamplingBufferSize*(Double.SIZE/8));
    setValues(value);
  }

  @Override
  public String getName()
  {
    return name_;
  }

  @Override
  public void setValues(ByteBuffer values)
  {
    values.clear();
    values_.clear();
    values_.put(values);
    values_.clear();
    values.clear();
  }
  
  public void setValues(double value)
  {
    values_.clear();
    for ( int i=0; i<Scheduler.SamplingBufferSize; ++i)
      values_.putDouble(value);
    values_.clear();
  }

  @Override
  public ByteBuffer getValues()
  {
    return values_;
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

  @Override
  public ValueType getValueType()
  {
    return type_;
  }

  @Override
  public ValueUnit getValueUnit()
  {
    return unit_;
  }

  @Override
  public ValueRange getValueRange()
  {
    return range_;
  }
  
  private boolean linked_;
  private String name_;
  private ByteBuffer values_;
  private Module module_;
  private ValueType type_;
  private ValueUnit unit_;
  private ValueRange range_;
  
  
}
