package synthlabgui.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import synthlabgui.widgets.buttons.MiniButton;

public class HorizontalDivider extends BasicSplitPaneDivider
{
  private static final long serialVersionUID = -6499359680403403645L;

  public HorizontalDivider(BasicSplitPaneUI ui)
  {
    super(ui);
    setupGeneral();
  }

  private void setupGeneral()
  {
    setLayout(null);
    MiniButton library = new MiniButton(new ImageIcon("library.png").getImage(), false);
    MiniButton config = new MiniButton(new ImageIcon("config.png").getImage(), false);
    library.setBounds(0, 500, (int) library.getBounds().getWidth(), (int) library.getBounds().getHeight());
    config.setBounds(0, 500 - 44, (int) config.getBounds().getWidth(), (int) config.getBounds().getHeight());
    add(library);
    add(config);
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
    g2.drawLine(10, 0, 10, getHeight());
    // draw components (buttons)
    paintComponents(g);
  }
}
