package component;

import javax.swing.JPanel;

/**
 * Cette classe hérite de JPanel et représente un port 
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
	
	/**
	 * Appelé apès changement de taille d'un composant module
	 * */
	public void notifyChange() {
		
	}


	
	

}
