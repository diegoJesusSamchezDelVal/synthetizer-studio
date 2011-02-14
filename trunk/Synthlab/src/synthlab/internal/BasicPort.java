package synthlab.internal;

import java.nio.ByteBuffer;
import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;

/**
 * This is the default implementation of a port
 */
public class BasicPort extends Port
{
  public BasicPort(String name, double value, ValueType type, ValueUnit unit,
      ValueRange range, String description)
  {
    module_ = null;
    name_ = name;
    linked_ = false;
    type_ = type;
    unit_ = unit;
    range_ = range;
    description_ = description;
    // Our ByteBuffer should contain Schedler.SamplingBufferSize samples.
    // Each sample is 8 bytes (i.e. Double.SIZE/8)
    values_ = ByteBuffer.allocate(Scheduler.SamplingBufferSize
        * (Double.SIZE / 8));
    setValues(value);
    initialValue_ = value;
  }

  @Override
  public String getName()
  {
    return name_;
  }

  @Override
  public void setValues(ByteBuffer values)
  {
    // Synchronize the value to prevent race conditions when they are concurrently modified
    synchronized (this)
    {
      // We clear all the streams to reset their position at 0
      values.clear();
      values_.clear();
      values_.put(values);
      values_.clear();
      values.clear();
    }
    // Notify our observers that our value has changed!
    setChanged();
    notifyObservers();
  }

  public void setValues(double value)
  {
    // Synchronize the value to prevent race conditions when they are concurrently modified
    synchronized (this)
    {
      // We clear all the streams to reset their position at 0
      values_.clear();
      for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
        values_.putDouble(value);
      values_.clear();
    }
    // Notify our observers that our value has changed!
    setChanged();
    notifyObservers();
  }

  public void resetValue()
  {
    setValues(initialValue_);
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
    if (getModule() == null || getModule().getInputs() == null)
      return false;

    // Check if we are contained in the inputs of or owner module
    return getModule().getInputs().contains(this);
  }

  @Override
  public boolean isOutput()
  {
    // Sanity check
    if (getModule() == null || getModule().getOutputs() == null)
      return false;

    // Check if we are contained in the outputs of or owner module
    return getModule().getOutputs().contains(this);
  }

  @Override
  public boolean isLinked()
  {
    return linked_;
  }

  @Override
  public void setLinked(boolean linked)
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

  @Override
  public String getDescription()
  {
    return description_;
  }
  
  @Override
  public String toString()
  {
    return getName() + " ( " + getValueUnit() + " )";
  }
  
  //--- Cloneable
  public Port clone() throws CloneNotSupportedException
  {
    return new BasicPort( name_, initialValue_, type_, unit_, range_, description_ );
  }

  private String     description_;
  private double     initialValue_;
  private boolean    linked_;
  private String     name_;
  private ByteBuffer values_;
  private Module     module_;
  private ValueType  type_;
  private ValueUnit  unit_;
  private ValueRange range_;
}
