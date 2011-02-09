package synthlabgui.widgets.keyboard;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Key extends JPanel
{
  public Key()
  {
    setLayout(null);
    setOpaque(true);
    setBorder(BorderFactory.createLineBorder(Color.black));
  }

  public Color getColor()
  {
    return Color.WHITE;
  }

  private static final long serialVersionUID = -1955536235297058856L;
}