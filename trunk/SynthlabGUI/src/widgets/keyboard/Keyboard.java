package widgets.keyboard;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import widgets.knob.FunctionKnob;



public class Keyboard extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private float[] frequences = {261.6f, // DO
      293.7f,  // RE
      329.7f,  // MI
      349.2f,  // FA
      392.0f,  // SOL
      440.0f,  // LA
      493.9f,  // SI
      523.2f,  // DO
      587.4f,  // RE
      659.4f}; // MI
  
  public Keyboard(int nbKeys) {
      setLayout(null);
      setBackground(Color.black);
      
      WhiteKey whiteKey = new WhiteKey();
      BlackKey blackKey = new BlackKey();
      int width = whiteKey.getWidth() * nbKeys;
      int height = whiteKey.getHeight();
      setSize(width, height);
      setPreferredSize(getSize());
      int decx = 0;
      int decy = 0;
      
      ArrayList<Key> whiteKeys = new ArrayList<Key>();
      ArrayList<Key> blackKeys = new ArrayList<Key>();
      
      for(int i = 0; i < nbKeys; i++) {
          WhiteKey key = new WhiteKey();
          key.addMouseListener(new KeyboardListener());
          key.setLocation(i*whiteKey.getWidth()+decx, decy);
          whiteKeys.add(key);
          add(key);
      }
      
      int decalBlack = decx + whiteKey.getWidth() - blackKey.getWidth()/2;
      
      
      for(int i = 0; i < nbKeys; i++) {
	  if (i%7==0 || i%7==1 || i%7==3 || i%7==4 || i%7==5 ){
		BlackKey key = new BlackKey();
		key.addMouseListener(new KeyboardListener());
		key.setLocation(decalBlack + whiteKey.getWidth()*i, decy);
		blackKeys.add(key);
		add(key,0);
	}

      }     
  }
  
  public void paint(Graphics gc) {
      super.paint(gc);
      requestFocus(true);
  }
  
  private class KeyboardListener implements MouseListener {

      private Color keyPressedColor = Color.lightGray;

    @Override
      public void mouseClicked(MouseEvent arg0) {
	  
      }
  
      @Override
      public void mouseEntered(MouseEvent arg0)
      {
        // TODO Auto-generated method stub
        
      }
  
      @Override
      public void mouseExited(MouseEvent arg0)
      {
        // TODO Auto-generated method stub
        
      }
  
      @Override
      public void mousePressed(MouseEvent e)
      {
	  ((JPanel)e.getSource()).setBackground(keyPressedColor);
	  repaint();
      }
  
      @Override
      public void mouseReleased(MouseEvent e)
      {
	  Key key = (Key) e.getSource();
	  ((JPanel)e.getSource()).setBackground(key.getColor());
	  repaint();
        
      }
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
 
    Keyboard k = new Keyboard(10);
    f.add(k);   
  }

}
