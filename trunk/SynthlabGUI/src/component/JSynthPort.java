package component;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Cette classe h�rite de JPanel et repr�sente un port 
 * */
public class JSynthPort extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1785517245371722444L;
	
	private JSynthCable cable = null;
	
	private JSynthModule parent;
	
	public JSynthPort(JSynthModule parent) {
		this.parent = parent;	
	
	}
	
	public void paint(Graphics gc) {
		ImageIcon img = new ImageIcon("res/image/port.png");
		gc.drawImage(img.getImage(), 0, 0, null);		
	}
	
	/**
	 * Appel� ap�s changement de taille d'un composant module
	 * */
	public void notifyChange() {
		
	}


	
	

}
