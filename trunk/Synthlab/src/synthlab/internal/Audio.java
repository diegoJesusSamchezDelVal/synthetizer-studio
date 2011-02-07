package synthlab.internal;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

public class Audio
{
  public static SourceDataLine getLine()
  {
    if (line_ == null)
    {
      AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED, 44100,
          Short.SIZE, 1, 2, 44100, true);
      try
      {
        line_ = AudioSystem.getSourceDataLine(format);
        line_.open(format);
      }
      catch (LineUnavailableException e)
      {
        e.printStackTrace();
      }
    }
    
    return line_;
  }

  private static SourceDataLine line_ = null;
}
