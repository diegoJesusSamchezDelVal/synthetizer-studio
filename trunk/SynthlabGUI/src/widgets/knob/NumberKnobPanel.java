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

import synthlab.api.Port;

/**
 * Panneau de règlage avec un afficheur
 * */
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
  
  private int numberDisplaySize = 20;
  
  public static int TITLE_HEIGHT = 20;
  
  private Dimension size = new Dimension(AbstractKnob.size.width + 20 + 2, 20 + 2 + AbstractKnob.size.height + TITLE_HEIGHT + numberDisplaySize);

  private String title;
  
  private boolean enabled = true;
  
  private Port inputPort;
  
  /**
   * @param min valeur minimal
   * @param max valeur maximal
   * @param unit unité 
   * @param pattern pattern des chiffres 
   * */
  public NumberKnobPanel(String title, double min, double max, String unit, String pattern, boolean enable) {
    this.title = title;
    maxValue = max;
    minValue = min;     
    this.unit = unit;
    this.pattern = pattern;
    enabled = enable;
    
    setLayout(null);   
    
    setMinimumSize(size);
    setPreferredSize(size);
    setSize(size);    
    
    knob = new NumberKnob();
    knob.addKnobListener(this);
    add(knob);
    knob.setLocation((size.width-AbstractKnob.size.width) /2, numberDisplaySize + TITLE_HEIGHT + 11);
    
    value = computeValue(knob.value);
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
        gc.drawRect(0, 0, getWidth()-1, TITLE_HEIGHT);
        gc.drawRect(0, 0, getWidth()-1, TITLE_HEIGHT + numberDisplaySize);
        
        
        if (enabled)
          gc.setColor(Color.white);
        else 
          gc.setColor(Color.lightGray);
        gc.fillRect(1, 1, getWidth()-2, TITLE_HEIGHT-2);
        gc.fillRect(1, 1+TITLE_HEIGHT, getWidth()-2, numberDisplaySize-2);
        
        // dessine le titre
        gc.setColor(Color.black);    
        gc.drawString(title, 3, 15);
        
        // dessine la valeur
        DecimalFormat df = new DecimalFormat(pattern);
        String str = df.format(value);
        gc.drawString(str, 3, 15 + TITLE_HEIGHT);
        gc.drawString(unit, 36, 15 + TITLE_HEIGHT);    
  } 
  
  @Override
  public void knobTurned(KnobEvent e)
  {
        // 0 .. 9999
        int scale = e.getValue();        
        value = computeValue(scale);
        notifyPort(value);
        repaint(0,0,getWidth(), 20);
  }
  
  private double computeValue(int rawValue) {
      double piece = (maxValue - minValue) / 10000;
      return minValue + piece * rawValue;
  }
  
  /**
   * Envoie la valeur à son port.</br>
   * Faite rien si le port est null ou occupé par une câble.  
   * @param value la valeur à envoyer
   * */
  private void notifyPort(double value)
  {
      if(inputPort != null && !inputPort.isLinked()) {
	  inputPort.setValues(value);
	  
	  DecimalFormat df = new DecimalFormat("0.00");	  
	  System.out.println("Send " + df.format(value) + " to " + inputPort.getName());
      }
  }
  
  public void setPort(Port port){
      inputPort = port;
  }

  public void setState(boolean enabled){
      this.enabled = enabled;
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
 
    NumberKnobPanel k = new NumberKnobPanel("Pitch", 100, 1000, "Hz", "0", true);
    f.add(k);
    
    k = new NumberKnobPanel("Pitch-in", -5, +5, "V", "0.0", false);
    f.add(k);
    
    k = new NumberKnobPanel("A", 0, 1000, "ms", "0", true);
    f.add(k);
    
   
  }


}
