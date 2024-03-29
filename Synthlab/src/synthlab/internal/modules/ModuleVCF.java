package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;


/**
 * Concrete module class of VCF
 * Parameters are cutOff in Hz, manually or externally; resonance in Db, manually add for the output signal
 * 4 output signals, high pass, low pass, banned pass, Notch.
 * @author Dayou
 * */
public class ModuleVCF extends BasicModule
{
  
  //output signal
  private double  valueOut;
  
  private final int frameRate_;
  private final double maxVolume = 1;
  private final double minVolume = -1;
  
  // for remenber precent computed value 
  private double       lowPassPole  = 0.0;
  private double       bandPassPole = 0.0;
  
  /**
   * Constructor of VCF initial all ports E/S, initial name of module
   * */

  public ModuleVCF()
  {
    super("VCF");
    // Input port
    addInput(new BasicPort("iSignal", 0, Port.ValueType.INCONFIGURABLE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Input sound wave"));
    addInput(new BasicPort("iCutOff", 100, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.HERTZ, new Port.ValueRange(100, 5000),"Frequence cutoff setup"));
    addInput(new BasicPort("iResonance", 0.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.DECIBELS, new Port.ValueRange(0.0, 6.0),"Decibels add for the volume"));

    // OutPut port
    addOutput(new BasicPort("oLowPass", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Pass lower signals then cutoff"));
    addOutput(new BasicPort("oHighPass", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Pass higher signals then cutoff"));
    addOutput(new BasicPort("oBandPass", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Passes signals with frequencies of a certain range"));
    addOutput(new BasicPort("oNotch", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Signals with frequencies of a certain range banned, others pass"));

    frameRate_ = 44100;
    
  }

  /**
   * compute method 
   * */
  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      synchronized (getInput("iCutOff"))
      {
        synchronized (getInput("iResonance"))
        {
          synchronized (getOutput("oLowPass"))
          {
            synchronized (getOutput("oHighPass"))
            {
              synchronized (getOutput("oBandPass"))
              {
                synchronized (getOutput("oNotch"))
                {
                  //clear all ports
                  getInput("iSignal").getValues().clear();
                  getInput("iCutOff").getValues().clear();
                  getInput("iResonance").getValues().clear();
                  getOutput("oLowPass").getValues().clear();
                  getOutput("oHighPass").getValues().clear();
                  getOutput("oBandPass").getValues().clear();
                  getOutput("oNotch").getValues().clear();

                  for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
                  {
                    //get input Signal
                    double sigalIn = getInput("iSignal").getValues().getDouble();
                      
                    //get cutOff Setup
                    double cutOff = getInput("iCutOff").getValues().getDouble();
                   
                    //get resonance Setup with decibels
                    //decibels = 20*log(Vout/Vin)
                    double resonance = Math.pow(10.0, getInput("iResonance").getValues().getDouble() / 20.0)-1.0;

                    //RC = 1/4*PI*cutOff
                    final double rc = 1.0 / (4.0 * Math.PI * cutOff);
                    
                    //Delta T = 1/FrameRate
                    //alpha = Delta T / Delta T + RC
                    final double alpha = (1.0 / frameRate_)/ (1.0 / frameRate_ + rc);
                    
                    //PassBas = prePassBas + alpha*(Input - prePassBas)
                    double lowPass = lowPassPole + alpha * (sigalIn - lowPassPole);
                    
                    //prePassBas = PassBas
                    lowPassPole = lowPass;
                    
                    
                    //PassHaut = Input - PassBas
                    double highPass = sigalIn - lowPass;
                    
                    //BandPass = preBandPass+ alpha*(PassHaut - preBandPass)
                    double bandPass = bandPassPole + alpha * (highPass - bandPassPole);
                    
                    //preBandPass = BandPass
                    bandPassPole = bandPass;
                    
                    
                    //Comput with their resonance
                    lowPass += bandPass * resonance;
                    highPass += bandPass * resonance;
                    bandPass += bandPass * resonance;
  
                    //Comput notch = Input - BandPass , BandPass computed with resonance
                    final double notch = sigalIn - bandPass;
                    
                    
                    // setup 4 outPuts
                    setValue(lowPass);
                    getOutput("oLowPass").getValues().putDouble(valueOut);

                    setValue(highPass);
                    getOutput("oHighPass").getValues().putDouble(valueOut);
                    
                    setValue(bandPass);
                    getOutput("oBandPass").getValues().putDouble(valueOut);

                    setValue(notch);
                    getOutput("oNotch").getValues().putDouble(valueOut);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * getter of value
   * */
  public double getValue()
  {
    return valueOut;
  }
  
  /**
   * setter of value
   * */

  public void setValue(double val)
  {
    if (val > minVolume && val < maxVolume)
      valueOut = val;
    else if (val > maxVolume)
      valueOut = maxVolume;
    else if (val < minVolume)
      valueOut = minVolume;
  }

  
  /**
   * Method for create a new module of VCF
   * */
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleVCF();
  }
}
