package synthlab.internal.modules;

import java.nio.*;
import javax.sound.sampled.*;

import synthlab.api.*;
import synthlab.api.Port;
import synthlab.internal.*;


/**
 * Concrete module class of Speaker who transport the signal to the son card 
 * 1 input signal only
 * @author Dayou
 * */

public class ModuleOut extends BasicModule
{
 
  /**
   * Constructor of Speaker initial input port, initial the name of module
   * */
  public ModuleOut()
  {
    super("Speaker");

    addInput(new BasicPort("iSignal", 0, Port.ValueType.INCONFIGURABLE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Input sound waveto be sent to the sound card"));

    data_ = ByteBuffer.allocate(Scheduler.SamplingBufferSize * 2);
  }

  
  /**
   * compute method 
   * */
  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      getInput("iSignal").getValues().clear();

      for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
      {
        data_.putShort(i * 2, (short) (getInput("iSignal").getValues()
            .getDouble(i * 8) * Short.MAX_VALUE));
      }

      
      Audio.getLine().write(data_.array(), 0, Scheduler.SamplingBufferSize * (Short.SIZE/8));

      getInput("iSignal").getValues().clear();
    }
  }
  
  /**
   * Method for create a new module of Speaker
   * */
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleOut();
  }

  AudioInputStream stream_;
  DataLine.Info    lineInfo_;
  ByteBuffer       data_;
}
