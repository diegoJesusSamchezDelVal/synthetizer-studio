package synthlab.internal.modules;

import java.nio.*;
import javax.sound.sampled.*;

import synthlab.api.*;
import synthlab.api.Port;
import synthlab.internal.*;

public class ModuleOut extends BasicModule
{
  public ModuleOut()
  {
    super("Out");

    addInput(new BasicPort("iSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Input sound waveto be sent to the sound card"));

    data_ = ByteBuffer.allocate(Scheduler.SamplingBufferSize * 2);
  }

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
  
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleOut();
  }

  AudioInputStream stream_;
  DataLine.Info    lineInfo_;
  ByteBuffer       data_;
}
