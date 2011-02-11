package synthlab.internal.modules;

import javax.sql.rowset.spi.SyncFactory;

import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleEnvelope extends BasicModule
{

  private enum State {IDLE,ATTACK,DECAY,SUSTAIN,RELASE}
  private double attackInSimple,decayInSimple,relaseInSimple;
  private double sustainLevel;
  
  private double currentSimple;
  
  private State currentState;
  
  //private final int frameRate_ = 44100;
  
  public ModuleEnvelope()
  {
    super("ENV");
    
    //add input port
    addInput(new BasicPort("iExternalGate", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), name_));
    addInput(new BasicPort("iGate", 0, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), name_));
    addInput(new BasicPort("iAttack", 0.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,10000), name_));
    addInput(new BasicPort("iDecay", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,10000), name_));
    addInput(new BasicPort("iSustain", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 1), name_));
    addInput(new BasicPort("iRelase", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0,10000), name_));
    
    //add output port
    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1), name_));
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
              synchronized (getInput("iRelase"))
              {
                synchronized (getOutput("oSignal"))
                {
                  getInput("iExternalGate").getValues().clear();
                  getInput("iGate").getValues().clear();
                  getInput("iAttack").getValues().clear();
                  getInput("iDecay").getValues().clear();
                  getInput("iSustain").getValues().clear();
                  getInput("iRelase").getValues().clear();
                  getOutput("oSignal").getValues().clear();
                  
                  double out = 0;
                  
                  double exGate = getInput("iExternalGate").getValues().getDouble();
                  double gate = getInput("iGate").getValues().getDouble();
                  boolean actived = ( exGate>0.5 || gate>0.5);
                  
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
                        out = currentSimple * (2.0 * sustainLevel - 2.0)/decayInSimple + 1.0;
                        currentSimple = currentSimple + 1.0;
                        if(currentSimple >= decayInSimple)
                        {
                          currentState = State.SUSTAIN;
                          currentSimple = 0.0;
                        }
                        break;
                      case SUSTAIN:
                        out = 2.0 * sustainLevel - 1.0;
                        break;
                      case RELASE:
                        out = currentSimple * -2.0 * sustainLevel / relaseInSimple + 2.0 * sustainLevel - 1.0;
                        currentSimple = currentSimple + 1.0;
                        if(currentSimple >= relaseInSimple)
                        {
                          currentState = State.IDLE;
                          currentSimple = 0.0;
                        }
                        break;
                      default:
                        out = -1.0;
                        break;
                    }
                  }
                  getOutput("oSignal").getValues().putDouble(out);
                }
                
              }
            }
          }
        }
      }
    }
    
  }

}
