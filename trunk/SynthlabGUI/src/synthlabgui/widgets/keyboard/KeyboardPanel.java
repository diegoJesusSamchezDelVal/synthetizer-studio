package synthlabgui.widgets.keyboard;

import javax.swing.JFrame;
import javax.swing.JPanel;

import synthlab.api.Port;
import synthlabgui.widgets.configPanel.AbstractConfigPanel;

public class KeyboardPanel extends JPanel implements AbstractConfigPanel,
		KeyboardListener {
	public KeyboardPanel() {
		Keyboard k = new Keyboard(14);
		k.addKeyboardListener(this);
		add(k);
	}

	@Override
	public void notifyPort(double value) {
		if (inputPort != null)
			inputPort.setValues(value);
	}

	@Override
	public void setPort(Port port) {
		inputPort = port;
		notifyPort(-1);
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

}
