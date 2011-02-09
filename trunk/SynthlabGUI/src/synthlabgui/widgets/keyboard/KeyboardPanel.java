package synthlabgui.widgets.keyboard;

import javax.swing.JPanel;

import synthlab.api.Port;
import synthlabgui.widgets.configPanel.AbstractConfigPanel;

public class KeyboardPanel extends JPanel implements AbstractConfigPanel,
	KeyboardListener {
    public KeyboardPanel() {
	Keyboard k = new Keyboard(10);
	k.addKeyboardListener(this);
	add(k);
    }

    @Override
    public void notifyPort(double value) {
	System.out.println("Send " + value);
	if (inputPort != null)
	    inputPort.setValues(value);
    }

    @Override
    public void setPort(Port port) {
	inputPort = port;
    }

    @Override
    public void keyPressed(KeyboardEvent e) {
	value = e.getValue();
	notifyPort(value);
    }

    private static final long serialVersionUID = 7066858859998499895L;

    Port inputPort;

    private double value;

    @Override
    public void setState(boolean enabled) {
    }

}
