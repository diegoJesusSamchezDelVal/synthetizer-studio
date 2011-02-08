package synthlab.internal.modules;

import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleVCA extends BasicModule
{

  private double       volume;
  private int          frameCount_;

  // mode
  // private int mode;
  // private final int LINEAR = 1;
  // private final int EXPOL = 2;

  private final double maxVolume = 1;
  private final double minVolume = -1;

  public ModuleVCA()
  {
    super("VCA");

    // port In (input)(Gain)

    addInput(new BasicPort("iSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    addInput(new BasicPort("iGain", 0.5, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 1)));

    // port out (output)
    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));

    // init a 0
    volume = 0.0;

    // init mode
    // mode = LINEAR;
  }

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
                                                                      // input
            double gain = getInput("iGain").getValues().getDouble();
            this.setVolume(val * gain);

            frameCount_ = ++frameCount_ % 44100;

            getOutput("oSignal").getValues().putDouble(volume);
          }
        }
      }
    }
  }

  public double getVolume()
  {
    return volume;
  }

  public void setVolume(double vol)
  {
    if (vol > minVolume && vol < maxVolume)
      volume = vol;
    else if (vol > maxVolume)
      volume = maxVolume;
    else if (vol < minVolume)
      volume = minVolume;
  }
}
