package ui.component.knob;

public class KnobTurnEvent {
	
	private double value;
	
	private JSynthKnob source;
	
	public KnobTurnEvent(JSynthKnob source, double value) {
		this.source = source;
		this.value = value;
	}

	public double getValue() {
		return value;
	}	

	public JSynthKnob getSource() {
		return source;
	}
}
