package ui.component.knob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class KnobPanel extends JPanel implements KnobListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private double value;
  
  private double maxValue;
  
  private double minValue;
  
  private String unit;
  
  private String pattern;

  private Knob knob;
  
  /**
   * @param min valeur minimal
   * @param max valeur maximal
   * @param unit unité 
   * @param pattern pattern des chiffres
   * 
   * */
  public KnobPanel(double min, double max, String unit, String pattern) {
    maxValue = max;
    minValue = min;    
    this.unit = unit;
    this.pattern = pattern;
    
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
    
    // dessine la valeur
    gc.setColor(Color.black);
    
    DecimalFormat df = new DecimalFormat(pattern);
    String str = df.format(value);
    gc.drawString(str, 10, 15);
    gc.drawString(unit, 45, 15);
    
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
 
    KnobPanel k = new KnobPanel(1000, 3000, "Hz", "0");
    f.add(k);
    
    k = new KnobPanel(-5, +5, "V", "0.0");
    f.add(k);
    
   
  }


}
