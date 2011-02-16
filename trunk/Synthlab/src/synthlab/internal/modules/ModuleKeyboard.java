package synthlab.internal.modules;

import synthlab.api.*;
import synthlab.internal.*;

public class ModuleKeyboard extends BasicModule
{

  public ModuleKeyboard()
  {
    super("Keyboard");

    addInput(new BasicPort("iSignal1", -1, Port.ValueType.KEYBOARD,
        Port.ValueUnit.VOLT, new Port.ValueRange(-1, 23),"The fraction of volt/octave representing the note"));
    addInput(new BasicPort("iSignal2", -1, Port.ValueType.KEYBOARD,
        Port.ValueUnit.VOLT, new Port.ValueRange(-1, 23),"The fraction of volt/octave representing the note"));
    addInput(new BasicPort("iSignal3", -1, Port.ValueType.KEYBOARD,
        Port.ValueUnit.VOLT, new Port.ValueRange(-1, 23),"The fraction of volt/octave representing the note"));
    addInput(new BasicPort("iOctave", 3.0, Port.ValueType.DISCRETE,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 7, 7),"Octave modulation input following the 1v/octave convention"));

    addOutput(new BasicPort("oSignal1", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 2),"Output note voltage following the 1v/ocatave convention"));
    addOutput(new BasicPort("oSignal2", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 2),"Output note voltage following the 1v/ocatave convention"));
    addOutput(new BasicPort("oSignal3", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 2),"Output note voltage following the 1v/ocatave convention"));

    addOutput(new BasicPort("oActive", 0, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1), "Active control signal"));
  }

  @Override
  public void compute()
  {
    synchronized (getInput("iSignal1"))
    {
      synchronized (getInput("iSignal2"))
      {
        synchronized (getInput("iSignal3"))
        {
          synchronized (getInput("iOctave"))
          {
            getInput("iSignal1").getValues().clear();
            getInput("iSignal2").getValues().clear();
            getInput("iSignal3").getValues().clear();
            getInput("iOctave").getValues().clear();
            
            getOutput("oSignal1").getValues().clear();
            getOutput("oSignal2").getValues().clear();
            getOutput("oSignal3").getValues().clear();
            getOutput("oActive").getValues().clear();
    
            for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
            {
              boolean active = false;
              double octave = getInput("iOctave").getValues().getDouble();
              
              for(int j = 1; j < 4; j++) {
                // 0 ~ 23
                double n = getInput("iSignal"+j).getValues().getDouble();
                
                double volt = 0.0;          
                if(n >= 0.0) {
                                
                    volt = n / 24 * 2 + octave;             
                    active = true;
                }
                getOutput("oSignal"+j).getValues().putDouble(volt);            
           } //for
              
             if (active)
                getOutput("oActive").getValues().putDouble(1);
             else 
                getOutput("oActive").getValues().putDouble(0);
          }
          }
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
