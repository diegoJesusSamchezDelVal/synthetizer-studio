package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleSequencer extends BasicModule
{
  public ModuleSequencer()
  {
    super("Sequencer");

    addInput(new BasicPort("iTempo", 15, Port.ValueType.DISCRETE,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(1, 100),
        "Octave modulation input following the 1v/octave convention"));

    addOutput(new BasicPort("oOctave", 3, Port.ValueType.DISCRETE,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 7, 7),
        "Octave modulation input following the 1v/octave convention"));
    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 8),
        "Output note voltage following the 1v/octave convention"));
    addOutput(new BasicPort("oActive", 0, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1),
        "Active control signal"));

    sampleCount_ = 0;
  }

  @Override
  public void compute()
  {

    //double[] melodyNescafe = { 17, 17, 17, 13, 15, 15, 15, 18, 17, 17, 17, 13,
    //    15, 15, 15, 18, 17, 17, 17, 13, 15, 15, 12, 8, 10, 10, 5, 17, 15, 13,
    //    12 };
    double[] melodyMorricone = { 9, 14, 9, 14, 9, 9, 9, 9, 9, 9, 9, 9, 5, 5, 5,
        5, 7, 7, 7, 7, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 14,
        9, 14, 9, 9, 9, 9, 9, 9, 9, 9, 5, 5, 5, 5, 7, 7, 7, 7, 12, 12, 12, 12,
        12, 12, 12, 12, 12, 12, 12, 12, 9, 14, 9, 14, 14, 14, 14, 14, 14, 14,
        14, 14, 5, 5, 5, 5, 4, 4, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9,
        14, 9, 14, 9, 9, 9, 9, 9, 9, 7, 7, 7, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        2, 2, 2, 2, 2, 2, 2, 2 };
    synchronized (getInput("iTempo"))
    {
      synchronized (getOutput("oActive"))
      {
        synchronized (getOutput("oOctave"))
        {
          synchronized (getOutput("oSignal"))
          {
            double note = 0;
            for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
            {
              ++sampleCount_;
              sampleCount_ %= 44100 * getInput("iTempo").getValues().getDouble(
                  i * (Double.SIZE / 8));
              getOutput("oOctave").getValues().putDouble(i * (Double.SIZE / 8),
                  3.0);
              note = melodyMorricone[(int) Math
                  .floor((sampleCount_ / (44100. * getInput("iTempo")
                      .getValues().getDouble(i * (Double.SIZE / 8))))
                      * (double) melodyMorricone.length)];
              getOutput("oSignal").getValues().putDouble(i * (Double.SIZE / 8),
                  note);

            }
            if (previousNote_ != note)
            {
              getOutput("oActive").setValues(0);
            }
            else
              getOutput("oActive").setValues(1);
            previousNote_ = note;
          }
        }
      }
    }
  }

  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleSequencer();
  }

  private int    sampleCount_;

  private double previousNote_;
}
