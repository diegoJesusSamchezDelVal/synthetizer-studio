package widgets.knob;

import synthlab.api.Port;

public interface AbstractKnobPanel {
    
    public void setPort(Port port);
    
    public void notifyPort(double value);
}
