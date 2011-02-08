package widgets.keyboard;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import synthlab.api.Port;

import widgets.configPanel.AbstractConfigPanel;

public class KeyboardPanel extends JPanel implements AbstractConfigPanel, KeyboardListener {

    Port inputPort;
    private double value;
    
    public KeyboardPanel() {
	
	Keyboard k = new Keyboard(10);
	k.addKeyboardListener(this);
	add(k);
	
    }
    
    @Override
    public void notifyPort(double value) {
	System.out.println("Send " + value);
	if(inputPort != null)
	    inputPort.setValues(value);
    }

    @Override
    public void setPort(Port port) {
	inputPort = port;	
    }

    @Override
    public void keyPressed(KeyboardEvent e) {
	value = e.getValue();
	notifyPort(value);
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
   
      KeyboardPanel k = new KeyboardPanel();
      f.add(k);   
    }

}
