package synthlab.internal.modules;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.*;
import javax.sound.sampled.*;

import synthlab.api.*;
import synthlab.api.Port;
import synthlab.internal.*;

public class ModuleOutToFile extends BasicModule
{
  
  private static byte[] RIFF="RIFF".getBytes();  
  private static byte[] RIFF_SIZE=new byte[4];  
  private static byte[] RIFF_TYPE="WAVE".getBytes();  
    
    
  private static byte[] FORMAT="fmt ".getBytes();  
  private static byte[] FORMAT_SIZE=new byte[4];  
  private static byte[] FORMATTAG=new byte[2];  
  private static byte[] CHANNELS=new byte[2];  
  private static byte[] SamplesPerSec =new byte[4];  
  private static byte[] AvgBytesPerSec=new byte[4];  
  private static byte[] BlockAlign =new byte[2];  
  private static byte[] BitsPerSample =new byte[2];  
    
  private static byte[] DataChunkID="data".getBytes();  
  private static byte[] DataSize=new byte[4];  
  public static boolean isrecording=false;  
  
  AudioInputStream stream_;
  DataLine.Info    lineInfo_;
  ByteBuffer       data_;
  
  public ModuleOutToFile()
  {
    super("Out");

    addInput(new BasicPort("iSignal", 0, Port.ValueType.INCONFIGURABLE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Input sound waveto be sent to the sound card"));
    
    
    addInput(new BasicPort("Recording", 0, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),"Record stream data to a wave file"));
    
    data_ = ByteBuffer.allocate(Scheduler.SamplingBufferSize * 2);
    
    
         
  }

   public static void init()
   {  
     FORMAT_SIZE=new byte[]{(byte)16,(byte)0,(byte)0,(byte)0};  
     byte[] tmp=revers(intToBytes(1));  
     FORMATTAG=new byte[]{tmp[0],tmp[1]};  
     CHANNELS=new byte[]{tmp[0],tmp[1]};  
     SamplesPerSec=revers(intToBytes(16000));  
     AvgBytesPerSec=revers(intToBytes(32000));  
     tmp=revers(intToBytes(2));  
     BlockAlign=new byte[]{tmp[0],tmp[1]};  
     tmp=revers(intToBytes(16));  
     BitsPerSample=new byte[]{tmp[0],tmp[1]};  
   }
   
   public static byte[] revers(byte[] tmp)
   {  
     byte[] reversed=new byte[tmp.length];  
     for(int i=0;i<tmp.length;i++){  
       reversed[i]=tmp[tmp.length-i-1];                               
     }  
     return reversed;  
    }  
   
    public static byte[] intToBytes(int num)
    {  
      byte[]  bytes=new byte[4];  
      bytes[0]=(byte)(num>>24);  
      bytes[1]=(byte)((num>>16)& 0x000000FF);  
      bytes[2]=(byte)((num>>8)& 0x000000FF);  
      bytes[3]=(byte)(num & 0x000000FF);  
      return bytes;         
    }  
  
  @SuppressWarnings("null")
  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      synchronized (getInput("Recording"))
      {
        getInput("iSignal").getValues().clear();
        getInput("Recording").getValues().clear();

        double recording = getInput("Recording").getValues().getDouble();
        
        if(recording>=0){
          isrecording = true;
        }else{
          isrecording = false;
        }
        
        for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
        {
          data_.putShort(i * 2, (short) (getInput("iSignal").getValues()
              .getDouble(i * 8) * Short.MAX_VALUE));
        }
  
        
        //TODO
         OutputStream output = new ByteArrayOutputStream();
         try
        {
          output.write(data_.array());
        }
        catch (IOException e1)
        {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
         
   
         
             int toaldatasize=0;  
             int audiolen=Scheduler.SamplingBufferSize;  
             byte[] audiochunk=new byte[1024];  
             ByteArrayOutputStream bytebuff=new ByteArrayOutputStream(9600000);    
             while(isrecording){  
                 
                 toaldatasize+=audiolen;  
                  bytebuff.write(audiochunk, 0, audiolen);  
             }  
               
             DataSize=revers(intToBytes(toaldatasize));  
             RIFF_SIZE=revers(intToBytes(toaldatasize+36-8));  
             File wavfile= new File("outWav.wav");  
             FileOutputStream file=null;  
               
              try {  
                   file=new FileOutputStream(wavfile);  
                   BufferedOutputStream fw=new BufferedOutputStream(file);  
                   init();  
                
             fw.write(RIFF);  
             fw.write(RIFF_SIZE);  
             fw.write(RIFF_TYPE);  
             fw.write(FORMAT);  
             fw.write(FORMAT_SIZE);  
             fw.write(FORMATTAG);  
             fw.write(CHANNELS);  
             fw.write(SamplesPerSec);  
             fw.write(AvgBytesPerSec);  
             fw.write(BlockAlign);  
             fw.write(BitsPerSample);  
               
             fw.write(DataChunkID);  
             fw.write(DataSize);  
             fw.write(bytebuff.toByteArray());  
             fw.flush();  
              } catch (IOException e) {  
                     // TODO Auto-generated catch block  
                     e.printStackTrace();  
                 }  
        
        Audio.getLine().write(data_.array(), 0, Scheduler.SamplingBufferSize * (Short.SIZE/8));
  
        getInput("iSignal").getValues().clear();
      }
    }
  }
  


  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleOutToFile();
  }
}
