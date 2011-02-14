package synthlab.internal.modules;

import synthlab.api.*;
import synthlab.internal.*;

public class ModuleKeyboard extends BasicModule
{

  public ModuleKeyboard()
  {
    super("Keyboard");

    addInput(new BasicPort("iSignal", 0, Port.ValueType.KEYBOARD,
        Port.ValueUnit.VOLT, new Port.ValueRange(-1, 1),"The fraction of volt/octave representing the note"));
    addInput(new BasicPort("iOctave", 3, Port.ValueType.DISCRETE,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 7, 7),"Octave modulation input following the 1v/octave convention"));

    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 8),"Output note voltage following the 1v/ocatave convention"));

    addOutput(new BasicPort("oActive", 0, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1), "Active control signal"));
  }

  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      synchronized (getInput("iOctave"))
      {
        getInput("iSignal").getValues().clear();
        getInput("iOctave").getValues().clear();
        getOutput("oSignal").getValues().clear();
        getOutput("oActive").getValues().clear();
  
        for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
        {
          // 0 ~ 23
          double n = getInput("iSignal").getValues().getDouble();
          double octave = getInput("iOctave").getValues().getDouble();
          double volt = 0.0;          
          if(n >= 0.0) {
              volt = n / 24 * 2 + octave;             
              getOutput("oActive").getValues().putDouble(1);
          } else {           
            getOutput("oActive").getValues().putDouble(0);
          }
          getOutput("oSignal").getValues().putDouble(volt);
        }
      }
    }
  }

  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleKeyboard();
  }
}
