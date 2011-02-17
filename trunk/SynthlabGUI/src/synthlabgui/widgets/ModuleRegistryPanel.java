package synthlabgui.widgets;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import synthlab.api.ModuleRegistry;
import synthlab.api.ModuleRegistryFactory;
import synthlab.internal.modules.ModuleEnvelope;
import synthlab.internal.modules.ModuleKeyboard;
import synthlab.internal.modules.ModuleLFO;
import synthlab.internal.modules.ModuleMixer;
import synthlab.internal.modules.ModuleMultiplexer;
import synthlab.internal.modules.ModuleOut;
import synthlab.internal.modules.ModuleSequencer;
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
    addModule(new ModuleLFO());
    addModule(new ModuleVCO());
    addModule(new ModuleVCA());
    addModule(new ModuleMixer());
    addModule(new ModuleMultiplexer());
    addModule(new ModuleVCF());
    addModule(new ModuleEnvelope());
    addModule(new ModuleOut());
    addModule(new ModuleKeyboard());
    addModule(new ModuleSequencer());
    setupGeneral();
  }

  private void setupGeneral()
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setVisible(true);
  }

  public void addModule(synthlab.api.Module module)
  {
    registry_.register(module);
    add(new ModulePrototype(module));
    counterTable.put(module.getName(), 1);
    if (getParent() != null)
      getParent().validate();
    validate();
    repaint();
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
    g2.fillRoundRect(5, 5, getWidth() - 18, getHeight() - 11, 10, 10);
    // Outer bg
    g2.setColor(new Color(150, 150, 150));
    g2.drawRoundRect(5, 5, getWidth() - 18, getHeight() - 11, 10, 10);
  }

  private ModuleRegistry           registry_;

  private HashMap<String, Integer> counterTable = new HashMap<String, Integer>();

  public int getCounter(String name)
  {
    return counterTable.get(name);
  }

  public void increaseCounter(String name)
  {
    int i = counterTable.get(name) + 1;
    counterTable.put(name, i);
  }
}
