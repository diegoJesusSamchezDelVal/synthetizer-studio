package component.knob;

import java.awt.Dimension;

import javax.swing.JPanel;

public class JSynthKnob extends JPanel {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4151030748040607370L;

	/**
	 * Constructeur par défault
	 * */
	public JSynthKnob() {
	
		Dimension dim = new Dimension(30,30);
		
		setOpaque(false);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setSize(dim);
	}
}
