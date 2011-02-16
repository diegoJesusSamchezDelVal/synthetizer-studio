package synthlabgui.widgets.configPanel.keyboard;

import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;

import synthlab.api.Port;
import synthlabgui.widgets.configPanel.AbstractConfigPanel;

public class KeyboardPanel extends JPanel implements AbstractConfigPanel,
	KeyboardListener {

    ArrayList<Port> inputPorts = new ArrayList<Port>();

    private static final long serialVersionUID = 7066858859998499895L;

    public KeyboardPanel() {
	Keyboard k = new Keyboard(14);
	k.addKeyboardListener(this);
	add(k);
    }

    @Override
    public void notifyPort(double value) {
	inputPorts.get(0).setValues(value);
	/*
	 * if (!inputPorts.isEmpty()) for (Port p : inputPorts) { if
	 * (p.getValues().getDouble() == -1.0) { p.getValues().putDouble(value);
	 * break; } }
	 */
    }

    public void releasePort(double value) {
	/*
	 * if (!inputPorts.isEmpty()) for (Port p : inputPorts) { if
	 * (p.getValues().getDouble() == value) { p.getValues().putDouble(-1);
	 * break; } }
	 */
    }

    @Override
    public void setPort(Port port) {
	inputPorts.add(0, port);
    }

    public void addPort(Port port) {
	inputPorts.add(port);
    }

    @Override
    public void keyActionPerformed(KeyboardEvent e) {
	if (e.getType() == KeyboardEvent.PRESSED) {
	    notifyPort(e.getValue());
	    System.out.println("Send " + e.getValue());
	} else {
	    releasePort(e.getValue());
	    System.out.println("Remove " + e.getValue());
	}
    }

    @Override
    public void setState(boolean enabled) {
    }

    // Test la fonctionalite de composant. Merci de ne pas supprimer pour
    // instant.
    static public void main(String[] args) {
	JFrame f = new JFrame();
	KeyboardPanel k = new KeyboardPanel();
	f.add(k);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setSize(400, 400);
	f.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
