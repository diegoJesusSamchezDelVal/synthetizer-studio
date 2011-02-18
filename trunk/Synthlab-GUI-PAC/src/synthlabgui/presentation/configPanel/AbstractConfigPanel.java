package synthlabgui.presentation.configPanel;

import java.util.Observer;

import synthlab.api.Port;

/**
 * Interface des panneaux de configuration. Tous les composants de configuration
 * doivent implementer cette interface.
 * */
public interface AbstractConfigPanel extends Observer {

	/**
	 * Connecte au port qui va configurer.
	 * 
	 * @param port
	 *            le port à configurer
	 * */
	public void setPort(Port port);

	/**
	 * notifie son port une nouvelle valeur. Rien ne se passe si le composant
	 * n'est pas connecté à un port.
	 * 
	 * @param value
	 *            nouvelle valeur
	 * */
	public void notifyPort(double value);

	/**
	 * Active ou désactive le composant de configuration. si le composant est
	 * désactivé, ancune valeur est envoyée à son port.
	 * 
	 * @param enabled
	 *            vrai pour activer et faux pour désactiver
	 * */
	public void setState(boolean enabled);
}
