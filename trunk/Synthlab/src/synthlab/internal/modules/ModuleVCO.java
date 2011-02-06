package synthlab.internal.modules;

import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

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
    initialFrequency_ = 440.;
  }

  @Override
  public void compute()
  {
    //double ifreq = getInput("iFrequency").getValue();
    //double iconst = getInput("iConstant").getValue();
    double out = Math.sin( ((double)frameCount_/(double)frameRate_) * initialFrequency_ * 2. * Math.PI);
    //out *= Math.pow(2, ifreq+iconst);
    getOutput("oSignal").setValue(out);

    frameCount_ = ++frameCount_ % 441400;
  }

  private int    frameCount_;
  private int    frameRate_;
  private double initialFrequency_;
}
