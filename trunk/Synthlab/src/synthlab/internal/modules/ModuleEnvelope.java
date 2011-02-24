package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;


/**
 * Concrete module class of ADSR
 * Parameters are pass by UI, A,D,R in MilleSconde, S in percentage
 * Parameters Gate is for manually, ExtermGate for a external control signal
 * @author Dayou
 * */

public class ModuleEnvelope extends BasicModule
{
  private enum State
  {
    IDLE, ATTACK, DECAY, SUSTAIN, RELASE
  }

  private final int FRAME_RATE   = 44100;
  private double       attackInSimple, decayInSimple, relaseInSimple;
  private double       sustainLevel;

  private double       currentSimple;

  private State        currentState = State.IDLE;

  
  /**
   * Constructor of ADSR initial all ports E/S, initial name of module
   * */
  public ModuleEnvelope()
  {
    super("Envelope");

    // add input port
    addInput(new BasicPort("iExternalGate", 0, Port.ValueType.INCONFIGURABLE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),
        "Extern controle for the state of Gate"));
    addInput(new BasicPort("iGate", 0, Port.ValueType.SWITCH,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),
        "Mannuly switch for Gate"));
    addInput(new BasicPort("iAttack", 1000.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0, 2000),
        "Attack time controlor"));
    addInput(new BasicPort("iDecay", 1000.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0, 2000),
        "Decay time controlor"));
    addInput(new BasicPort("iSustain", 50.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.PERCENTAGE, new Port.ValueRange(0, 100),
        "Sustain percentage controlor"));
    addInput(new BasicPort("iRelease", 1000.0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(0, 2000),
        "Relase time controlor"));

    // add output port
    addOutput(new BasicPort("oFrequency", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),
        "Frequence output"));
  }

  /**
   * compute method 
   * */
  
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

                  attackInSimple = getInput("iAttack").getValues().getDouble()
                      * FRAME_RATE / 1000.0;
                  
                  decayInSimple = getInput("iDecay").getValues().getDouble()
                      * FRAME_RATE / 1000.0;
                  
                  sustainLevel = getInput("iSustain").getValues().getDouble();
                  
                  relaseInSimple = getInput("iRelease").getValues().getDouble()
                      * FRAME_RATE / 1000.0;

                  double out = 0;

                  final double exGate = getInput("iExternalGate").getValues()
                      .getDouble();
                  final double gate = getInput("iGate").getValues().getDouble();
                  final boolean actived = (exGate > 0.5 || gate > 0.5);

                  if (actived)
                  {
                    switch (currentState)
                    {
                      case IDLE:
                      case RELASE:
                        currentState = State.ATTACK;
                        currentSimple = 0.0;
                        break;
                      default:
                        break;
                    }
                  }
                  else
                  {
                    switch (currentState)
                    {
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
                    switch (currentState)
                    {
                      case IDLE:
                        out = -1.0;
                        break;
                      case ATTACK:
                        out = 2 * currentSimple / attackInSimple - 1.0;
                        currentSimple = currentSimple + 1.0;
                        if (currentSimple >= attackInSimple)
                        {
                          currentState = State.DECAY;
                          currentSimple = 0.0;
                        }
                        break;
                      case DECAY:
                        out = currentSimple
                            * (2.0 * (sustainLevel / 100) - 2.0)
                            / decayInSimple + 1.0;
                        currentSimple = currentSimple + 1.0;
                        if (currentSimple >= decayInSimple)
                        {
                          currentState = State.SUSTAIN;
                          currentSimple = 0.0;
                        }
                        break;
                      case SUSTAIN:
                        out = 2.0 * (sustainLevel / 100) - 1.0;
                        break;
                      case RELASE:
                        out = currentSimple * -2.0 * (sustainLevel / 100)
                            / relaseInSimple + 2.0 * (sustainLevel / 100) - 1.0;
                        currentSimple = currentSimple + 1.0;
                        if (currentSimple >= relaseInSimple)
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

  /**
   * Method for create a new module of ADSR
   * */
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleEnvelope();
  }
}
