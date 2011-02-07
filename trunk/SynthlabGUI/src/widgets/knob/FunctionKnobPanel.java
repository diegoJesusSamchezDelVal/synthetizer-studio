package widgets.knob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FunctionKnobPanel extends JPanel implements KnobListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private int value;

  /**
   * icone de l'ouest
   * */
  private ImageIcon icon0;

  /**
   * icone du north
   * */
  private ImageIcon icon1;

  /**
   * icone de l'est
   * */
  private ImageIcon icon2;

  /**
   * icone du sud
   * */
  private ImageIcon icon3;

  private FunctionKnob knob;
  
  public FunctionKnobPanel(ImageIcon icon0, ImageIcon icon1,ImageIcon icon2,ImageIcon icon3) {
    this.icon0 = icon0;
    this.icon1 = icon1;
    this.icon2 = icon2;
    this.icon3 = icon3;
    
    setLayout(null);
    
    Dimension dim = new Dimension(82,82);
    setMinimumSize(dim);
    setPreferredSize(dim);
    setSize(dim);
    
    
    knob = new FunctionKnob();
    knob.addKnobListener(this);
    add(knob);
    knob.setLocation(11, 11);
    
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
    
    
    gc.setColor(Color.white);
    gc.fillRect(1, 1, getWidth()-2, getHeight()-2);
    
    
    
  } 
  
  @Override
  public void knobTurned(KnobEvent e)
  {
    value = e.getValue();    
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
 
    FunctionKnobPanel k = new FunctionKnobPanel(null, null, null, null);
    f.add(k);
    
    
    
   
  }

}
