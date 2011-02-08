package synthlab.internal.modules;

import synthlab.api.Port;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;


public class ModuleVCA extends BasicModule
{

  private double volume;
  
  // taux d'echantillon 16bit 
  private final double maxVolume = 32534;
  private final double minVolume =-32534;
  
  
  public ModuleVCA()
  {
    super("VCA");
    
    //port In (input)(Gain)
    addInput(new BasicPort("iSignal", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    addInput(new BasicPort("iGain", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 1)));
    
    //port out (output)
    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS, Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));
    
    //init a 0
    volume = 0.0;
  }

  @Override
  public void compute()
  {
    // TODO Auto-generated method stub
    
  }

  public double getVolume()
  {
    return volume;
  }

  public void setVolume(double vol)
  {
    if(vol>minVolume && vol<maxVolume)volume = vol;
    else if(vol>maxVolume) volume = maxVolume;
    else if(vol<minVolume) volume = minVolume;
  }

}
