package synthlab.internal.modules;

import java.nio.*;
import javax.sound.sampled.*;

import synthlab.api.*;
import synthlab.api.Port;
import synthlab.internal.*;

public class ModuleKeyboard extends BasicModule
{ 
 
    public ModuleKeyboard()
    {
      super("Keyboard");
      
      addInput(new BasicPort("iSignal", 0, Port.ValueType.KEYBOARD,
          Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
      
      addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
          Port.ValueUnit.VOLT, new Port.ValueRange(0, 8)));
     
    }

    @Override
    public void compute()
    {
      synchronized (getInput("iSignal"))
      {
        getInput("iSignal").getValues().clear();
        getOutput("oSignal").getValues().clear();
       
        for (int i = 0; i < Scheduler.SamplingBufferSize; ++i) {
            // 0 ~ 23
           double n = getInput("iSignal").getValues().getDouble();
           double volt = n / 24 * 16;
            
            
            getOutput("oSignal").getValues().putDouble(volt);
            frameCount_ = ++frameCount_ % frameRate_;
        }
      }
    }

    private int                frameCount_ = 0;
    private int                frameRate_ = 44100;
  }

  
