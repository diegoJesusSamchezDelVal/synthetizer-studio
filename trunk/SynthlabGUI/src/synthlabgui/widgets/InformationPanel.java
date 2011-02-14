package synthlabgui.widgets;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class InformationPanel extends JPanel
{
  private static final long serialVersionUID = 3415209104738851835L;

  private Oscilloscope      oscilloscope_;

  private Spectrometer      spectrometer_;

  public InformationPanel(Oscilloscope o, Spectrometer s)
  {
    super();
    oscilloscope_ = o;
    spectrometer_ = s;
    setupGeneral();
  }

  public void monitor(PortHandler p)
  {
    oscilloscope_.monitor(p);
    spectrometer_.monitor(p);
    validate();
    repaint();
  }

  private void setupGeneral()
  {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setOpaque(false);
    setVisible(true);
    add(oscilloscope_);
    add(spectrometer_);
    oscilloscope_.setBounds(10, 10, (int) oscilloscope_.getBounds().getWidth(), (int) oscilloscope_.getBounds()
        .getHeight());
    spectrometer_.setBounds((int) oscilloscope_.getBounds().getWidth(), (int) oscilloscope_.getBounds().getHeight(),
        (int) spectrometer_.getBounds().getWidth(), (int) spectrometer_.getBounds().getHeight());
  }

  public void paintComponent(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);
    Graphics2D g2 = (Graphics2D) g;
    // back cg
    g2.setColor(new Color(237, 237, 237));
    g2.fillRect(0, 0, getWidth(), getHeight());
    // Inner bg
    GradientPaint background = new GradientPaint(new Point(0, 0), new Color(255, 255, 255), new Point(0, getHeight()),
        new Color(230, 230, 230));
    g2.setPaint(background);
    g2.fillRoundRect(5, 5, getWidth() - 15, getHeight() - 11, 10, 10);
    // Outer bg
    g2.setColor(new Color(150, 150, 150));
    g2.drawRoundRect(5, 5, getWidth() - 15, getHeight() - 11, 10, 10);
  }
}
