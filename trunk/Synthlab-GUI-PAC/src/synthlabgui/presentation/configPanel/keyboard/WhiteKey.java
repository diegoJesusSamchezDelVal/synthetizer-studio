package synthlabgui.presentation.configPanel.keyboard;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

public class WhiteKey extends Key
{
  public WhiteKey()
  {
    super();
    setSize(21, 71);
    setPreferredSize(this.getSize());
    setBackground(Color.white);
    setOpaque(false);
  }

  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);
    Graphics2D g2 = (Graphics2D) g;
    // Render key
    g2.setColor(Color.white);
    g2.fillRoundRect(0, 0, 20, 70, 8, 8);
    GradientPaint gradient;
    gradient = new GradientPaint(new Point(0, 0), new Color(200, 200, 200, 255), new Point(5, 0), new Color(255, 255,
        255, 0));
    g2.setPaint(gradient);
    g2.fillRoundRect(0, 0, 20, 70, 8, 8);
    gradient = new GradientPaint(new Point(15, 0), new Color(255, 255, 255, 0), new Point(20, 0), new Color(200, 200,
        200, 255));
    g2.setPaint(gradient);
    g2.fillRoundRect(0, 0, 20, 70, 8, 8);
    if (getBackground() != Color.white)
    {
      gradient = new GradientPaint(new Point(0, 70), new Color(200, 200, 200, 255), new Point(0, 0), new Color(255,
          255, 255, 255));
      g2.setPaint(gradient);
      g2.fillRoundRect(0, 0, 20, 70, 8, 8);
    }
    g2.setColor(new Color(170, 170, 170));
    g2.drawRoundRect(0, 0, 20, 70, 8, 8);
    g2.setColor(Color.black);
  }

  private static final long serialVersionUID = 6732029081995103424L;
}
