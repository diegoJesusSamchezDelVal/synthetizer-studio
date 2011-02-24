package synthlabgui.widgets;

import java.awt.Color;
import java.awt.Dimension;
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
import synthlabgui.widgets.configPanel.ModuleConfigWindow;

public class Module extends JPanel implements MouseListener, MouseMotionListener
{
  public Module(String name, synthlab.api.Module module, ModulePoolPanel parent)
  {
    super();
    module_ = module;
    poolPanel_ = parent;
    this.name_ = name;
    setupGeneral();
    setupShadow();
    setupPorts();
    configWindow_ = new ModuleConfigWindow(name, module_, null/* parent.getParentPanel() */, new Point(0, 0));
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
      ports_.add(ph);
      currentHeight += 20;
      // TODO hardcode ici
      if (module_.getName() == "Record Out" && p.getValueType() == Port.ValueType.SWITCH)
      {
        setupLED(p);
      }
    }
    currentHeight = 30;
    for (Port p : module_.getOutputs())
    {
      // Port circle
      PortHandler ph = new PortHandler(p);
      add(ph);
      ports_.add(ph);
      ph.setBounds(184, currentHeight, (int) ph.getBounds().getWidth(), (int) ph.getBounds().getHeight());
      currentHeight += 20;
    }
  }

  private void setupLED(Port p)
  {
    JLed led;
    led = new JLed();
    add(led, 0);
    led.setLocation(6, 6);
    led.setVisible(true);
    p.addObserver(led);
    System.out.println("Led added.");
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
    // close button
    InternamCloseButton cb = new InternamCloseButton(this, poolPanel_);
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
    if (!mouseOver_)
      g.setColor(standardColor);
    else
      g.setColor(highlightColor);
    g.fillRoundRect(0, 0, wide_, maximumPortNumber * 20 + 30, 10, 10);
    g.setColor(Color.black);
    g.drawRoundRect(0, 0, wide_, maximumPortNumber * 20 + 30, 10, 10);
    // Module name
    g.drawString(name_, 100 - (g.getFontMetrics().stringWidth(module_.getName()) / 2), 15);
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
      if (x + getWidth() >= poolPanel_.getWidth())
        x = poolPanel_.getWidth() - getWidth();
    if (y < 0)
      y = 0;
    else
      if (y + getHeight() >= poolPanel_.getHeight())
        y = poolPanel_.getHeight() - getHeight();
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
    if (e.getClickCount() == 2 && module_.getInputs().size() > 0)
    {
      if (configWindow_ == null)
        configWindow_ = new ModuleConfigWindow(name_, module_, (MainWindow) getRootPane().getParent(), new Point(0, 0));
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
    mouseOver_ = true;
    poolPanel_.repaint();
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    mouseOver_ = false;
    poolPanel_.repaint();
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }

  public ArrayList<PortHandler> getPorts()
  {
    return ports_;
  }

  public void setMouseOver(boolean b)
  {
    mouseOver_ = b;
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

  private Point                  startingPosition_;

  private synthlab.api.Module    module_;

  private BufferedImage          shadow_;

  private ConvolveOp             op_;

  private int                    wide_            = 200;

  private ModuleConfigWindow     configWindow_;

  private boolean                mouseOver_       = false;

  private ArrayList<PortHandler> ports_           = new ArrayList<PortHandler>();

  private static final long      serialVersionUID = -5287018845570600845L;

  private ModulePoolPanel        poolPanel_;

  private String                 name_;

  private static Color           highlightColor   = new Color(255, 255, 255, 255);

  private static Color           standardColor    = new Color(255, 255, 255, 255);

  public class InternamCloseButton extends JPanel implements MouseListener, MouseMotionListener
  {
    private ModulePoolPanel   poolPanel;

    private static final long serialVersionUID = -4424643932151754593L;

    Module                    module;

    private Color             normalColor      = Color.black;

    private Color             mouseOverColor   = Color.red;

    private int               scale            = 1;

    boolean                   mouseOver        = false;

    private boolean           mousePressed     = false;

    public InternamCloseButton(Module parent, ModulePoolPanel poolPanel)
    {
      module = parent;
      this.poolPanel = poolPanel;
      Dimension dim = new Dimension(8, 8);
      setMinimumSize(dim);
      setMaximumSize(dim);
      setSize(dim);
      setLayout(null);
      setOpaque(false);
      setVisible(true);
      addMouseListener(this);
      addMouseMotionListener(this);
    }

    public void paint(Graphics gc)
    {
      if (mouseOver)
        gc.setColor(mouseOverColor);
      else
        gc.setColor(normalColor);
      if (!mousePressed)
      {
        gc.drawLine(0, 0, getWidth() - 1, getHeight() - 1);
        gc.drawLine(0, getHeight() - 1, getWidth() - 1, 0);
      }
      else
      {
        gc.drawLine(0 + scale, 0 + scale, getWidth() - 1 - scale, getHeight() - 1 - scale);
        gc.drawLine(0 + scale, getHeight() - 1 - scale, getWidth() - scale - 1, 0 + scale);
      }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
      mouseOver = true;
      repaint();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
      mouseOver = false;
      repaint();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
      mousePressed = true;
      repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
      mousePressed = false;
      repaint();
      if (this.contains(e.getPoint()))
        poolPanel.removeModule(module);
    }

    @Override
    public void mouseDragged(MouseEvent arg0)
    {
    }

    @Override
    public void mouseMoved(MouseEvent arg0)
    {
    }
  }
}
