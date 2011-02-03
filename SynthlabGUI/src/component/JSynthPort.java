package component;

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
	
	/**
	 * Appel� ap�s changement de taille d'un composant module
	 * */
	public void notifyChange() {
		
	}


	
	

}
