package synthlab.internal.modules;

import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;
import util.ValGlobales;

public class ModuleVCO extends BasicModule
{

  public ModuleVCO()
  {
    super("VCO");

    addInput(new BasicPort("iFrequency", 0));
    addInput(new BasicPort("iConstant", 1));
    addOutput(new BasicPort("oSignal", 0));

    frameCount_ = 0;
    frameRate_ = 44100;
    initialFrequency_ = 440.0;
    signalMode_ = ValGlobales.SIGNAL_SQUARE;
  }

  public int getSingalMode()
  {
    return signalMode_;
  }

  public void setSingalMode(int signalMode)
  {
    this.signalMode_ = signalMode;
  }

  @Override
  public void compute()
  {
    // double ifreq = getInput("iFrequency").getValue();
    // double iconst = getInput("iConstant").getValue();
    double out = 0;
    
    switch (signalMode_)
    {
      case ValGlobales.SIGNAL_SINE:
        out = Math.sin(((double) frameCount_ / (double) frameRate_)
            * initialFrequency_ * 2. * Math.PI);
        break;
      case ValGlobales.SIGNAL_SAWTOOTH:
        break;
      case ValGlobales.SIGNAL_TRIANGLE:
        break;
      case ValGlobales.SIGNAL_SQUARE:
        break;
      default:
        System.out.println("unknown signal mode.");
        break;
    }
    
    getOutput("oSignal").setValue(out);
    frameCount_ = ++frameCount_ % 44100;
  }

  private int          frameCount_;
  private int          frameRate_;
  private double       initialFrequency_;
  private int          signalMode_;
}
