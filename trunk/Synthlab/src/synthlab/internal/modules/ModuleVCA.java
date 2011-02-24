package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;



/**
 * Concrete module class of VCA
 * 2 parameters , 1 for input signal, 1 for gain of this signal
 * We can control the parameters gain(volume) manually in UI,or we can do it with a external control signal
 * @author Dayou
 * */

public class ModuleVCA extends BasicModule
{
  private double       volume;
  private int          frameCount_;

  private final double maxVolume = 1;
  private final double minVolume = -1;

  

  /**
   * Constructor of VCA initial all ports E/S, initial name of module
   * */
  
  public ModuleVCA()
  {
    super("VCA");

    // port In (input)(Gain)

    addInput(new BasicPort("iSignal", 0, Port.ValueType.INCONFIGURABLE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Input sound wave"));
    addInput(new BasicPort("iGain", 1.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 2),"Gain (volume) amplitude modifier"));

    // port out (output)
    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Output sound wave whose amplitude was modified"));

    // init volume a 0
    volume = 0.0;

  }

  
  /**
   * compute method 
   * */
  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      synchronized (getInput("iGain"))
      {
        synchronized (getOutput("oSignal"))
        {
          getInput("iSignal").getValues().clear();
          getInput("iGain").getValues().clear();
          getOutput("oSignal").getValues().clear();

          for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
          {
            double val = getInput("iSignal").getValues().getDouble(); // Signal
            double gain = 0.0;
                                                                      // input
            if(getInput("iGain").isLinked()){
              gain = getInput("iGain").getValues().getDouble()+1;
            }else{
              gain = getInput("iGain").getValues().getDouble()*0.5;
            }
            this.setVolume(val * gain);

            frameCount_ = ++frameCount_ % 44100;

            getOutput("oSignal").getValues().putDouble(volume);
          }
        }
      }
    }
  }
  
  /**
   * getter of volume
   * */

  public double getVolume()
  {
    return volume;
  }
  
  /**
   * setter of volume
   * */

  public void setVolume(double vol)
  {
    if (vol > minVolume && vol < maxVolume)
      volume = vol;
    else if (vol > maxVolume)
      volume = maxVolume;
    else if (vol < minVolume)
      volume = minVolume;
  }
  
  
  /**
   * Method for create a new module of VCA
   * */
  
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleVCA();
  }
}
