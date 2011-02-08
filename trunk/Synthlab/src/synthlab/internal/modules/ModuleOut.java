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
  }

  @Override
  public void compute()
  {
    getInput("iSignal").getValues().clear();
    
    ByteBuffer data = ByteBuffer.allocate(Scheduler.SamplingBufferSize*2);
    for ( int i = 0; i<Scheduler.SamplingBufferSize; ++ i )
      data.putShort( i*2,(short) (getInput("iSignal").getValues().getDouble(i*8) * Short.MAX_VALUE) );
    
    Audio.getLine().write( data.array(), 0, Scheduler.SamplingBufferSize*2);
    
    getInput("iSignal").getValues().clear();
  }
  
  AudioInputStream    stream_;
  DataLine.Info       lineInfo_;
}