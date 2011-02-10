package synthlab.internal.modules;

import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleEnvelope extends BasicModule
{

  public ModuleEnvelope()
  {
    super("ENV");
    
    //add input port
    addInput(new BasicPort("iSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    addInput(new BasicPort("Attack", 0.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,100)));
    addInput(new BasicPort("Decay", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,100)));
    addInput(new BasicPort("Sustain", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 100)));
    addInput(new BasicPort("Relase", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,100)));
    
    //add output port
    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
  }

  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      synchronized (getInput("Attack"))
      {
        synchronized (getInput("Decay"))
        {
          synchronized (getInput("Sustain"))
          {
            synchronized (getInput("Relase"))
            {
              synchronized (getOutput("oSignal"))
              {
                getInput("iSignal").getValues().clear();
                getInput("Attack").getValues().clear();
                getInput("Decay").getValues().clear();
                getInput("Sustain").getValues().clear();
                getInput("Relase").getValues().clear();
                getOutput("oSignal").getValues().clear();
                
                
                for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
                {
                  
                }
                
                
              }
            }
          }
        }
      }
    }
    
  }

}
