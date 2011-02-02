package synthlab.internal.modules;

import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleVCO extends BasicModule
{
  public ModuleVCO()
  {
    setName("VCO");

    addInput(new BasicPort("iFrequency", 0, null));
    addInput(new BasicPort("iConstant", 1, null));
    addOutput(new BasicPort("oFrequency", 0, null));
  }

  @Override
  public void compute()
  {
    Integer ifreq = getInput("iFrequency").getValue();
    Integer iconst = getInput("iConstant").getValue();
    Integer out = (int) Math.pow(2., (ifreq + iconst));
    getOutput("oFrequency").setValue(out);
  }
}
