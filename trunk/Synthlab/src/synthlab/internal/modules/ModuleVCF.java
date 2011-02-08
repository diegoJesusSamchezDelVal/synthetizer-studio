package synthlab.internal.modules;

import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleVCF extends BasicModule
{
  private int          frameCount_;
  private double       valueOut;
  
  private final double maxVolume = 1;
  private final double minVolume = -1;

  public ModuleVCF()
  {
    super("VCF");
    // Input port
    addInput(new BasicPort("iSignal", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    addInput(new BasicPort("iCutOff", 100, Port.ValueType.CONTINUOUS, Port.ValueUnit.HERTZ, new Port.ValueRange(100,5000 )));
    addInput(new BasicPort("iResonance", 0, Port.ValueType.DISCRETE, Port.ValueUnit.DECIBELS, new Port.ValueRange(0.0,10.0)));
    
    //OutPut  port
    addOutput(new BasicPort("oLowPass", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    addOutput(new BasicPort("oHighPass", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    addOutput(new BasicPort("oBandPass", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    addOutput(new BasicPort("oNotch", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    
  }

  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      synchronized (getInput("iCutOff"))
      {
        synchronized (getInput("iResonance"))
        {
          synchronized (getOutput("oLowPass"))
          { 
            synchronized (getOutput("oHighPass"))
            {
              synchronized (getOutput("oBandPass"))
              {
                synchronized (getOutput("oNotch"))
                {
                  getInput("iSignal").getValues().clear();
                  getInput("iCutOff").getValues().clear();
                  getInput("iResonance").getValues().clear();
                  getOutput("oLowPass").getValues().clear();
                  getOutput("oHighPass").getValues().clear();
                  getOutput("oBandPass").getValues().clear();
                  getOutput("oNotch").getValues().clear();
                  
                  for(int i = 0; i< Scheduler.SamplingBufferSize; ++i){
                    
                    double val = getInput("iSignal").getValues().getDouble();
                    double cutoff = getInput("iCutOff").getValues().getDouble();
                    double reso = getInput("iResonance").getValues().getDouble();
                    
                    //TODO
                    //--->oLowPass algo 
                    setValue(1);
                    getOutput("oLowPass").getValues().putDouble(valueOut);
                    //TODO
                    //--->oHighPass algo
                    setValue(1);
                    getOutput("oHighPass").getValues().putDouble(valueOut);
                    //TODO
                    //--->oBandPass algo
                    setValue(1);
                    getOutput("oBandPass").getValues().putDouble(valueOut);
                    //TODO
                    //--->oNotch algo
                    setValue(1);
                    getOutput("oNotch").getValues().putDouble(valueOut);
                    frameCount_ = ++frameCount_ % 44100;
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  public double getValue()
  {
    return valueOut;
  }

  public void setValue(double val)
  {
    if (val > minVolume && val < maxVolume)
      valueOut = val;
    else if (val > maxVolume)
      valueOut = maxVolume;
    else if (val < minVolume)
      valueOut = minVolume;
  }

  
}
