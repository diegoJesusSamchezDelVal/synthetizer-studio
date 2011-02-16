package synthlabgui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import synthlab.api.Port;

public class ModulePrototype extends JPanel implements MouseMotionListener
{
  private static final long serialVersionUID = 7438195823860532921L;

  public ModulePrototype(synthlab.api.Module module)
  {
    super();
    module_ = module;
    setupGeneral();
    setupShadow();
    setupDrag();
  }

  public synthlab.api.Module getWrapped()
  {
    return module_;
  }

  private void setupDrag()
  {
    setTransferHandler(new ModuleTransferHandler());
  }

  private void setupShadow()
  {
    // Generate an image containing the low quality wrapping rounded rectangle
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
    setPreferredSize(new Dimension(200 + 10, maximumPortNumber * 20 + 30 + 10));
    setMaximumSize(new Dimension(200 + 10, maximumPortNumber * 20 + 30 + 10));
    setMinimumSize(new Dimension(200 + 10, maximumPortNumber * 20 + 30 + 10));
    setTransferHandler(new TransferHandler("ModulePrototype"));
    MouseListener ml = new MouseAdapter() {
      public void mousePressed(MouseEvent e)
      {
        JComponent c = (JComponent) e.getSource();
        TransferHandler th = c.getTransferHandler();
        th.exportAsDrag(c, e, TransferHandler.COPY);
      }
    };
    addMouseListener(ml);
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
    g.setColor(Color.white);
    g.fillRoundRect(0, 0, 200, maximumPortNumber * 20 + 30, 10, 10);
    g.setColor(Color.black);
    g.drawRoundRect(0, 0, 200, maximumPortNumber * 20 + 30, 10, 10);
    // Module name
    g.drawString(module_.getName(), 100 - (g.getFontMetrics().stringWidth(module_.getName()) / 2), 15);
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
      g.drawString(p.getName(), 200 - 15 - g.getFontMetrics().stringWidth(p.getName()), currentHeight);
      currentHeight += stepHeight;
    }
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    getTransferHandler().exportAsDrag(this, e, TransferHandler.COPY);
  }

  @Override
  public void mouseMoved(MouseEvent arg0)
  {
  }

  private synthlab.api.Module module_;

  private BufferedImage       shadow_;

  private ConvolveOp          op_;
}
