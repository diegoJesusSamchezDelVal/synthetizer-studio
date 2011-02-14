package synthlab.internal;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

/**
 * This is a singleton responsible of allocating and storing an audio data line
 * of the correct format used by the library.
 */
public class Audio
{
  /**
   * This will return an audio data line singleton using the appropriate format.
   */
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
        e.printStackTrace();
      }
    }

    return line_;
  }

  /**
   * This will open the default audio data line for writing.
   */
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

  /**
   * This will close the default audio data line.
   */
  public static void closeLine()
  {
    getLine();
    line_.close();
  }
  
  /**
   * This will start playing the audio buffer written in the default data line.
   */
  public static void startLine()
  {
    getLine().start();
  }
  
  /**
   * This will stop playing the audio buffer written in the default data line.
   */
  public static void stopLine()
  {
    getLine().stop();
  }

  /**
   * The default data line singleton instance
   */
  private static SourceDataLine line_   = null;
  
  /**
   * The default format of the audio data line
   */
  private static AudioFormat    format_ = null;
}
