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
  
  // --- Meta data
  public static enum ValueType
  {
    CONTINUOUS,
    DISCRETE,
    NON_CONFIGURABLE
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
  public ValueType getValueType();
  public ValueUnit getValueUnit();
  public ValueRange getValueRange();
  
  // --- Module
  public Module getModule();
  
  public void setModule(Module module);
}
