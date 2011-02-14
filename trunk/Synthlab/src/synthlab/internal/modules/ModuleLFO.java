package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleLFO extends BasicModule
{
  public ModuleLFO()
  {
    super("LFO");

    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1),"Low frequency sinusoidal wave (period 1s)"));

    frameCount_ = 0;
    frameRate_ = 44100;
  }

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
  
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleLFO();
  }

  private int frameCount_;
  private int frameRate_;
}
