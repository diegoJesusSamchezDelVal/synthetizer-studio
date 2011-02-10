package synthlabgui.widgets.configPanel.keyboard;

public class KeyboardEvent
{
  public KeyboardEvent(Key key, double value)
  {
    // source = key;
    this.value = value;
  }

  public double getValue()
  {
    return value;
  }

  private double value;
  // private Key source;
}
