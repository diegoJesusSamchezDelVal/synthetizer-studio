package widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

import synthlab.api.Port;

public class PortHandler extends JPanel implements MouseMotionListener,
    MouseListener
{
  private static final long serialVersionUID = 6479523243400160278L;
  
  private boolean mouseOver = false;

  public PortHandler(Port port)
  {
    setupGeneral();

    port_ = port;
    isInput_ = port_.isInput();
  }

  public void setupGeneral()
  {
    setBorder(BorderFactory.createLineBorder(Color.black));
    setBackground(Color.white);

    addMouseListener(this);
    addMouseMotionListener(this);

    setBounds(0, 0, 16, 14);
    setMinimumSize(new Dimension(16, 14));
    setMaximumSize(new Dimension(16, 14));

    setLayout(null);
    setOpaque(false);
    setVisible(true);
  }

  @Override
  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);

    if (port_.isLinked())
    {
      g.setColor(Color.yellow);
      g.fillOval(5, 3, 6, 6);
      g.setColor(Color.black);
    } else if(mouseOver) {
	g.setColor(Color.green);
	g.fillOval(5, 3, 6, 6);
	g.setColor(Color.black);
    }
    
    if ( ((ModulePoolPanel) getParent().getParent()).isLinking() && !port_.isLinked() )
    {
      ((Graphics2D)g).setStroke(new BasicStroke(2));
      g.setColor( Color.green.darker());
      g.drawOval(5, 3, 6, 6);
      ((Graphics2D)g).setStroke(new BasicStroke(1));
      g.setColor( Color.black);
    }
    else
    {
      g.drawOval(5, 3, 6, 6);
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
  }

  public Port getWrapped()
  {
    return port_;
  }
  
  @Override
  public void mouseReleased(MouseEvent e)
  {
    ((ModulePoolPanel) getParent().getParent()).endDrag((Module) getParent(), this);
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    Point start = new Point((int) (getBounds().getX()+getParent().getBounds().getX()), (int) (6+getParent().getBounds().getY()+getBounds().getY()));
    Point end = new Point((int)(e.getPoint().getX()+start.getX()), (int)(e.getY()+start.getY()-6));
    if ( !isInput_ )
      ((ModulePoolPanel) getParent().getParent()).updateDrag((Module) getParent(), this, start, end);
    else
      ((ModulePoolPanel) getParent().getParent()).updateDrag((Module) getParent(), this, end, start);
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
      repaint();
  }

  private Port port_;
  boolean isInput_;
}
