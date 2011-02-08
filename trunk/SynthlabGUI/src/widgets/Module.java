package widgets;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import synthlab.api.Port;
import synthlab.internal.BasicPort;

public class Module extends JPanel implements MouseListener,
    MouseMotionListener
{
  private static final long serialVersionUID = -5287018845570600845L;

  public Module(synthlab.api.Module module)
  {
    super();

    module_ = module;

    setupGeneral();
    setupShadow();
    setupPorts();
  }
  
  public void setupPorts()
  {
    setLayout( null );
    int currentHeight;
    
    currentHeight = 30;
    for ( Port p : module_.getInputs() )
    {
      // Port circle
      PortHandler ph = new PortHandler(p);
      ph.setBounds( 0, currentHeight, 16, 14 );
      add( ph );
      currentHeight += 20;
    }
    
    currentHeight = 30;
    for ( Port p : module_.getOutputs() )
    {
      // Port circle
      PortHandler ph = new PortHandler(p);
      add( ph );
      ph.setBounds( 184, currentHeight, (int)ph.getBounds().getWidth(), (int)ph.getBounds().getHeight() );
      currentHeight += 20;
    }
  }

  public synthlab.api.Module getWrapped()
  {
    return module_;
  }

  private void setupShadow()
  {
    // Generate an image containing the low quality wrapping rounded rectangle
    int maximumPortNumber = Math.max(module_.getInputs().size(), module_
        .getOutputs().size());
    shadow_ = new BufferedImage(200 + 1 + 20, maximumPortNumber * 20 + 30 + 1
        + 20, BufferedImage.TYPE_INT_ARGB);
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
    
    CloseButton cb = new CloseButton(this);
    cb.setLocation(185, 4);
    add(cb, 0);
  }

  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);

    // Drop shadow
    ((Graphics2D) g).drawImage(shadow_, op_, 0, 0);

    // Wrapping rounded rectangle
    int maximumPortNumber = Math.max(module_.getInputs().size(), module_
        .getOutputs().size());
    g.setColor(Color.white);
    g.fillRoundRect(0, 0, 200, maximumPortNumber * 20 + 30, 10, 10);
    g.setColor(Color.black);
    g.drawRoundRect(0, 0, 200, maximumPortNumber * 20 + 30, 10, 10);

    // Module name
    g.drawString(module_.getName(),
        100 - (g.getFontMetrics().stringWidth(module_.getName()) / 2), 15);

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
      g.drawString(p.getName(),
          200 - 15 - g.getFontMetrics().stringWidth(p.getName()), currentHeight);
      currentHeight += stepHeight;
    }
    
      super.paintComponents(g);
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    setBounds(e.getX() + getBounds().x - startingPosition_.x, e.getY()
        + getBounds().y - startingPosition_.y, getBounds().width,
        getBounds().height);

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
    if (e.getClickCount() == 2)
    {
      // JOptionPane.showMessageDialog(this, "Module settings dialog not implement yet.");
      new ModuleConfigWindow(module_, (JFrame)getRootPane().getParent(), e.getLocationOnScreen());
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
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }

  private Point               startingPosition_;
  private synthlab.api.Module module_;
  private BufferedImage       shadow_;
  private ConvolveOp          op_;
}
