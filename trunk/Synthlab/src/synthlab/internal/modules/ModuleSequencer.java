package synthlab.internal.modules;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;


/**
 * Concrete module class of Sequencer 
 * Product a song!
 * @author
 * */
public class ModuleSequencer extends BasicModule
{
  private int    sampleCount_;
  private double previousNote_;
  
  /**
   * Constructor of Sequencer initial all ports E/S, initial name of module
   * */
  public ModuleSequencer()
  {
    super("Sequencer");

    addInput(new BasicPort("iTempo", 50, Port.ValueType.DISCRETE,
        Port.ValueUnit.MILLISECONDS, new Port.ValueRange(1, 100),
        "Octave modulation input following the 1v/octave convention"));

    addOutput(new BasicPort("oSignal", 0, Port.ValueType.CONTINUOUS,
        Port.ValueUnit.VOLT, new Port.ValueRange(0, 8),
        "Output note voltage following the 1v/octave convention"));
    addOutput(new BasicPort("oActive", 0, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(0, 1),
        "Active control signal"));

    sampleCount_ = 0;
  }

  
  /**
   * method compute
   * */
  @Override
  public void compute()
  {

    // double[] melodyNescafe = { 17, 17, 17, 13, 15, 15, 15, 18, 17, 17, 17,
    // 13,
    // 15, 15, 15, 18, 17, 17, 17, 13, 15, 15, 12, 8, 10, 10, 5, 17, 15, 13,
    // 12 };
    // double[] melodyMorricone = { 9, 14, 9, 14, 9, 9, 9, 9, 9, 9, 9, 9, 5, 5,
    // 5,
    // 5, 7, 7, 7, 7, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 14,
    // 9, 14, 9, 9, 9, 9, 9, 9, 9, 9, 5, 5, 5, 5, 7, 7, 7, 7, 12, 12, 12, 12,
    // 12, 12, 12, 12, 12, 12, 12, 12, 9, 14, 9, 14, 14, 14, 14, 14, 14, 14,
    // 14, 14, 5, 5, 5, 5, 4, 4, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9,
    // 14, 9, 14, 9, 9, 9, 9, 9, 9, 7, 7, 7, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
    // 2, 2, 2, 2, 2, 2, 2, 2 };
    double[] melodyMuse = { 12, 15, 19, 24, 27, 31, 36, 39, 43, 39, 36, 31, 27,
        24, 19, 15, 10, 14, 17, 22, 26, 29, 34, 38, 41, 38, 34, 29, 26, 22, 17,
        14, 5, 8, 12, 17, 20, 24, 29, 32, 36, 32, 29, 24, 20, 17, 12, 8 };
    synchronized (getInput("iTempo"))
    {
      synchronized (getOutput("oActive"))
      {
        synchronized (getOutput("oSignal"))
        {
          double note = 0;
          for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
          {
            ++sampleCount_;
            sampleCount_ %= 44100 * getInput("iTempo").getValues().getDouble(
                i * (Double.SIZE / 8));
            note = melodyMuse[(int) Math
                .floor((sampleCount_ / (44100. * getInput("iTempo").getValues()
                    .getDouble(i * (Double.SIZE / 8))))
                    * (double) melodyMuse.length)];
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
  
  
  /**
   * Method for create a new module of Sequencer
   * */

  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleSequencer();
  }
}
