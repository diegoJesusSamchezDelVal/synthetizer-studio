package synthlab.internal.modules;

import java.awt.image.SampleModel;

import org.omg.CORBA.FREE_MEM;

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

    //frameCount_ = 0;
    frameRate_ = 44100;
    frequency_ = 440.0;
    framePerPeriod_ = (double)frameRate_ / frequency_;
    currentPositionInPeriod_ = 0.0;
    signalMode_ = ValGlobales.SIGNAL_SINE;
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
    
    double currentPositionInPercent = currentPositionInPeriod_/framePerPeriod_;
    switch (signalMode_)
    {
      case ValGlobales.SIGNAL_SINE:
        out = Math.sin(currentPositionInPercent * 2.0 * Math.PI);
        break;
      case ValGlobales.SIGNAL_SAWTOOTH:
        out = 2.0*currentPositionInPeriod_/framePerPeriod_ - 1.0;
        break;
      case ValGlobales.SIGNAL_TRIANGLE:
        if(currentPositionInPercent <0.25)
          out = 4.0*currentPositionInPercent;
        else if (currentPositionInPercent<0.75)
          out = 2.0 - 4.0*currentPositionInPercent;
        else 
          out = 4.0*currentPositionInPercent - 4.0;
        break;
      case ValGlobales.SIGNAL_PULSE:
        out = currentPositionInPercent>0.5? 1.0 : 0.0;
        break;
      default:
        System.out.println("unknown signal mode.");
        break;
    }
    
    getOutput("oSignal").setValue(out);
    currentPositionInPeriod_ +=  1.0 ;
    if(currentPositionInPeriod_ >= framePerPeriod_)
      currentPositionInPeriod_ -= framePerPeriod_;
  }

  public static final double SHAPE_SQUARE   = -0.5;
  public static final double SHAPE_SINE     = -0.0;
  public static final double SHAPE_TRIANGLE = +0.5;
  public static final double SHAPE_SAWTOOTH = +1.0;

 // private int          frameCount_;
  private int          frameRate_;
  private double       frequency_;
  private double       framePerPeriod_;
  private double       currentPositionInPeriod_;
  private int          signalMode_;
}
