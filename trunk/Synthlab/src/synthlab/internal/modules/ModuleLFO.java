package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;


/**
 * Concrete module class of LFO
 * 1 input for configuration his output signal frequency
 * 1 output product low frequency sinusoidal wave signal
 * @author
 * */
public class ModuleLFO extends BasicModule
{
  

  private int frameCount_;
  private int frameRate_;
  private double currentPositionInPeriod_;
  
  
  /**
   * Constructor of LFO initial all ports E/S, initial name of module
   * initial base Data: frame rate,
   * */
  public ModuleLFO()
  {
    super("LFO");
    
    addInput(new BasicPort("Frequency", 5.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.HERTZ,new Port.ValueRange(0.1, 10.0), "Set output frequency"));

    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1),"Low frequency sinusoidal wave"));

    currentPositionInPeriod_ = 0;
    frameCount_ = 0;
    frameRate_ = 44100;
  }
  
  

  /**
   * compute method 
   * */

  @Override
  public void compute()
  {
    synchronized (getInput("Frequency"))
    {
      synchronized (getOutput("oSignal"))
      {
        getInput("Frequency").getValues().clear();
        getOutput("oSignal").getValues().clear();
  
        for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
        {
          double frequency = getInput("Frequency").getValues().getDouble();
          
          double out = 0;
  
          double framePerPeriod_ = (double) frameRate_ / frequency;
          double currentPositionInPercent = currentPositionInPeriod_/framePerPeriod_;
  
          out = Math.sin(currentPositionInPercent * 2. * Math.PI);
          //double positionInPeriod = (double) frameCount_ / (double) frameRate_;
  
          // out = (Math.sin(positionInPeriod  * 2. * Math.PI)+1.)/2.;
  
          getOutput("oSignal").getValues().putDouble(out);
          frameCount_ = ++frameCount_ % 44100;
          currentPositionInPeriod_ +=  1.0 ;
          if(currentPositionInPeriod_ >= framePerPeriod_)
            currentPositionInPeriod_ -= framePerPeriod_;
        }
        //getOutput("oSignal").getValues().clear();
      }
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
