package synthlab.internal.modules;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.Audio;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;

public class ModuleOutToFile extends BasicModule
{

  // private static byte[] RIFF="RIFF".getBytes();
  // private static byte[] RIFF_SIZE=new byte[4];
  // private static byte[] RIFF_TYPE="WAVE".getBytes();
  //
  //
  // private static byte[] FORMAT="fmt ".getBytes();
  // private static byte[] FORMAT_SIZE=new byte[4];
  // private static byte[] FORMATTAG=new byte[2];
  // private static byte[] CHANNELS=new byte[2];
  // private static byte[] SamplesPerSec =new byte[4];
  // private static byte[] AvgBytesPerSec=new byte[4];
  // private static byte[] BlockAlign =new byte[2];
  // private static byte[] BitsPerSample =new byte[2];
  //
  // private static byte[] DataChunkID="data".getBytes();
  // private static byte[] DataSize=new byte[4];

  private static Integer   fileCounter = 0;

  private boolean          isRecording = false;
  private boolean          toRecord    = false;
  private File             file        = null;
  private FileOutputStream fos         = null;

  ByteBuffer               data_, data_l;

  public ModuleOutToFile()
  {
    super("Out");

    addInput(new BasicPort("iSignal", 0, Port.ValueType.INCONFIGURABLE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),
        "Input sound wave to be sent to the sound card"));

    addInput(new BasicPort("Recording", -1, Port.ValueType.DISCRETE,
        Port.ValueUnit.AMPLITUDE, new Port.ValueRange(-1, 1),
        "Record stream data to a wave file"));

    data_ = ByteBuffer.allocate(Scheduler.SamplingBufferSize * 2);
    data_l = ByteBuffer.allocate(Scheduler.SamplingBufferSize * 2);

  }

  // public static void init()
  // {
  // FORMAT_SIZE=new byte[]{(byte)16,(byte)0,(byte)0,(byte)0};
  // byte[] tmp=revers(intToBytes(1));
  // FORMATTAG=new byte[]{tmp[0],tmp[1]};
  // CHANNELS=new byte[]{tmp[0],tmp[1]};
  // SamplesPerSec=revers(intToBytes(16000));
  // AvgBytesPerSec=revers(intToBytes(32000));
  // tmp=revers(intToBytes(2));
  // BlockAlign=new byte[]{tmp[0],tmp[1]};
  // tmp=revers(intToBytes(16));
  // BitsPerSample=new byte[]{tmp[0],tmp[1]};
  // }

  // public static byte[] revers(byte[] tmp)
  // {
  // byte[] reversed=new byte[tmp.length];
  // for(int i=0;i<tmp.length;i++){
  // reversed[i]=tmp[tmp.length-i-1];
  // }
  // return reversed;
  // }

  public static byte[] longToBytes(final long num)
  {
    final byte[] bytes = new byte[4];
    bytes[3] = (byte) ((num >> 24) & 0x000000FF);
    bytes[2] = (byte) ((num >> 16) & 0x000000FF);
    bytes[1] = (byte) ((num >> 8) & 0x000000FF);
    bytes[0] = (byte) (num & 0x000000FF);
    return bytes;
  }

  @Override
  public void compute()
  {
    synchronized (getInput("iSignal"))
    {
      synchronized (getInput("Recording"))
      {
        getInput("iSignal").getValues().clear();
        getInput("Recording").getValues().clear();

        for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
        {
          data_.putShort(i * 2, (short) (getInput("iSignal").getValues()
              .getDouble(i * 8) * Short.MAX_VALUE));
          data_l.put(i * 2, data_.get(i * 2 + 1));
          data_l.put(i * 2 + 1, data_.get(i * 2));
        }

        toRecord = getInput("Recording").getValues().getDouble() > 0;

        if (toRecord)
        {
          if (!isRecording)
          { // Demmarage d'enregistrement
            // Creer un nouveau fichier audio a ecrire
            synchronized (fileCounter)
            {
              file = new File("Output" + fileCounter++ + ".wav");
              while (file != null && file.exists())
              {
                file = new File("Output" + fileCounter++ + ".wav");
              }
            }
            // Creer l'entete de fichier audio
            try
            {
              fos = new FileOutputStream(file);
              final AudioFormat format = new AudioFormat(
                  AudioFormat.Encoding.PCM_SIGNED, 44100, Short.SIZE, 1, 2,
                  44100, false);
              final AudioInputStream ais = new AudioInputStream(
                  new ByteArrayInputStream(data_l.array()), format,
                  data_l.array().length);
              AudioSystem.write(ais, AudioFileFormat.Type.WAVE, fos);
              isRecording = true;
            }
            catch (final Exception e)
            {
            }
          }
          else
          { // Suite d'enregistrement
            if (fos != null)
            {
              try
              {
                fos.write(data_l.array());
              }
              catch (final Exception e)
              {
              }
            }
          }
          data_.clear();
        }
        else
        {
          if (isRecording)
          {
            try
            {
              fos.close();
            }
            catch (final IOException e)
            {
              e.printStackTrace();
            }
            // A la fin d'enregistrement, corriger la longueur de données dans
            // le fichier.
            try
            {
              final RandomAccessFile randomFile = new RandomAccessFile(file,
                  "rw");
              // Dans un fichier PCM WAV, l'entête est 44 octets.
              final long dataLength = randomFile.length() - 44;
              final long chunkSize = dataLength + 36;

              // Le 4ème octets et les 3 suivants représentent la longeur de
              // donnée + format
              // Il faut le corriger avec la bonne longueur du ficher.
              randomFile.seek(4);
              randomFile.write(longToBytes(chunkSize));
              // Le 40ème octets et les 3 suivants représentent la longeur de
              // donnée.
              // Il faut le corriger avec la bonne longueur du ficher.
              randomFile.seek(40);
              randomFile.write(longToBytes(dataLength));

              randomFile.close();
            }
            catch (final Exception e)
            {
              e.printStackTrace();
            }
            isRecording = false;
          }
        }
      }

      Audio.getLine().write(data_.array(), 0,
          Scheduler.SamplingBufferSize * (Short.SIZE / 8));

    }
  }

  @Override
  public Module clone() throws CloneNotSupportedException
  {
    return new ModuleOutToFile();
  }
}
