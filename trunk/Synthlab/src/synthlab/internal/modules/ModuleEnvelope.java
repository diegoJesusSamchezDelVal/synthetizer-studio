package synthlab.internal.modules;

import javax.sql.rowset.spi.SyncFactory;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleEnvelope extends BasicModule
{

  private enum State {IDLE,ATTACK,DECAY,SUSTAIN,RELASE}
  private double attackInSimple,decayInSimple,releaseInSimple;
  private double sustainLevel;
  
  private final double frameRate_ = 44100;
  
  private double currentSimple;
  
  private State currentState = State.IDLE;
  
  //private final int frameRate_ = 44100;
  
  public ModuleEnvelope()
  {
    super("Envelope");
    
    //add input port
    addInput(new BasicPort("iExternalGate", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), "Extern controle for the state of Gate"));
    addInput(new BasicPort("iGate", 0, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), "Mannuly switch for Gate"));
    addInput(new BasicPort("iAttack", 0.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,10000), "Attack time controlor"));
    addInput(new BasicPort("iDecay", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,10000), "Decay time controlor"));
    addInput(new BasicPort("iSustain", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 100), "Sustain percentage controlor"));
    addInput(new BasicPort("iRelease", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,10000), "Relase time controlor"));
    
    //add output port
    addOutput(new BasicPort("oFrequency", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), "Frequency output"));
  }

  @Override
  public void compute()
  {
    synchronized (getInput("iExternalGate"))
    {
      synchronized (getInput("iGate"))
      { 
        synchronized (getInput("iAttack"))
        {
          synchronized (getInput("iDecay"))
          {
            synchronized (getInput("iSustain"))
            {
              synchronized (getInput("iRelease"))
              {
                synchronized (getOutput("oFrequency"))
                {
                  getInput("iExternalGate").getValues().clear();
                  getInput("iGate").getValues().clear();
                  getInput("iAttack").getValues().clear();
                  getInput("iDecay").getValues().clear();
                  getInput("iSustain").getValues().clear();
                  getInput("iRelease").getValues().clear();
                  getOutput("oFrequency").getValues().clear();
                  
                  double out = 0;
                  
                  double exGate = getInput("iExternalGate").getValues().getDouble();
                  double gate = getInput("iGate").getValues().getDouble();
                  boolean actived = ( exGate>0.5 || gate>0.5);
                  
                  attackInSimple = frameRate_ * getInput("iAttack").getValues().getDouble();
                  decayInSimple = frameRate_ * getInput("iDecay").getValues().getDouble();
                  releaseInSimple = frameRate_ * getInput("iRelease").getValues().getDouble();
                  sustainLevel = getInput("iSustain").getValues().getDouble()/100;
                  
                  
                  if(actived){
                    switch(currentState){
                      case IDLE:
                      case RELASE:
                        currentState = State.ATTACK;
                        currentSimple = 0.0;
                        break;                       
                        default:
                          break;  
                    }                    
                  }else{
                    switch(currentState){
                      case ATTACK:
                      case DECAY:
                      case SUSTAIN:
                        currentState = State.RELASE;
                        currentSimple = 0.0;
                        break;                        
                       default:
                         break;                      
                      }
                    }               
  
                  for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
                  {
                    switch(currentState){
                      case IDLE:
                        out = -1.0;
                        break;
                      case ATTACK:
                        out = 2 * currentSimple / attackInSimple - 1.0;
                        currentSimple =currentSimple + 1.0;
                        if(currentSimple >= attackInSimple)
                        {
                          currentState = State.DECAY;
                          currentSimple = 0.0;
                        }
                        break;
                      case DECAY:
                        out = currentSimple * (2.0 *  (sustainLevel) - 2.0)/decayInSimple + 1.0;
                        currentSimple = currentSimple + 1.0;
                        if(currentSimple >= decayInSimple)
                        {
                          currentState = State.SUSTAIN;
                          currentSimple = 0.0;
                        }
                        break;
                      case SUSTAIN:
                        out = 2.0 * (sustainLevel) - 1.0;
                        break;
                      case RELASE:
                        out = currentSimple * -2.0 *  (sustainLevel) / releaseInSimple + 2.0 *  (sustainLevel) - 1.0;
                        currentSimple = currentSimple + 1.0;
                        if(currentSimple >= releaseInSimple)
                        {
                          currentState = State.IDLE;
                          currentSimple = 0.0;
                        }
                        break;
                      default:
                        out = -1.0;
                        break;
                    }
                    getOutput("oFrequency").getValues().putDouble(out);
                  }
                }
              }
            }
          }
        }
      }
    } 
  }
  
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleEnvelope();
  }
}
