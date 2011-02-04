package synthlab.internal.modules;

import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class Out extends BasicModule{

  public Out(String name)
  {
    super("parleur");
    addInput(new BasicPort("iFrequency", 0));
  }

  @Override
  public void compute()
  {
    double ifreq = getInput("iFrequency").getValue();
    
  }
  
  
  

}
