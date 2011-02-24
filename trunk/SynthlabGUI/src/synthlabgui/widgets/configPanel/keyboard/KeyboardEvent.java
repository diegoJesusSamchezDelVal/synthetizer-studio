package synthlabgui.widgets.configPanel.keyboard;

/**
 * A event that occurred when a keyboard's key is pressed or released.
 * */
public class KeyboardEvent
{
  private double    value;

  private int       type;

  public static int PRESSED  = 0;

  public static int RELEASED = 1;

  public KeyboardEvent(Key key, double value, int type)
  {
    this.value = value;
    this.type = type;
  }

  public double getValue()
  {
    return value;
  }

  public int getType()
  {
    return type;
  }
}
