package synthlab.internal.modules;

import java.awt.image.VolatileImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.crypto.spec.IvParameterSpec;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleOut extends BasicModule
{
  public ModuleOut()
  {
    super("Out");
    
    sampleCompteur  = 0;
    addInput(new BasicPort("iSignal", 0));
    
    baseFormat =  new AudioFormat(Encoding.PCM_SIGNED, 44100, Short.SIZE, 1, 2, 44100, true);
    
    data = ByteBuffer.allocate(2*44100);
  }

  @Override
  public void compute()
  {
    double volIn = getInput("iSignal").getValue();
    
    volIn = Math.max(-1, volIn);
    volIn = Math.min( 1, volIn);
    
    sampleCompteur++;
    data.putShort((short) (volIn*Short.MAX_VALUE));
    stream = new AudioInputStream(new ByteArrayInputStream(data.array()),  baseFormat, 44100*2);
    try
    {
      if (sampleCompteur == 44099)
      {
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        clip.start();
        clip.drain();
        data.clear();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  int                 sampleCompteur;
  private AudioFormat baseFormat;
  private ByteBuffer  data;
  AudioInputStream    stream;
}
