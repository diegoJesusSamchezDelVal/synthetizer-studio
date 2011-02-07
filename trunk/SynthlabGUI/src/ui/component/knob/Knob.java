package ui.component.knob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class Knob extends JPanel implements MouseListener, MouseMotionListener
{
    protected Point centerPoint;
    
    protected Point pointer;
    
    protected int value;
    
    
    
    protected ArrayList<KnobListener> listeners = new ArrayList<KnobListener>();
    
    protected Knob() {
      Dimension dim = new Dimension(60,60);
      setMinimumSize(dim);
      setPreferredSize(dim);
      setSize(dim);
      setOpaque(false);
      centerPoint = new Point(30,30);
      pointer = new Point(30,0);
      
      addMouseListener(this);
      addMouseMotionListener(this);
    }
    
    public void addKnobListener(KnobListener l) {
      listeners.add(l);
    }
    
    public void removeKnobListener(KnobListener l) {
      listeners.remove(l);
    }
    
    protected void notifyListener()
    {
     for (KnobListener listener : listeners) {
       KnobEvent e = new KnobEvent(this, value);
       listener.knobTurned(e);
     }      
    }

    public void paintComponent(Graphics gc) {
      RenderingHints renderHints = new RenderingHints(
          RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      renderHints.put(RenderingHints.KEY_RENDERING,
          RenderingHints.VALUE_RENDER_QUALITY);
      ((Graphics2D) gc).setRenderingHints(renderHints);
      
      gc.setColor(Color.white);
      gc.fillOval(0, 0, getWidth(), getHeight());
      gc.setColor(Color.black);
      gc.drawOval(0, 0, getWidth()-1, getHeight()-1);
      
      gc.setColor(Color.green);
      gc.drawLine(centerPoint.x, centerPoint.y, pointer.x, pointer.y);
    }
    
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
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
      repaint();
      pointer = computePointer(e.getPoint());
      System.out.println(value);
      notifyListener();
      
    }  

    @Override
    public void mouseMoved(MouseEvent e)
    {
      repaint();      
    }
    
    protected abstract Point computePointer(Point point);
    
    protected abstract int updateValue(double k, Point currentPoint);
}
