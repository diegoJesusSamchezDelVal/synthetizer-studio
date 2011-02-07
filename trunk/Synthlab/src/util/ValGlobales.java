package util;

public class ValGlobales{
  
  /**
   * variable globales relatives
   * */
  public static int bufferSize = 10;     // ms
  private static int sampleRate = 44100;  //Hz
  private static int sampleSize = 16;     //bit
  
  public static final int PITCH_IN_START = -5;  //vol
	public static final int PITCH_IN_MIN = -5;    //vol
	public static final int PITCH_IN_MAX = 5;     //vol
	
	public static final int SIGNAL_SINE = 1;
	public static final int SIGNAL_SQUARE = 2;
	public static final int SIGNAL_SAWTOOTH = 3;
	public static final int SIGNAL_TRIANGLE = 4;

}
