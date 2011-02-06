package widgets;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Knob extends JPanel implements MouseListener, MouseMotionListener
{
  private static final long serialVersionUID = -2398408682438548381L;

  public Knob()
  {
    super();

    setupGeneral();
    setupImages();
  }

  public void setupGeneral()
  {
    setOpaque(false);
    setVisible(true);

    value_ = -1;

    addMouseListener(this);
    addMouseMotionListener(this);

    setBounds(0, 0, 55, 65);
  }

  public void setupImages()
  {
    background1_ = new ImageIcon("res/image/knob/scale_minmax.png").getImage();
    background2_ = new ImageIcon("res/image/knob/knob.png").getImage();

    pointer_ = new ImageIcon("res/image/knob/green_pointer.png").getImage();
  }

  public void paint(Graphics g)
  {
    super.paint(g);

    g.drawImage(background1_, 0, 0, null);
    g.drawImage(background2_, 7, 4, null);

    // Interpolate value between 5pi/4 and -pi/4
    double angle = ((-value_ + 1.) / 2.) * ((6. * Math.PI) / 4.)
        - (Math.PI / 4.);
    angle = 2 * Math.PI - angle;
    g.drawImage(pointer_, (int) (Math.cos(angle) * 12.) + 2,
        (int) (Math.sin(angle) * 12.) + 16, null);
    
    Font f = g.getFont();
    g.setFont(new Font("Arial",Font.BOLD,10));
    g.drawString( ""+(float)((int)(value_*10.))/10., 20, 60);
    g.setFont(f);
  }

  public void inc(double delta)
  {
    value_ += delta;
    if (value_ > 1.)
      value_ = 1.;
    if (value_ < -1.)
      value_ = -1.;
    repaint();
  }

  public void dec(double delta)
  {
    value_ -= delta;
    if (value_ < -1.)
      value_ = -1.;
    if (value_ > 1.)
      value_ = 1.;
    repaint();
  }

  private Image  background1_;
  private Image  background2_;
  private Image  pointer_;
  private double value_;
  private Point  dragStart_;

  @Override
  public void mouseClicked(MouseEvent arg0)
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
  public void mousePressed(MouseEvent e)
  {
    dragStart_ = e.getPoint();
  }

  @Override
  public void mouseReleased(MouseEvent arg0)
  {
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    // 1pixel drag = 0.01 value change
    inc( (dragStart_.getY()-e.getPoint().getY())/100. );
    dragStart_ = e.getPoint();
  }

  @Override
  public void mouseMoved(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }
}
