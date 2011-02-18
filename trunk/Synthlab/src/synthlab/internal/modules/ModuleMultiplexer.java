package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;


/**
 * Concrete module class of Hub
 * 1 input signal 
 * 4 output signal just transport the value of input
 * @author Dayou
 * */

public class ModuleMultiplexer extends BasicModule
{
  
  /**
   * Constructor of Hub, initial all ports E/S, initial name of module
   * */
  public ModuleMultiplexer()
  {
    super("Hub");

    addInput(new BasicPort("iSignal", 0, Port.ValueType.INCONFIGURABLE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), "Input signal"));

    addOutput(new BasicPort("oSignal1", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), "Output signal 1"));
    addOutput(new BasicPort("oSignal2", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), "Output signal 2"));
    addOutput(new BasicPort("oSignal3", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), "Output signal 3"));
    addOutput(new BasicPort("oSignal4", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), "Output signal 4"));
  }

  
  /**
   * compute method 
   * */
  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      synchronized (getOutput("oSignal1"))
      {
        synchronized (getOutput("oSignal2"))
        {
          synchronized (getOutput("oSignal3"))
          {
            synchronized (getOutput("oSignal4"))
            {
              getInput("iSignal").getValues().clear();
              getOutput("oSignal1").getValues().clear();
              getOutput("oSignal2").getValues().clear();
              getOutput("oSignal3").getValues().clear();
              getOutput("oSignal4").getValues().clear();

              for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
              {
                final double data = getInput("iSignal").getValues().getDouble();
                getOutput("oSignal1").getValues().putDouble(data);
                getOutput("oSignal2").getValues().putDouble(data);
                getOutput("oSignal3").getValues().putDouble(data);
                getOutput("oSignal4").getValues().putDouble(data);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Method for create a new module of Hub
   * */
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleMultiplexer();
  }

}
