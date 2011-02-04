package synthlab.internal.modules;

import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;
import util.ValGlobales;

public class PitchIn extends BasicModule
{
  private int tension;

  public PitchIn()
  {
    super("PitchIn");
    tension = ValGlobales.PITCH_IN_START; 
    addOutput(new BasicPort("iVolume", tension));
    
  }

  @Override
  public void compute()
  {
    
  }

}
