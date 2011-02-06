package ui.component.knob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Knob extends JPanel implements MouseListener, MouseMotionListener
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Point centerPoint;
  
  private Point pointer;
    
  private boolean hasBlock = true ;
  
  private int value = 50;
  
  private ArrayList<KnobListener> listeners = new ArrayList<KnobListener>();

  public Knob() {
    Dimension dim = new Dimension(60,60);
    setMinimumSize(dim);
    setPreferredSize(dim);
    setSize(dim);
    setOpaque(false);
    
    centerPoint = new Point(30,30);
    new Point(centerPoint);
    pointer = new Point(30,0);
    
    addMouseListener(this);
    addMouseMotionListener(this);
  }
  
  public void paintComponent(Graphics gc){
    RenderingHints renderHints = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) gc).setRenderingHints(renderHints);
    
    gc.setColor(Color.white);
    gc.fillOval(0, 0, getWidth(), getHeight());
    gc.setColor(Color.black);
    gc.drawOval(0, 0, getWidth()-1, getHeight()-1);
    
    //gc.setColor(Color.green);
    // gc.drawLine(centerPoint.x, centerPoint.y, currentPoint.x, currentPoint.y);
    gc.setColor(Color.green);
    gc.drawLine(centerPoint.x, centerPoint.y, pointer.x, pointer.y);
  }

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
   e.getPoint();    
  }

  @Override
  public void mouseReleased(MouseEvent arg0)
  {
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {    
    repaint();
    pointer = updatePointer(e.getPoint());
    //System.out.println(value);
    
    notifyListener();
    
    e.getPoint();    
  }

  private void notifyListener()
  {
   for (KnobListener listener : listeners) {
     KnobEvent e = new KnobEvent(this, value);
     listener.knobTurned(e);
   }
    
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    repaint();
    
  }
  
  public Point updatePointer(Point currentPoint) {
    
    int RADIUS = 28;
    
    if ((centerPoint.x - currentPoint.x) != 0) {
        double k =((double)(centerPoint.y - currentPoint.y))/((double)(centerPoint.x - currentPoint.x));
        double b = centerPoint.y - (k * centerPoint.x);   
        
        if (hasBlock  && currentPoint.y > centerPoint.y && (k > Math.tan(Math.PI/6) || k < Math.tan(-Math.PI/6))){
            return pointer;
        } else {
            
            value = updateValue(k, currentPoint);
            
            boolean found = false;
            double x = currentPoint.x;
            while(!found) {
                double y = k * x + b;
                
                double r = Math.sqrt( Math.pow((centerPoint.y - y),2) + Math.pow((centerPoint.x - x),2));
              
                if (Math.abs(r - RADIUS) <=2.0) {
                    found = true;
                    break;
                } else {
                    if (x < centerPoint.x && r > RADIUS)
                      x+=0.01;
                    else if (x < centerPoint.x && r < RADIUS)
                      x-=0.01;
                    else if (x > centerPoint.x && r > RADIUS)
                      x-=0.01;
                    else if (x > centerPoint.x && r < RADIUS)
                      x+=0.01;          
                }     
            }          
            int y = (int) (k * x + b);
            return new Point((int) x,y);
        }
    } else {        
      
        if (currentPoint.y < centerPoint.y) {
           value = 50; 
           return new Point(centerPoint.x, centerPoint.y - RADIUS);
        } else
            return pointer;         
    }
}
  
  private int updateValue(double k, Point currentPoint)
  {
    double angle = Math.atan(k);
    int result;
    if (currentPoint.x < centerPoint.x){
       angle += Math.PI/6;
       result = (int) (angle / (Math.PI / 150) /2);
      
    } else {
      angle += Math.PI/2;
      result = (int) (angle / (Math.PI / 150)/2) +50;            
    }
    return result;
  }

  /**
   * Test 
   * */
  public static void main(String [] args) {
    JFrame f = new JFrame();
    f.setLayout(new FlowLayout());
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(400,400);
    f.setVisible(true);
 
    Knob k = new Knob();
    f.add(k);
    
   
  }
  
}
