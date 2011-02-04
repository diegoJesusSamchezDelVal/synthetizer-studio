package TestArea;


import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GestionDeSound{
    //private String file_path="source/homersimpson.wav";
    private String file_path="source/Audio.wav";
    //private String file_path="source/garfield.wav"; 
    private AudioInputStream    ais; 
    private SourceDataLine      line; 
    private AudioFormat         baseFormat;
    private static final int    BUFFER_SIZE=4000*4;
    
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
        ais= AudioSystem.getAudioInputStream(new File(file_path));
        baseFormat = ais.getFormat();
        //System.out.println(ais.toString());
        line = getLine(baseFormat);
        line.start();
        int inBytes = 0;
        byte[] audioData=new byte[BUFFER_SIZE];    
        while (inBytes != -1){
            inBytes = ais.read(audioData, 0, BUFFER_SIZE);
            if (inBytes >= 0) {
               int outBytes = line.write(audioData, 0, inBytes);
            }
        }
    }
    public static void main(String args[])throws UnsupportedAudioFileException, LineUnavailableException, IOException
    {
      GestionDeSound g=new GestionDeSound();
        g.play();
      //System.out.println(g.toString());
    }

}