package synthlab.internal.modules;

import java.io.*;
import java.nio.*;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.*;

import synthlab.internal.*;

public class ModuleOut extends BasicModule
{
  public ModuleOut()
  {
    super("Out");
    
    addInput(new BasicPort("iSignal", 0));
    
    sampleCounter_  = 0;
    baseFormat_ =  new AudioFormat(Encoding.PCM_SIGNED, 44100, Short.SIZE, 1, 2, 44100, true);
    data_ = ByteBuffer.allocate(2*44100);
    lineInfo_ = new DataLine.Info(SourceDataLine.class, baseFormat_);
    
    try
    {
      line_ = AudioSystem.getSourceDataLine(baseFormat_);
      line_.open(baseFormat_);
    }
    catch (LineUnavailableException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void compute()
  {
    double volIn = getInput("iSignal").getValue();
    
    // Sanity check: clamp input signal in bounds [-1;+1]
    volIn = Math.max(-1, volIn);
    volIn = Math.min( 1, volIn);
    
    // Increment sample counter, clamp in bounds [0;44100]
    sampleCounter_ = ++sampleCounter_ % 44100;
    
    // Add sample to data stream
    data_.putShort(sampleCounter_*2, (short) (volIn*Short.MAX_VALUE));
    
    stream_ = new AudioInputStream(new ByteArrayInputStream(data_.array()),  baseFormat_, 44100*2);
    
    try
    {
      // time to send data to audio card
      if (sampleCounter_ == 44099)
      {
        line_.start();
        line_.write(data_.array(), 0, 44100*2);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
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
