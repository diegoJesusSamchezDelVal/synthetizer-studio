package synthlab.internal.modules;

import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleVCO extends BasicModule
{
  public ModuleVCO()
  {
    super("VCO");

    addInput(new BasicPort("iFrequency", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 8)));

    addInput(new BasicPort("iConstant", 4, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 8)));

    addInput(new BasicPort("iShape", SHAPE_SINE, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1, 4)));

    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));

  //  frameCount_ = 0;
    frameRate_ = 44100;
    initialFrequency_ = 32.7; // Octave:0, Note:Do , with Hz
    
    currentPositionInPeriod_ = 0.0;
  }

  @Override
  public void compute()
  {
    synchronized (getInput("iShape"))
    {
      synchronized (getInput("iFrequency"))
      {
        synchronized (getInput("iConstant"))
        {
          synchronized (getOutput("oSignal"))
          {
            getInput("iShape").getValues().clear();
            getInput("iFrequency").getValues().clear();
            getInput("iConstant").getValues().clear();
            getOutput("oSignal").getValues().clear();

            
            for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
            {
              double out = 0;
              double ishape = getInput("iShape").getValues().getDouble();
              double ifreq = getInput("iFrequency").getValues().getDouble();
              double iconst = getInput("iConstant").getValues().getDouble();

             // double positionInPeriod = (double) frameCount_
               //   / (double) frameRate_;
              double frequency = Math.pow(2, ifreq + iconst)
                  * initialFrequency_;

              double framePerPeriod_ = (double) frameRate_ / frequency;
              double currentPositionInPercent = currentPositionInPeriod_/framePerPeriod_;

              if (ishape >= SHAPE_SAWTOOTH)
              {
                out = 2*currentPositionInPeriod_ / framePerPeriod_ - 1.0;
              }
              else if (ishape >= SHAPE_TRIANGLE && ishape < SHAPE_SAWTOOTH)
              {
                if (currentPositionInPercent < 0.25)
                  out = 4.0 * currentPositionInPercent;
                else if (currentPositionInPercent < 0.75)
                  out = 2.0 - 4.0 * currentPositionInPercent;
                else
                  out = 4.0 * currentPositionInPercent - 4.0;
              }
              else if (ishape >= SHAPE_SINE && ishape < SHAPE_TRIANGLE)
              {
                out = Math.sin(currentPositionInPercent * 2. * Math.PI);
              }
              else if (ishape >= SHAPE_PULSE && ishape < SHAPE_SINE)
              {
                out = currentPositionInPercent > 0.5 ? 1.0 : -1.0;
              }
              else
             {
                assert (false);
              }
              getOutput("oSignal").getValues().putDouble(out);
              
              currentPositionInPeriod_ +=  1.0 ;
              if(currentPositionInPeriod_ >= framePerPeriod_)
                currentPositionInPeriod_ -= framePerPeriod_;
            }
          }
        }
      }
    }
  }

  public static final double SHAPE_SAWTOOTH = 0.75;
  public static final double SHAPE_TRIANGLE = 0.50;
  public static final double SHAPE_SINE     = 0.25;
  public static final double SHAPE_PULSE    = 0.00;

 // private int                frameCount_;
  private int                frameRate_;
  private double             initialFrequency_;
  private double       currentPositionInPeriod_;
}
