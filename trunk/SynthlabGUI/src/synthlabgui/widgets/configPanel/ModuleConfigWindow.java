package synthlabgui.widgets.configPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import synthlab.api.Module;
import synthlab.api.Port;
import synthlabgui.widgets.MainWindow;
import synthlabgui.widgets.configPanel.keyboard.KeyboardPanel;
import synthlabgui.widgets.configPanel.knob.numberKnob.NumberKnobPanel;
import synthlabgui.widgets.configPanel.knob.waveSelector.WaveSelector;
import synthlabgui.widgets.configPanel.switchButton.Switch;

/**
 * A configuration window in which contains the components to manipulate input ports.
 * */
public class ModuleConfigWindow extends JDialog
{
  private static final long                     serialVersionUID = -2827881534087119283L;

  /**
   * Hashmap that contains the unit's name.
   * 
   * @see Port.ValueUnit
   * */
  public static HashMap<Port.ValueUnit, String> unitList         = new HashMap<Port.ValueUnit, String>();

  /**
   * Module's abstract model
   * 
   * @see Module
   * */
  private Module                                module_;

  /**
   * The list contains all the config components.
   * */
  private HashMap<String, AbstractConfigPanel>  knobList         = new HashMap<String, AbstractConfigPanel>();

  /**
   * The title of this window.
   * */
  private String                                title;

  private boolean                               showing          = false;

  /**
   * Constructor
   * 
   * @param title
   *          the title of this window
   * 
   * @param module
   *          abstract model of the module
   * 
   * @param parent
   *          the window's parent component, generally it should be a JFrame
   * 
   * @param point
   *          location of the window
   * */
  public ModuleConfigWindow(String title, synthlab.api.Module module, MainWindow parent, Point point)
  {
    super();
    this.title = title;
    setModal(false);
    setAlwaysOnTop(true);
    setLayout(new BorderLayout());
    setTitle(title);
    getRootPane().setBorder(BorderFactory.createEtchedBorder());
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));
    add(contentPanel, BorderLayout.CENTER);
    setUndecorated(true);
    setDefaultLookAndFeelDecorated(false);
    initUnitList();
    module_ = module;
    AbstractConfigPanel knob;
    KeyboardPanel keyboard = null;
    for (Port p : module_.getInputs())
    {
      knob = createPanel(p);
      if (knob != null)
      {
        if (knob instanceof KeyboardPanel)
        {
          if (keyboard == null)
          {
            keyboard = (KeyboardPanel) knob;
            knobList.put(p.getName(), keyboard);
          }
          keyboard.addPort(p);
        }
        else
        {
          knob.setPort(p);
          contentPanel.add((JPanel) knob);
          knobList.put(p.getName(), knob);
        }
      }
    }
    if (keyboard != null)
    {
      contentPanel.add(keyboard);
    }
    if (contentPanel.getComponentCount() == 0)
    {
      JLabel l = new JLabel("No configuration for this module.");
      Dimension d = new Dimension(100, 30);
      l.setSize(d);
      l.setMinimumSize(d);
      l.setMinimumSize(d);
      l.setVisible(true);
      contentPanel.add(l);
      contentPanel.validate();
    }
    setLocation(point);
    setResizable(false);
    pack();
    int minWidth = title.length() * 3 + 10;
    if (getWidth() < minWidth)
      setSize(minWidth, getHeight());
    InternalMouseListener listener = new InternalMouseListener();
    addMouseListener(listener);
    addMouseMotionListener(listener);
    addTitleBar(listener);
  }

  /**
   * Add a title panel to the window
   * */
  private void addTitleBar(InternalMouseListener mouseListener)
  {
    JPanel titleBar = new JPanel(null);
    Dimension dim = new Dimension(getWidth(), 30);
    titleBar.setMinimumSize(dim);
    titleBar.setPreferredSize(dim);
    titleBar.setSize(dim);
    // titleBar.setBorder(BorderFactory.createEtchedBorder());
    titleBar.addMouseListener(mouseListener);
    titleBar.addMouseMotionListener(mouseListener);
    add(titleBar, BorderLayout.NORTH);
    //
    JLabel title = new JLabel(this.title);
    title.setLocation(getWidth() / 2 - 30, 0);
    title.setVisible(true);
    title.setBackground(Color.black);
    title.setSize(100, 29);
    titleBar.add(title);
    //
    InternalCloseButton cb = new InternalCloseButton();
    cb.setLocation(getWidth() - cb.getWidth() - 6, 4);
    titleBar.add(cb);
    setSize(getWidth(), getHeight() + titleBar.getHeight() - 20);
  }

  /**
   * create a config component according to the type of the port
   * 
   * @param port
   *          a input port
   * @return the config component for the port given in parameter
   * */
  private AbstractConfigPanel createPanel(Port port)
  {
    AbstractConfigPanel panel = null;
    if (port.getValueType() == Port.ValueType.WAVE_SHAPE)
    {
      double min = port.getValueRange().minimum;
      double max = port.getValueRange().maximum;
      panel = new WaveSelector(port.getName(), min, max, true);
    }
    else
      if (port.getValueType() == Port.ValueType.DISCRETE)
      {
        double min = port.getValueRange().minimum;
        double max = port.getValueRange().maximum;
        panel = new NumberKnobPanel(port.getName(), min, max, unitList.get(port.getValueUnit()), "0", !port.isLinked(),
            false);
      }
      else
        if (port.getValueType() == Port.ValueType.CONTINUOUS)
        {
          double min = port.getValueRange().minimum;
          double max = port.getValueRange().maximum;
          panel = new NumberKnobPanel(port.getName(), min, max, unitList.get(port.getValueUnit()), "0.0", !port
              .isLinked(), true);
        }
        else
          if (port.getValueType() == Port.ValueType.KEYBOARD)
          {
            panel = new KeyboardPanel();
          }
          else
            if (port.getValueType() == Port.ValueType.SWITCH)
            {
              panel = new Switch(port.getName(), "On", "Off");
            }
            else
            {
              panel = null;
            }
    return panel;
  }

  /**
   * Update the config components' states and repaint them. enable/disable them according to the ports' are linked or
   * not.
   * */
  public void refresh()
  {
    for (Port p : module_.getInputs())
    {
      String name = p.getName();
      AbstractConfigPanel knob = knobList.get(name);
      if (knob != null)
        knob.setState(!p.isLinked());
    }
    repaint();
  }

  /**
   * Show the window at a specific point.
   * 
   * @param point
   *          location of the window
   * */
  public void show(Point point)
  {
    for (Port p : module_.getInputs())
    {
      String name = p.getName();
      AbstractConfigPanel knob = knobList.get(name);
      if (knob != null)
        knob.setState(!p.isLinked());
    }
    setLocation(point);
    setVisible(true);
    showing = true;
  }

  /**
   * Hide the window.
   * */
  public void unshow(boolean userOperation)
  {
    setVisible(false);
    if (userOperation)
      showing = false;
  }

  public boolean isShowing()
  {
    return showing;
  }

  /**
   * Initialize unit name hashmap.
   * 
   * @see Port.ValueUnit
   * */
  private static void initUnitList()
  {
    unitList.put(Port.ValueUnit.AMPLITUDE, "");
    unitList.put(Port.ValueUnit.PERCENTAGE, "%");
    unitList.put(Port.ValueUnit.DECIBELS, "db");
    unitList.put(Port.ValueUnit.HERTZ, "Hz");
    unitList.put(Port.ValueUnit.MILLISECONDS, "ms");
    unitList.put(Port.ValueUnit.VOLT, "v");
  }

  private class InternalCloseButton extends JPanel implements MouseListener, MouseMotionListener
  {
    /**
		 * 
		 */
    private static final long serialVersionUID = -7952186173963083283L;

    private boolean           mouseOver        = false;

    private boolean           mousePressed     = false;

    private Color             normalColor      = Color.black;

    private Color             mouseOverColor   = Color.red;

    private int               scale            = 1;

    public InternalCloseButton()
    {
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
      // Enable anti-aliasing
      RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      ((Graphics2D) gc).setRenderingHints(renderHints);
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
        unshow(true);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }
  }

  /**
   * Ecouteur interne pour déplacment de dialog
   * */
  private class InternalMouseListener implements MouseListener, MouseMotionListener
  {
    Point start;

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
      start = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
      int x = getLocation().x;
      int y = getLocation().y;
      setLocation(x + e.getPoint().x - start.x, y + e.getPoint().y - start.y);
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }
  }
}
