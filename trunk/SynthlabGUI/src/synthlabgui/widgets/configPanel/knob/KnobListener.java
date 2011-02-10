package synthlabgui.widgets.configPanel.knob;

/**
 * Interface pour des écouteurs de règlage
 * */
public interface KnobListener {

	/**
	 * @param event
	 *            événement contenant la valeur de règlage
	 * */
	public void knobTurned(KnobEvent event);

}
