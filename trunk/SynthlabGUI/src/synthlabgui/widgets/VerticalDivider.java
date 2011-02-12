package synthlabgui.widgets;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import synthlabgui.widgets.buttons.MiniButton;

public class VerticalDivider extends BasicSplitPaneDivider
{
  private static final long serialVersionUID = -6499359680403403645L;

  public VerticalDivider(BasicSplitPaneUI ui)
  {
    super(ui);
    setupGeneral();
  }

  private void setupGeneral()
  {
    setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
    MiniButton mute = new MiniButton(new ImageIcon("mute.png").getImage(), true);
    MiniButton sound = new MiniButton(new ImageIcon("sound.png").getImage(), true);
    add(mute);
    add(sound);
  }

  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);
    Graphics2D g2 = (Graphics2D) g;
    // back bg
    g2.setColor(new Color(237, 237, 237));
    g2.fillRect(0, 0, getWidth(), getHeight());
    // back line
    g2.setColor(new Color(200, 200, 200));
    g2.drawLine(0, 10, getWidth(), 10);
    // draw components (buttons)
    paintComponents(g);
  }
}
