package synthlabgui.presentation.configPanel.keyboard;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

public class BlackKey extends Key
{
  public BlackKey()
  {
    super();
    setSize(11, 40);
    setPreferredSize(this.getSize());
    setBackground(Color.black);
    setOpaque(false);
  }

  public Color getColor()
  {
    return Color.BLACK;
  }

  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);
    Graphics2D g2 = (Graphics2D) g;
    // Render key
    g2.setColor(new Color(60, 60, 60));
    g2.fillRoundRect(0, 0, 10, 40, 8, 8);
    GradientPaint gradient;
    gradient = new GradientPaint(new Point(0, 0), new Color(0, 0, 0, 255), new Point(3, 0), new Color(60, 60, 60, 0));
    g2.setPaint(gradient);
    g2.fillRoundRect(0, 0, 10, 40, 8, 8);
    gradient = new GradientPaint(new Point(7, 0), new Color(60, 60, 60, 0), new Point(10, 0), new Color(0, 0, 0, 255));
    g2.setPaint(gradient);
    g2.fillRoundRect(0, 0, 10, 40, 8, 8);
    if (getBackground() != Color.black)
    {
      gradient = new GradientPaint(new Point(0, 40), new Color(0, 0, 0, 255), new Point(0, 0), new Color(60, 60, 60, 0));
      g2.setPaint(gradient);
      g2.fillRoundRect(0, 0, 10, 40, 8, 8);
    }
    g2.setColor(Color.black);
  }

  private static final long serialVersionUID = 2293420274238865119L;
}
