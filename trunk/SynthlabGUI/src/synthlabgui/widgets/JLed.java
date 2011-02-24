package synthlabgui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import synthlab.api.Port;

/**
 * A indicator of two colors that show module's working state.
 * 
 */
public class JLed extends JPanel implements Observer
{
  private static final long serialVersionUID = 1L;

  /**
   * state
   */
  private boolean           allume;

  private Color             colorOn          = new Color(100, 200, 100);

  private Color             colorOff         = new Color(200, 50, 50);

  /**
   * Constructor
   */
  public JLed()
  {
    Dimension size = new Dimension(13, 8);
    setMinimumSize(size);
    setMaximumSize(size);
    setPreferredSize(size);
    setSize(size);
    allume = false;
    setOpaque(false);
  }

  /**
   * {@inheritDoc}
   */
  public void setOn()
  {
    allume = true;
  }

  /**
   * {@inheritDoc}
   */
  public void setOff()
  {
    allume = false;
  }

  /**
   * @return vrai si la led est allumé, faux sinon.
   */
  public boolean isOn()
  {
    return allume;
  }

  /**
   * @param allume
   */
  public void setState(boolean allume)
  {
    this.allume = allume;
  }

  @Override
  public void paint(Graphics g)
  {
    if (allume)
    {
      g.setColor(colorOn);
    }
    else
    {
      g.setColor(colorOff);
    }
    g.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
  }

  @Override
  public void update(Observable o, Object arg)
  {
    double value = ((Port) o).getValues().getDouble(0);
    if (value > 0)
      setOn();
    else
      setOff();
    repaint();
  }
}