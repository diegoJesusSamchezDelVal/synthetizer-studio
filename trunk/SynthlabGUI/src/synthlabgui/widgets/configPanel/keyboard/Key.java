package synthlabgui.widgets.configPanel.keyboard;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Key extends JPanel {

    private static final long serialVersionUID = -1955536235297058856L;

    private int keyCode;

    public Key() {
	setLayout(null);
	setOpaque(true);
	setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Color getColor() {
	return Color.WHITE;
    }

    public void bindKeyCode(int keyCode) {
	this.keyCode = keyCode;
    }

    public int getKeyCode() {
	return keyCode;
    }

}
