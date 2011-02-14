package synthlab.api;

import java.nio.ByteBuffer;
import java.util.Observable;

public abstract class Port extends Observable
{
  // --- Name
  public abstract String getName();
  
  // --- Description
  public abstract String getDescription();
  
  // --- Value
  public abstract void setValues(ByteBuffer value);
  public abstract void setValues(double value);
  public abstract void resetValue();
  public abstract ByteBuffer getValues();
  
  // --- Input/Output
  public abstract boolean isInput();
  
  public abstract boolean isOutput();
  
  // --- Status
  public abstract boolean isLinked();
  
  public abstract void setLinked( boolean linked );
  
  // --- Meta data
  public static enum ValueType
  {
    CONTINUOUS,
    DISCRETE,
    KEYBOARD,
    INCONFIGURABLE    
  }
  
  public static enum ValueUnit
  {
    HERTZ,
    VOLT,
    PERCENTAGE,
    DECIBELS,
    MILLISECONDS,
    AMPLITUDE
  }
  public static class ValueRange
  {
    public ValueRange( double min, double max, int c )
    {
      minimum = min;
      maximum = max;
      count = c;
    }
    public ValueRange( double min, double max)
    {
      minimum = min;
      maximum = max;
      count = 1;
    }
    public double minimum;
    public double maximum;
    public int count;
  }
  public abstract ValueType getValueType();
  public abstract ValueUnit getValueUnit();
  public abstract ValueRange getValueRange();
  
  // --- Module
  public abstract Module getModule();
  
  public abstract void setModule(Module module);
  
  //--- Cloneable
  public abstract Port clone() throws CloneNotSupportedException;
}
