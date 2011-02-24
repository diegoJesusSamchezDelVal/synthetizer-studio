package synthlab.api;

import java.nio.ByteBuffer;
import java.util.Observable;

/**
 * This is one of the core classes of the synthesizer. A Port instance can represent
 * either an input or an output port (depending how you add it to its module, using addInput()
 * or addOutput()). A Port contains a buffer of samples that can be read or written by the module.
 */
public abstract class Port extends Observable
{
  //================================================ Name
  /**
   * This will return the name of the port. The convention is to prefix
   * input port names with a "i" and output port names with an "o".
   * e.g. iSignal, oFrequency...
   */
  public abstract String getName();
  
  //================================================ Description
  /**
   * This is a short description of this port. It could be queried
   * if you wonder what is the use of this port, or in tooltips if
   * you use a GUI.
   */
  public abstract String getDescription();
  
  //================================================ Value
  /**
   * This method will overwrite the value of this port's ByteBuffer
   * with the one you provide. Be sure to provide a ByteBuffer which
   * size is the same and the previous values (usually Scheduler.SamplingBufferSize)
   * otherwise the results are undefined.
   */
  public abstract void setValues(ByteBuffer value);
  
  /**
   * This wil overwrite the values contained in this port's ByteBuffer
   * with 'value'. This is usefull to set a whole sample set equal to a
   * same value (for instance when reseting a port).
   * @param value the value to be copied in this port's ByteBuffer
   */
  public abstract void setValues(double value);
  
  /**
   * This will overwrite this port's ByteBuffer with its original value defined
   * when it was created. This is usefull for instance if you want to simulate
   * that the port was unplugged.
   */
  public abstract void resetValue();
  
  /**
   * This will return the current ByteBuffer representing the values that this port
   * contains. Be sure not to modify it unless you specifically synchronize the port!.
   * @return the ByteBuffer of this port.
   */
  public abstract ByteBuffer getValues();
  
  //================================================ Input/Output
  /**
   * This will return true if this port is an input port, false otherwise. If this
   * port is not attached to any module, the result is undefined.
   */
  public abstract boolean isInput();
  
  /**
   * This will return true if this port is an output port, false otherwise. If this
   * port is not attached to any module, the result is undefined.
   */
  public abstract boolean isOutput();
  
  //================================================ Status
  /**
   * This will return true if this port is linked to an other one.
   */
  public abstract boolean isLinked();
  
  /**
   * This method is dangerous and should be called with care. This is used to set the
   * status of the port to linked or unlinked. You can use it to force a port as unliked
   * after you can deleted all its links. The only use of this method should be made through
   * ModulePool instances, or if you are creating your custom port handling classes.
   * @param linked true if this port is linked, false otherwise
   */
  public abstract void setLinked( boolean linked );
  
  //================================================ Meta data
  /**
   * This enumeration represents the kind of vale that this port handles. Wave signals output
   * by oscillators are considered continous. Discrete signals could be e.g. a gate.
   */
  public static enum ValueType
  {
    CONTINUOUS,
    DISCRETE,
    KEYBOARD,
    WAVE_SHAPE,
    SWITCH,
    INCONFIGURABLE    
  }
  /**
   * This enumeration represents the unit in which the values contained by this port are stored.
   * When you want to link ports together, it would be safe to check that they share the same
   * unit. (A GUI should provide semantic retroaction feedback to show the matching between port
   * units before linking them).
   */
  public static enum ValueUnit
  {
    HERTZ,
    VOLT,
    PERCENTAGE,
    DECIBELS,
    MILLISECONDS,
    AMPLITUDE
  }
  /***
   * This is the range of values that this port can handle. When using a discrete port, you can
   * also provide the number of ranges that are available to use.
   */
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
  /**
   * This will return the type of value that is port is handling.
   * @return the type of value that this port handles
   */
  public abstract ValueType getValueType();
  
  /**
   * This will return the unit in which the values of this port are stored.
   * @return the unit in which the values of this port are stored.
   */
  public abstract ValueUnit getValueUnit();
  
  /**
   * This will return the range of values (min/max) that this port handles. 
   * @return
   */
  public abstract ValueRange getValueRange();
  
  //================================================ Module
  /**
   * This will return the module that contains this port. If the port was not
   * previously bound to an existing module, this will return null.
   * @return the module that contains this port, null otherwise.
   */
  public abstract Module getModule();
  
  /**
   * This will assign a new module to this port. You generally should not use this
   * method as it won't inform the module that is port is no longer owned!
   * It is present for users willing to create their own module pool.
   * @param module
   */
  public abstract void setModule(Module module);
  
  //================================================ Cloneable
  /**
   * Any port should be possible to clone
   */
  public abstract Port clone() throws CloneNotSupportedException;
}
