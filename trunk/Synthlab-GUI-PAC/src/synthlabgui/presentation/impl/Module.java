package synthlabgui.presentation.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import synthlab.api.Port;
import synthlabgui.presentation.ModulePresentation;
import synthlabgui.presentation.configPanel.ModuleConfigWindow;

public class Module extends JPanel implements MouseListener, MouseMotionListener, ModulePresentation
{
  private static final long serialVersionUID = -5287018845570600845L;

  private ModulePoolPanel   poolPanel;

  private static Color      highlightColor   = new Color(255, 255, 255, 255);

  private static Color      standardColor    = new Color(255, 255, 255, 255);

  private String            name;

  public Module(String name, synthlab.api.Module module, ModulePoolPanel parent)
  {
    super();
    module_ = module;
    poolPanel = parent;
    this.name = name;
    setupGeneral();
    setupShadow();
    setupPorts();
    configWindow_ = new ModuleConfigWindow(name, module_, null, new Point(0, 0));
  }

  public void setupPorts()
  {
    setLayout(null);
    int currentHeight;
    currentHeight = 30;
    for (Port p : module_.getInputs())
    {
      // Port circle
      PortHandler ph = new PortHandler(p);
      ph.setBounds(0, currentHeight, 16, 14);
      add(ph);
      ports.add(ph);
      currentHeight += 20;
    }
    currentHeight = 30;
    for (Port p : module_.getOutputs())
    {
      // Port circle
      PortHandler ph = new PortHandler(p);
      add(ph);
      ports.add(ph);
      ph.setBounds(184, currentHeight, (int) ph.getBounds().getWidth(), (int) ph.getBounds().getHeight());
      currentHeight += 20;
    }
  }

  public synthlab.api.Module getWrapped()
  {
    return module_;
  }

  private void setupShadow()
  {
    // Generate an image containing the low quality wrapping rounded
    // rectangle
    int maximumPortNumber = Math.max(module_.getInputs().size(), module_.getOutputs().size());
    shadow_ = new BufferedImage(200 + 1 + 20, maximumPortNumber * 20 + 30 + 1 + 20, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) shadow_.getGraphics();
    g.setBackground(Color.white);
    g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
    g.fillRoundRect(3, 3, 200, maximumPortNumber * 20 + 30, 20, 20);
    g.setColor(Color.white);
    // Convolve the shadow image using a median kernel
    int kSize = 5;
    float[] data = new float[kSize * kSize];
    float value = (float) (1. / (float) (kSize * kSize));
    for (int i = 0; i < data.length; i++)
    {
      data[i] = value;
    }
    op_ = new ConvolveOp(new Kernel(kSize, kSize, data));
  }

  private void setupGeneral()
  {
    setBorder(BorderFactory.createLineBorder(Color.black));
    setBackground(Color.white);
    int maximumPortNumber = Math.max(module_.getInputs().size(), module_.getOutputs().size());
    setBounds(0, 0, 200 + 10, maximumPortNumber * 20 + 30 + 10);
    addMouseListener(this);
    addMouseMotionListener(this);
    startingPosition_ = new Point();
    CloseButton cb = new CloseButton(this, poolPanel);
    cb.setLocation(185, 6);
    add(cb, 0);
  }

  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);
    // Drop shadow
    ((Graphics2D) g).drawImage(shadow_, op_, 0, 0);
    // Wrapping rounded rectangle
    int maximumPortNumber = Math.max(module_.getInputs().size(), module_.getOutputs().size());
    if (!mouseOver)
      g.setColor(standardColor);
    else
      g.setColor(highlightColor);
    g.fillRoundRect(0, 0, wide, maximumPortNumber * 20 + 30, 10, 10);
    g.setColor(Color.black);
    g.drawRoundRect(0, 0, wide, maximumPortNumber * 20 + 30, 10, 10);
    // Module name
    g.drawString(name, 100 - (g.getFontMetrics().stringWidth(module_.getName()) / 2), 15);
    // Line under module name
    g.drawLine(0, 20, 200, 20);
    // Ports
    int currentHeight = 40;
    int stepHeight = 20;
    // --- Inputs
    currentHeight = 40;
    stepHeight = 20;
    for (Port p : module_.getInputs())
    {
      // Port name
      g.drawString(p.getName(), 15, currentHeight);
      currentHeight += stepHeight;
    }
    // --- Outputs
    currentHeight = 40;
    stepHeight = 20;
    for (Port p : module_.getOutputs())
    {
      // Port name
      g.drawString(p.getName(), 200 - 15 - g.getFontMetrics().stringWidth(p.getName()), currentHeight);
      currentHeight += stepHeight;
    }
    super.paintComponents(g);
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    int x = e.getX() + getBounds().x - startingPosition_.x;
    int y = e.getY() + getBounds().y - startingPosition_.y;
    if (x < 0)
      x = 0;
    else
      if (x + getWidth() >= poolPanel.getWidth())
        x = poolPanel.getWidth() - getWidth();
    if (y < 0)
      y = 0;
    else
      if (y + getHeight() >= poolPanel.getHeight())
        y = poolPanel.getHeight() - getHeight();
    setBounds(x, y, getBounds().width, getBounds().height);
    // This will update the module pool links
    getParent().repaint();
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    startingPosition_ = e.getPoint();
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    // TODO Double clicked = open settings panel for this module
    if (e.getClickCount() == 2 && module_.getInputs().size() > 0)
    {
      if (configWindow_ == null)
        configWindow_ = new ModuleConfigWindow(name, module_, (MainWindow) getRootPane().getParent(), new Point(0, 0));
      configWindow_.show(e.getLocationOnScreen());
    }
  }

  public Port portAt(int x, int y)
  {
    int i;
    // At input port ?
    i = 0;
    for (Port p : module_.getInputs())
    {
      if (x >= 2 && y >= 30 + i * 20 && x <= 14 && y <= 30 + i * 20 + 12)
        return p;
      ++i;
    }
    // At output port ?
    i = 0;
    for (Port p : module_.getOutputs())
    {
      if (x >= 186 && y >= 30 + i * 20 && x <= 198 && y <= 30 + i * 20 + 12)
        return p;
      ++i;
    }
    return null;
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
    mouseOver = true;
    poolPanel.repaint();
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    mouseOver = false;
    poolPanel.repaint();
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }

  public ArrayList<PortHandler> getPorts()
  {
    return ports;
  }

  private Point                  startingPosition_;

  private synthlab.api.Module    module_;

  private BufferedImage          shadow_;

  private ConvolveOp             op_;

  private int                    wide      = 200;

  private ModuleConfigWindow     configWindow_;

  private boolean                mouseOver = false;

  private ArrayList<PortHandler> ports     = new ArrayList<PortHandler>();

  public void setMouseOver(boolean b)
  {
    mouseOver = b;
  }

  /**
   * Appele cette méthode avant de détruire ce module pour détruire tous ces composants correspondants.
   * */
  public void close()
  {
    if (configWindow_ != null)
      configWindow_.dispose();
  }

  public ModuleConfigWindow getConfigWindow()
  {
    return configWindow_;
  }
}
