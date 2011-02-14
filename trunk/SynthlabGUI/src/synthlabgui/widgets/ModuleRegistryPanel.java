package synthlabgui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import synthlab.api.ModuleRegistry;
import synthlab.api.ModuleRegistryFactory;
import synthlab.internal.modules.ModuleEnvelope;
import synthlab.internal.modules.ModuleKeyboard;
import synthlab.internal.modules.ModuleLFO;
import synthlab.internal.modules.ModuleMixer;
import synthlab.internal.modules.ModuleOut;
import synthlab.internal.modules.ModuleVCA;
import synthlab.internal.modules.ModuleVCF;
import synthlab.internal.modules.ModuleVCO;

public class ModuleRegistryPanel extends JPanel
{
  private static final long serialVersionUID = -2727139434040925986L;

  public ModuleRegistryPanel()
  {
    super();
    // Create the module registry
    registry_ = ModuleRegistryFactory.createDefault();
    // Add default modules
    add(Box.createRigidArea(new Dimension(0, 20)));
    addModule(new ModuleLFO());
    addModule(new ModuleVCO());
    addModule(new ModuleVCA());
    addModule(new ModuleMixer());
    addModule(new ModuleVCF());
    addModule(new ModuleEnvelope());
    addModule(new ModuleOut());
    addModule(new ModuleKeyboard());
    setupGeneral();
  }

  private void setupGeneral()
  {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setVisible(true);
  }

  public void addModule(synthlab.api.Module module)
  {
    registry_.register(module);
    add(new ModulePrototype(module));
    validate();
    repaint();
  }

  public void paint(Graphics g)
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
    g2.fillRoundRect(5, 5, getWidth() - 18, getHeight() - 11, 10, 10);
    // Outer bg
    g2.setColor(new Color(150, 150, 150));
    g2.drawRoundRect(5, 5, getWidth() - 18, getHeight() - 11, 10, 10);
    // Paint inner components
    paintComponents(g);
  }

  private ModuleRegistry registry_;
}
