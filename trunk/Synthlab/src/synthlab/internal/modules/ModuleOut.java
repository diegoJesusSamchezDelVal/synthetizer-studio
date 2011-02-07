package synthlab.internal.modules;

import java.io.*;
import java.nio.*;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.*;

import synthlab.api.Scheduler;
import synthlab.internal.*;

public class ModuleOut extends BasicModule
{
  public ModuleOut()
  {
    super("Out");
    
    addInput(new BasicPort("iSignal", 0));
    
    sampleCounter_  = 0;
    baseFormat_ =  new AudioFormat(Encoding.PCM_SIGNED, 44100, Short.SIZE, 1, 2, 44100, true);
    data_ = ByteBuffer.allocate(Scheduler.SamplingBufferSize*2);
    lineInfo_ = new DataLine.Info(SourceDataLine.class, baseFormat_);
    
    try
    {
      line_ = AudioSystem.getSourceDataLine(baseFormat_);
      line_.open(baseFormat_);
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void compute()
  {
    // Increment sample counter, clamp in bounds [0;44100]
    sampleCounter_ += Scheduler.SamplingBufferSize;
    
    // Add sample to data stream
    for ( int i=0; i<Scheduler.SamplingBufferSize; ++i)
      data_.putShort(i*2, (short) (getInput("iSignal").getValues().getDouble()*Short.MAX_VALUE));
    
    stream_ = new AudioInputStream(new ByteArrayInputStream(data_.array()),  baseFormat_, Scheduler.SamplingBufferSize*2);
    
    line_.start();
    line_.write(data_.array(), 0, Scheduler.SamplingBufferSize*2);
    
    data_.clear();
  }
  
  protected void finalize()
  {
    line_.stop();
    line_.close();
  }

  int                 sampleCounter_;
  private AudioFormat baseFormat_;
  private ByteBuffer  data_;
  AudioInputStream    stream_;
  SourceDataLine      line_;
  DataLine.Info       lineInfo_;
}
