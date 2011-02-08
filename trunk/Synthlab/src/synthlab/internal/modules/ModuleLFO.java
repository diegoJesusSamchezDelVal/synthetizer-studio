package synthlab.internal.modules;

import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleLFO extends BasicModule
{
  public ModuleLFO()
  {
    super("LFO");

    addOutput(new BasicPort("oSignal", 0));

    frameCount_ = 0;
    frameRate_ = 44100;
  }

  @Override
  public void compute()
  {
    getOutput("oSignal").getValues().clear();

    for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
    {
      double out = 0;

      double positionInPeriod = (double) frameCount_ / (double) frameRate_;

      out = Math.sin(positionInPeriod * 2. * Math.PI);

      getOutput("oSignal").getValues().putDouble(out);
      frameCount_ = ++frameCount_ % 44100;
    }
  }
  
  private int frameCount_;
  private int frameRate_;
}
