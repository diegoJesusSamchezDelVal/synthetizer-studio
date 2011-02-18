package synthlabgui.presentation.buttons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.ButtonModel;
import javax.swing.JButton;

public class MiniButton extends JButton
{
  private static final long serialVersionUID = -3853554874805994862L;

  private Image             image_;

  private boolean           horizontal_;

  public MiniButton(Image image, boolean horizontal)
  {
    super();
    image_ = image;
    horizontal_ = horizontal;
    setupGeneral();
  }

  private void setupGeneral()
  {
    setVisible(true);
    if (horizontal_)
    {
      setBounds(0, 0, 42, 22);
      setSize(42, 22);
      setPreferredSize(new Dimension(42, 22));
      setMinimumSize(new Dimension(42, 22));
      setMaximumSize(new Dimension(42, 22));
    }
    else
    {
      setBounds(0, 0, 22, 42);
      setSize(22, 42);
      setPreferredSize(new Dimension(22, 42));
      setMinimumSize(new Dimension(22, 42));
      setMaximumSize(new Dimension(22, 42));
    }
    setCursor(Cursor.getDefaultCursor());
  }

  public void setImage(Image image)
  {
    image_ = image;
  }

  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);
    Graphics2D g2 = (Graphics2D) g;
    // inner bg
    ButtonModel model = getModel();
    Color color;
    GradientPaint gradient;
    gradient = new GradientPaint(new Point(0, 0), new Color(255, 255, 255), new Point(0, 20), new Color(230, 230, 230));
    color = new Color(120, 120, 120);
    if (model.isRollover())
    {
      gradient = new GradientPaint(new Point(0, 0), new Color(255, 255, 255), new Point(0, 20),
          new Color(240, 240, 240));
      color = new Color(80, 80, 80);
    }
    if (model.isPressed())
    {
      gradient = new GradientPaint(new Point(0, 0), new Color(230, 230, 230), new Point(0, 20),
          new Color(255, 255, 255));
      color = new Color(120, 120, 120);
    }
    g2.setPaint(gradient);
    if (horizontal_)
      g2.fillRoundRect(1, 1, 40, 19, 20, 20);
    else
      g2.fillRoundRect(1, 1, 19, 40, 20, 20);
    // outer bg
    g2.setColor(color);
    if (horizontal_)
      g2.drawRoundRect(1, 1, 40, 19, 20, 20);
    else
      g2.drawRoundRect(1, 1, 19, 40, 20, 20);
    // image
    if (horizontal_)
      g2.drawImage(image_, 15, 3, null);
    else
      g2.drawImage(image_, 3, 14, null);
  }
}
