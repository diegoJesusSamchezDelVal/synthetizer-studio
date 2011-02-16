package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleMixer extends BasicModule
{
  private double       valueOut;
  
  private double       input1;
  private double       input2;
  private double       input3;
  private double       input4;
  
  private final double maxVolume = 1;
  private final double minVolume = -1;
  
  public ModuleMixer()
  {
    super("MIXER");
    // Input1 & his gain
    addInput(new BasicPort("iInput1", 0, Port.ValueType.INCONFIGURABLE, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1),"Input sound wave 1"));
    addInput(new BasicPort("iGain1", 0.5, Port.ValueType.CONTINUOUS, Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 100),"Gain (volume) modifier for amplitude of sound wave 1"));
    // Input2 & his gain
    addInput(new BasicPort("iInput2", 0, Port.ValueType.INCONFIGURABLE, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Input sound wave 1"));
    addInput(new BasicPort("iGain2", 0.5, Port.ValueType.CONTINUOUS, Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 100),"Gain (volume) modifier for amplitude of sound wave 1"));
    // Input3 & his gain
    addInput(new BasicPort("iInput3", 0, Port.ValueType.INCONFIGURABLE, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Input sound wave 1"));
    addInput(new BasicPort("iGain3", 0.5, Port.ValueType.CONTINUOUS, Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 100),"Gain (volume) modifier for amplitude of sound wave 1"));
    // Input4 & his gain
    addInput(new BasicPort("iInput4", 0, Port.ValueType.INCONFIGURABLE, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Input sound wave 1"));
    addInput(new BasicPort("iGain4", 0.5, Port.ValueType.CONTINUOUS, Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 100),"Gain (volume) modifier for amplitude of sound wave 1"));
    
    //Output
    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Output sound wave mixed (averaged) by all inputs/gains"));
  }

  @Override
  public void compute()
  {
    synchronized (getInput("iInput1"))
    {
      synchronized (getInput("iGain1"))
      {
        synchronized (getInput("iInput2"))
        {
          synchronized (getInput("iGain2"))
          { 
            synchronized (getInput("iInput3"))
            {
              synchronized (getInput("iGain3"))
              {
                synchronized (getInput("iInput4"))
                {
                  synchronized (getInput("iGain4"))
                  { 
                    synchronized (getOutput("oSignal"))
                    {
                      getInput("iInput1").getValues().clear();
                      getInput("iGain1").getValues().clear();
                      getInput("iInput2").getValues().clear();
                      getInput("iGain2").getValues().clear();
                      getInput("iInput3").getValues().clear();
                      getInput("iGain3").getValues().clear();
                      getInput("iInput4").getValues().clear();
                      getInput("iGain4").getValues().clear();

                      getOutput("oSignal").getValues().clear();
                     
                      double val;
                      double gain;

                      for(int i = 0; i< Scheduler.SamplingBufferSize; ++i){
                        
                        val = getInput("iInput1").getValues().getDouble();
                        gain = getInput("iGain1").getValues().getDouble();
                        input1 = val*gain;
                       
                        val = getInput("iInput2").getValues().getDouble();
                        gain = getInput("iGain2").getValues().getDouble();
                        input2 = val*gain;
                        
                        val = getInput("iInput3").getValues().getDouble();
                        gain = getInput("iGain3").getValues().getDouble();
                        input3 = val*gain;
                        
                        val = getInput("iInput4").getValues().getDouble();
                        gain = getInput("iGain4").getValues().getDouble();
                        input4 = val*gain;
                        
                        valueOut =input1+input2+input3+input4;
                        
                        if(valueOut>minVolume && valueOut<maxVolume)
                          getOutput("oSignal").getValues().putDouble(valueOut);
                        else if(valueOut>maxVolume)
                          getOutput("oSignal").getValues().putDouble(maxVolume);
                        else if(valueOut<minVolume)
                          getOutput("oSignal").getValues().putDouble(minVolume);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }  
  }
  
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleMixer();
  }
}
