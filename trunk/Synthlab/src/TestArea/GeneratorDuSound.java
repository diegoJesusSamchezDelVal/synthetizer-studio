package TestArea;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GeneratorDuSound{

  private SourceDataLine      line; 
  private AudioFormat         baseFormat;
  
  private SourceDataLine getLine(AudioFormat audioFormat) {
     SourceDataLine res = null;
     DataLine.Info info = new DataLine.Info(SourceDataLine.class,audioFormat);
     try {
         res = (SourceDataLine) AudioSystem.getLine(info);
         res.open(audioFormat);
     }catch (Exception e) {
         e.printStackTrace();
     }
     return res;
  }
  
  private void play()throws UnsupportedAudioFileException, LineUnavailableException, IOException{
    
    baseFormat = new AudioFormat(44100.0f, 16, 1, true, false);
    line = getLine(baseFormat);
    
    line.start();
    
    byte[] buffer = new byte[44100*2];
    final int maxVolume =(int) Math.pow(2.0, 15.0);
    final double frequency = 3000.0;
    final double periodePerSample = frequency / 44100.0;
    double currentPositionInPeriode = 0.0;
    for(int sample=0; sample<44100; sample++) {
      int value = (int)(maxVolume * Math.sin(currentPositionInPeriode * 2.0 * Math.PI) - 1);
      currentPositionInPeriode += periodePerSample;
      if(currentPositionInPeriode >= 1.0)
        currentPositionInPeriode -= 1.0;
      buffer[sample*2] = (byte)(value & 0xFF);
      buffer[sample*2 + 1] = (byte)((value & 0xFF00) >> 8);
    }
    
      line.write(buffer, 0, 44100*2);

      line.stop();
  }
  public static void main(String args[])throws UnsupportedAudioFileException, LineUnavailableException, IOException
  {
    GeneratorDuSound g=new GeneratorDuSound();
      g.play();
    //System.out.println(g.toString());
  }

}
