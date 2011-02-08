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
    addInput(new BasicPort("iConstant", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 8)));
    addInput(new BasicPort("iShape", 0, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1, 4)));
    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1)));

    frameCount_ = 0;
    frameRate_ = 44100;
    initialFrequency_ = 440; // Octave:0, Note:Do
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

              double positionInPeriod = (double) frameCount_
                  / (double) frameRate_;
              double frequency = Math.pow(2, ifreq + iconst)
                  * initialFrequency_;

              double framePerPeriod_ = (double) frameRate_ / frequency;
              double currentPositionInPercent = frameCount_ / framePerPeriod_;

              if (ishape <= SHAPE_SQUARE)
              {
                out = (Math.sin(positionInPeriod * frequency * 2. * Math.PI)) >= 0 ? 1.
                    : -1;
              }
              else if (ishape <= SHAPE_SINE)
              {
                out = Math.sin(positionInPeriod * frequency * 2. * Math.PI);
              }
              else if (ishape <= SHAPE_TRIANGLE)
              {
                if (currentPositionInPercent < 0.25)
                  out = 4.0 * currentPositionInPercent;
                else if (currentPositionInPercent < 0.75)
                  out = 2.0 - 4.0 * currentPositionInPercent;
                else
                  out = 4.0 * currentPositionInPercent - 4.0;
              }
              else if (ishape <= SHAPE_SAWTOOTH)
              {
                out = 2.0 * frameCount_ / framePerPeriod_ - 1.0;
              }
              else
              {
                assert (false);
              }

              getOutput("oSignal").getValues().putDouble(out);
              frameCount_ = ++frameCount_ % 44100;
            }
          }
        }
      }
    }
  }

  public static final double SHAPE_SQUARE   = -0.5;
  public static final double SHAPE_SINE     = -0.0;
  public static final double SHAPE_TRIANGLE = +0.5;
  public static final double SHAPE_SAWTOOTH = +1.0;

  private int                frameCount_;
  private int                frameRate_;
  private double             initialFrequency_;
}
