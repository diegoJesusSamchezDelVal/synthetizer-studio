package synthlabgui.widgets.configPanel.knob;

/**
 * Un événement de règlage
 * */
public class KnobEvent {
	public KnobEvent(AbstractKnob knob, int value) {
		this.value = value;
		// this.source = source;
	}

	public int getValue() {
		return value;
	}

	private int value;
	// private NumberKnob source;
}
