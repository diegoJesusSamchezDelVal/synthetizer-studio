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
    maxVolume_ = (int) Math.pow(2.0, 15.0);
    signalMode_ = ValGlobales.SIGNAL_SINE;
    outBuffer_ = new byte[44100 * 2];
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
    double out;
    switch (signalMode_)
    {
      case ValGlobales.SIGNAL_SINE:
        out = Math.sin(((double) frameCount_ / (double) frameRate_)
            * initialFrequency_ * 2. * Math.PI);
        // out *= Math.pow(2, ifreq+iconst);
        getOutput("oSignal").setValue(out);
        frameCount_ = ++frameCount_ % 44100;
        break;
        /*
         * case ValGlobales.SIGNAL_SQUARE: for(int sample=0; sample<44100;
         * sample++) { if(sample%initialFrequency_<= initialFrequency_/2){
         * outBuffer[frameRate_*2] = (byte)(1 & 0xFF); outBuffer[frameRate_*2 +
         * 1] = (byte)((1 & 0xFF00) >> 8); }else{ outBuffer[frameRate_*2] =
         * (byte)(0 & 0xFF); outBuffer[frameRate_*2 + 1] = (byte)((0 & 0xFF00)
         * >> 8); } }
         */
      case ValGlobales.SIGNAL_SAWTOOTH:
        break;
      case ValGlobales.SIGNAL_TRIANGLE:
        break;
      default:
        System.out.println("unknown signal mode.");
        break;
    }
  }

  private int          frameCount_;
  private int          frameRate_;
  private int          maxVolume_;
  private double       initialFrequency_;
  private double       currentPositionInPeriod_;
  private final double periodPerSample_ = initialFrequency_ / frameRate_;
  private int          signalMode_;
  byte[]               outBuffer_;
}
