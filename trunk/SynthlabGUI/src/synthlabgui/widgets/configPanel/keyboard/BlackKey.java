package synthlabgui.widgets.configPanel.keyboard;

import java.awt.Color;

public class BlackKey extends Key
{
  public BlackKey()
  {
    super();
    setSize(10, 40);
    setPreferredSize(this.getSize());
    setBackground(Color.BLACK);
  }

  public Color getColor()
  {
    return Color.BLACK;
  }

  private static final long serialVersionUID = 2293420274238865119L;
}
