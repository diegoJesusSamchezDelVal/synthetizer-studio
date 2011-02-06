package ui.component.knob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class KnobPanel extends JPanel implements KnobListener {

  private double value;
  
  private double maxValue;
  
  private double minValue;

  private Knob knob;
  
  public KnobPanel(double min, double max) {
    maxValue = max;
    minValue = min;    
    
    setLayout(null);
   
    Dimension dim = new Dimension(62,82);
    setMinimumSize(dim);
    setPreferredSize(dim);
    setSize(dim);
    
    
    knob = new Knob();
    knob.addKnobListener(this);
    add(knob);
    knob.setLocation(1, 20);
    
  }
  
  public void paintComponent(Graphics gc) {
    super.paintComponent(gc);
    RenderingHints renderHints = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) gc).setRenderingHints(renderHints);
    
    gc.setColor(Color.black);
    gc.drawRect(0, 0, getWidth()-1, getHeight()-1);
    gc.drawRect(0, 0, getWidth()-1, 20);
    
    gc.setColor(Color.white);
    gc.fillRect(1, 1, getWidth()-2, 18);
    
    gc.setColor(Color.black);
    gc.drawString(String.valueOf(value), 20, 15);
  } 
  
  @Override
  public void knobTurned(KnobEvent e)
  {
    // 0 .. 9999
    int scale = e.getValue();
    
    double piece = (maxValue - minValue) / 10000;
    value = minValue + piece * scale;
    repaint(0,0,getWidth(), 20);
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
 
    KnobPanel k = new KnobPanel(1000, 3000);
    f.add(k);
    
   
  }


}
