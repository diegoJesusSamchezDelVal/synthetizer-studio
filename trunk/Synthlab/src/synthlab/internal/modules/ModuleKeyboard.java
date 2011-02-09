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
          Port.ValueUnit.VOLT, new Port.ValueRange(-1, 1)));
      addInput(new BasicPort("iOctave", -1, Port.ValueType.DISCRETE,
          Port.ValueUnit.VOLT, new Port.ValueRange(0, 7, 7)));
      
      addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
          Port.ValueUnit.VOLT, new Port.ValueRange(0, 8)));      
     
    }

    @Override
    public void compute()
    {
      synchronized (getInput("iSignal"))
      {
        getInput("iSignal").getValues().clear();
        getInput("iOctave").getValues().clear();
        getOutput("oSignal").getValues().clear();
       
        for (int i = 0; i < Scheduler.SamplingBufferSize; ++i) {
            // 0 ~ 23
           double n = getInput("iSignal").getValues().getDouble();
           double octave = getInput("iOctave").getValues().getDouble();
           double volt = 0.0;
           if(n >= 0)
             volt = n / 24 * 2 + octave;
            
            
            getOutput("oSignal").getValues().putDouble(volt);
            frameCount_ = ++frameCount_ % frameRate_;
        }
      }
    }

    private int                frameCount_ = 0;
    private int                frameRate_ = 44100;
  }

  
