package synthlab.internal.modules;

import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleVCO extends BasicModule
{
  public ModuleVCO()
  {
    super("VCO");

    addInput(new BasicPort("iFrequency", 0));
    addInput(new BasicPort("iConstant", 0));
    addInput(new BasicPort("iShape", -1));
    addOutput(new BasicPort("oSignal", 0));

    frameCount_ = 0;
    frameRate_ = 44100;
    initialFrequency_ = 440.0;
  }

  @Override
  public void compute()
  {
    double out = 0;
    double ishape = getInput("iShape").getValue();
    double ifreq = getInput("iFrequency").getValue();
    double iconst = getInput("iConstant").getValue();

    if (ishape <= SHAPE_SQUARE)
    {
      out = (Math.sin(((double) frameCount_ / (double) frameRate_) * initialFrequency_ * 2. * Math.PI)) >= 0 ? 1. : -1;
    }
    else if ( ishape <= SHAPE_SINE )
    {
      out = Math.sin(((double) frameCount_ / (double) frameRate_) * initialFrequency_ * 2. * Math.PI);
    }
    else if ( ishape <= SHAPE_TRIANGLE)
    {
      out = Math.sin(((double) frameCount_ / (double) frameRate_) * initialFrequency_ * 2. * Math.PI);
    }
    else if ( ishape <= SHAPE_SAWTOOTH )
    {
      out = Math.sin(((double) frameCount_ / (double) frameRate_) * initialFrequency_ * 2. * Math.PI);
    }
    else
    {
      assert(false);
    }
    
    out *= Math.pow( 2, ifreq + iconst );

    getOutput("oSignal").setValue(out);
    frameCount_ = ++frameCount_ % 44100;
  }

  public static final double SHAPE_SQUARE   = -0.5;
  public static final double SHAPE_SINE     = -0.0;
  public static final double SHAPE_TRIANGLE = +0.5;
  public static final double SHAPE_SAWTOOTH = +1.0;
  private int         frameCount_;
  private int         frameRate_;
  private double      initialFrequency_;
}
