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
    addOutput(new BasicPort("oFrequency", 0));
  }

  @Override
  public void compute()
  {
    double ifreq = getInput("iFrequency").getValue();
    double iconst = getInput("iConstant").getValue();
    double out = (int) Math.pow(2., (ifreq + iconst));
    getOutput("oFrequency").setValue(out);
  }
}
