package widgets.knob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class NumberKnobPanel extends JPanel implements KnobListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private double value;
  
  private double maxValue;
  
  private double minValue;
  
  private String unit;
  
  private String pattern;

  private NumberKnob knob;
  
  /**
   * @param min valeur minimal
   * @param max valeur maximal
   * @param unit unité 
   * @param pattern pattern des chiffres
   * 
   * */
  
  
  
  private int numberDisplaySize =20;
  
  private int titleSize = 20;
  
  private Dimension size = new Dimension(AbstractKnob.size.width + 20 + 2, 20 + 2 + AbstractKnob.size.height + titleSize + numberDisplaySize);

  private String title;
  
  public NumberKnobPanel(String title, double min, double max, String unit, String pattern) {
    this.title = title;
    maxValue = max;
    minValue = min;    
    this.unit = unit;
    this.pattern = pattern;
    
    setLayout(null);
   
    
    setMinimumSize(size);
    setPreferredSize(size);
    setSize(size);
    
    
    knob = new NumberKnob();
    knob.addKnobListener(this);
    add(knob);
    knob.setLocation((size.width-AbstractKnob.size.width) /2, numberDisplaySize + titleSize + 11);
    
  }
  
  public void paintComponent(Graphics gc) {
    super.paintComponent(gc);
    RenderingHints renderHints = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) gc).setRenderingHints(renderHints);
    
    //cadre
    gc.setColor(Color.black);
    gc.drawRect(0, 0, getWidth()-1, getHeight()-1);
    gc.drawRect(0, 0, getWidth()-1, titleSize);
    gc.drawRect(0, 0, getWidth()-1, titleSize + numberDisplaySize);
    
    
    gc.setColor(Color.white);
    gc.fillRect(1, 1, getWidth()-2, titleSize-2);
    gc.fillRect(1, 1+titleSize, getWidth()-2, numberDisplaySize-2);
    
    // dessine le titre
    gc.setColor(Color.black);    
    gc.drawString(title, 3, 15);
    // dessine la valeur
    
    DecimalFormat df = new DecimalFormat(pattern);
    String str = df.format(value);
    gc.drawString(str, 3, 15 + titleSize);
    gc.drawString(unit, 36, 15 + titleSize);
    
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
 
    NumberKnobPanel k = new NumberKnobPanel("Pitch", 100, 1000, "Hz", "0");
    f.add(k);
    
    k = new NumberKnobPanel("Pitch-in", -5, +5, "V", "0.0");
    f.add(k);
    
    k = new NumberKnobPanel("A", 0, 1000, "ms", "0");
    f.add(k);
    
   
  }


}
