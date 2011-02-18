package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;


/**
 * Concrete module class of LFO
 * 1 output product low frequency sinusoidal wave signal
 * @author
 * */
public class ModuleLFO extends BasicModule
{
  

  private int frameCount_;
  private int frameRate_;
  
  
  /**
   * Constructor of LFO initial all ports E/S, initial name of module
   * initial base Data: frame rate,
   * */
  public ModuleLFO()
  {
    super("LFO");

    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1),"Low frequency sinusoidal wave (period 1s)"));

    frameCount_ = 0;
    frameRate_ = 44100;
  }
  
  

  /**
   * compute method 
   * */

  @Override
  public void compute()
  {
    synchronized (getOutput("oSignal"))
    {
      getOutput("oSignal").getValues().clear();

      for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
      {
        double out = 0;

        double positionInPeriod = (double) frameCount_ / (double) frameRate_;

        out = (Math.sin(positionInPeriod  * 2. * Math.PI)+1.)/2.;

        getOutput("oSignal").getValues().putDouble(out);
        frameCount_ = ++frameCount_ % 44100;
      }
      getOutput("oSignal").getValues().clear();
    }
  }
  
  /**
   * Method for create a new module of LFO
   * */
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleLFO();
  }

}
