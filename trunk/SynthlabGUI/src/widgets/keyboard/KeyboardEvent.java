package widgets.keyboard;



public class KeyboardEvent {
    
    private double value;
    
    private Key source;
    
    public KeyboardEvent(Key key, double value) {
	source = key;
	this.value = value;
    }
    
    public double getValue() {
	return value;
    }

}
