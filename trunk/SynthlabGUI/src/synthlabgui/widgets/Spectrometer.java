package synthlabgui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.nio.ByteBuffer;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import synthlab.api.Scheduler;
import flanagan.math.FourierTransform;

public class Spectrometer extends JPanel implements Observer
{
  private static final long serialVersionUID = 2305206084322377200L;

  private PortHandler       portHandler_;

  private double[]          data_;

  private double[]          specters_;

  public Spectrometer()
  {
    super();
    setupGeneral();
  }

  private void setupGeneral()
  {
    setSize(200, 100);
    setOpaque(false);
    setPreferredSize(new Dimension(200, 100));
    portHandler_ = null;
    data_ = new double[Scheduler.SamplingBufferSize];
  }

  int cpt = 0;

  private void computeFFT()
  {
    FourierTransform ft = new FourierTransform(data_);
    ft.setWelch();
    // Should be a multiple of Scheduler.SamplingBufferSize, which is
    // not easy to guess, so hardcoded to 9 :-P
    ft.setSegmentNumber(2);
    specters_ = ft.powerSpectrum()[1];
  }

  public void monitor(PortHandler p)
  {
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
      data_[i] = buffer.getDouble(i * (Double.SIZE / 8));
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
    // compute fft
    computeFFT();
    // Draw curve
    g2.setColor(new Color(80, 80, 150));
    for (int i = 0; i < specters_.length; ++i)
    {
      int x = (int) ((double) i / (double) (specters_.length) * getWidth());
      int y = (int) (specters_[i] * 2. * (double) getHeight());
      g2.drawLine(x, getHeight(), x, getHeight() - y);
    }
    // Restore previous state
    g2.setColor(Color.black);
  }

  @Override
  public void update(Observable port, Object o)
  {
    push(portHandler_.getWrapped().getValues());
  }
}
