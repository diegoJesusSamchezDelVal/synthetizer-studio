package widgets;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import synthlab.api.Port;

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

    int maximumPortNumber = Math.max(module_.getInputs().size(), module_
        .getOutputs().size());
    setBounds(0, 0, 200 + 10, maximumPortNumber * 20 + 30 + 10);

    addMouseListener(this);
    addMouseMotionListener(this);

    startingPosition_ = new Point();
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
      // Port circle
      if (p.isLinked())
      {
        g.setColor(Color.yellow);
        g.fillOval(5, currentHeight - 7, 6, 6);
        g.setColor(Color.black);
      }
      g.drawOval(5, currentHeight - 7, 6, 6);
      // Port name
      g.drawString(p.getName(), 15, currentHeight);
      currentHeight += stepHeight;
    }

    // --- Outputs
    currentHeight = 40;
    stepHeight = 20;
    for (Port p : module_.getOutputs())
    {
      // Port circle
      if (p.isLinked())
      {
        g.setColor(Color.yellow);
        g.fillOval(200 - 5 - 6, currentHeight - 7, 6, 6);
        g.setColor(Color.black);
      }
      g.drawOval(200 - 5 - 6, currentHeight - 7, 6, 6);
      // Port name
      g.drawString(p.getName(),
          200 - 15 - g.getFontMetrics().stringWidth(p.getName()), currentHeight);
      currentHeight += stepHeight;
    }
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    // Move the module
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
    //TODO Double clicked = open settings panel for this module
    if ( e.getClickCount()==2 )
    {
      // JOptionPane.showMessageDialog(this, "Module settings dialog not implement yet.");
      new ModuleConfigWindow(module_, (JFrame)getRootPane().getParent());
    }
  }

  @Override
  public void mouseMoved(MouseEvent arg0)
  {
  }

  @Override
  public void mouseEntered(MouseEvent arg0)
  {
  }

  @Override
  public void mouseExited(MouseEvent arg0)
  {
  }

  @Override
  public void mouseReleased(MouseEvent arg0)
  {
  }

  private Point               startingPosition_;
  private synthlab.api.Module module_;
  private BufferedImage       shadow_;
  private ConvolveOp          op_;
  
   
}
