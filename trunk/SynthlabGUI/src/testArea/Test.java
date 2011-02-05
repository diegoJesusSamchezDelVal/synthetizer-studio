package testArea;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.component.JSynthResources;
import ui.component.knob.JSynthKnob;


public class Test extends JPanel {
	
	public Test() {
		setMinimumSize(new Dimension(200, 100));
		setPreferredSize(new Dimension(200,100));
		setSize(200, 100);
		setBorder(BorderFactory.createTitledBorder("hello"));
		
		
	}
	
	public void paintComponent(Graphics gc){
		Graphics2D g = (Graphics2D)gc;
		Image i  = Toolkit.getDefaultToolkit().getImage("res/image/knob/knob.png");
		
		g.drawImage(i, 0, 0, this);
	
	}
	
	public static void main(String [] args) {
		JFrame f = new JFrame();
		f.setLayout(new FlowLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400,400);
		f.setVisible(true);
		Test t = new Test();
		f.add(t);
		
	}

}
