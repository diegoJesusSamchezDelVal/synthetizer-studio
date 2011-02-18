package synthlabgui.presentation.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.nio.ByteBuffer;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import synthlab.api.Scheduler;

public class Oscilloscope extends JPanel implements Observer
{
  private static final long serialVersionUID = 2305206084322377200L;

  private PortHandler       portHandler_;

  ByteBuffer                data_;

  public Oscilloscope()
  {
    super();
    setupGeneral();
  }

  private void setupGeneral()
  {
    setSize(200, 100);
    setPreferredSize(new Dimension(200, 100));
    setOpaque(false);
    portHandler_ = null;
    data_ = ByteBuffer.allocate(Scheduler.SamplingBufferSize * (Double.SIZE / 8));
  }

  public void monitor(PortHandler p)
  {
    for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
      data_.putDouble(i * (Double.SIZE / 8), 0);
    if (portHandler_ != null)
      portHandler_.getWrapped().deleteObserver(this);
    portHandler_ = p;
    if (portHandler_ != null)
      portHandler_.getWrapped().addObserver(this);
  }

  public void push(ByteBuffer buffer)
  {
    // Sanity check
    if (portHandler_ == null || portHandler_.getWrapped() == null)
      return;
    for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
      data_.putDouble(i * (Double.SIZE / 8), portHandler_.getWrapped().getValues().getDouble(i * (Double.SIZE / 8)));
    repaint();
  }

  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);
    super.paint(g);
    Graphics2D g2 = (Graphics2D) g;
    // Draw axes
    g2.setColor(Color.gray);
    g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
    g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
    // Draw curve
    GeneralPath path = new GeneralPath();
    path.moveTo(0, getHeight());
    for (int i = 0; i < Scheduler.SamplingBufferSize; ++i)
    {
      int x = (int) ((double) i / (double) (Scheduler.SamplingBufferSize) * getWidth());
      int y = (int) ((double) ((data_.getDouble(i * (Double.SIZE / 8)) + 1) / 2.) * (double) getHeight());
      path.lineTo(x, getHeight() - y);
    }
    path.lineTo(getWidth(), getHeight());
    path.closePath();
    g2.setColor(new Color(100, 200, 100, 150));
    g2.fill(path);
    g2.setColor(new Color(80, 150, 80, 255));
    g2.draw(path);
    // Restore previous state
    g2.setColor(Color.black);
  }

  @Override
  public void update(Observable port, Object o)
  {
    push(portHandler_.getWrapped().getValues());
  }
}
