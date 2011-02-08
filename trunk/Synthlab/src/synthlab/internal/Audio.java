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
      format_ = new AudioFormat(Encoding.PCM_SIGNED, 44100, Short.SIZE, 1, 2,
          44100, true);

      try
      {
        line_ = AudioSystem.getSourceDataLine(format_);
      }
      catch (LineUnavailableException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return line_;
  }

  public static void openLine()
  {
    getLine();
    try
    {
      line_.open(format_);
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
    }
  }

  public static void closeLine()
  {
    getLine();
    line_.close();
  }
  
  public static void startLine()
  {
    getLine().start();
  }
  
  public static void stopLine()
  {
    getLine().stop();
  }

  private static SourceDataLine line_   = null;
  private static AudioFormat    format_ = null;
}
