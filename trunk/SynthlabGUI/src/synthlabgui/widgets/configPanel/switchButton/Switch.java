package synthlabgui.widgets.configPanel.switchButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import synthlab.api.Port;
import synthlabgui.widgets.configPanel.AbstractConfigPanel;
import synthlabgui.widgets.configPanel.knob.AbstractKnob;
import synthlabgui.widgets.configPanel.knob.numberKnob.NumberKnobPanel;

public class Switch extends JPanel implements AbstractConfigPanel,
	ActionListener {

    private Port inputPort;

    private double value;

    private String title;

    private String str = "";

    private String enabledTitle;

    private String disabledTitle;

    private JToggleButton button;

    private int gapX = 25;
    private int gapY = 18;

    protected Dimension size = new Dimension(AbstractKnob.size.width + 35 + 2,
	    20 + 2 + AbstractKnob.size.height + NumberKnobPanel.TITLE_HEIGHT
		    + NumberKnobPanel.numberDisplaySize);

    public Switch(String title, String enabledTitle, String disabledTitle) {

	this.title = title;
	this.enabledTitle = enabledTitle;
	this.disabledTitle = disabledTitle;

	setLayout(null);

	setSize(size);
	setPreferredSize(size);
	setMinimumSize(size);
	setMaximumSize(size);

	button = new JToggleButton();
	button.setBounds(gapX, gapY, getWidth() - gapX - gapX, 60 - gapY);
	turnSwitch(false);
	button.addActionListener(this);
	add(button);

    }

    public void turnSwitch(boolean on) {
	button.setSelected(on);
	if (on)
	    str = enabledTitle;
	else
	    str = disabledTitle;
    }

    public void paint(Graphics gc) {
	super.paint(gc);
	RenderingHints renderHints = new RenderingHints(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	renderHints.put(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
	((Graphics2D) gc).setRenderingHints(renderHints);

	gc.setColor(Color.black);

	// dessine le titre
	gc.setColor(Color.darkGray);
	// 100 - (g.getFontMetrics().stringWidth(module_.getName()) / 2;
	gc.setFont(new Font("Arial", Font.PLAIN, 11));
	gc.drawString(title, (int) ((getWidth() - gc.getFontMetrics()
		.stringWidth(title)) / 2.) - 2, 10);

	// value
	gc.setColor(Color.gray);
	gc.setFont(new Font("Arial", Font.PLAIN, 10));

	gc.drawString(str,
		(getWidth() - gc.getFontMetrics().stringWidth(str)) / 2, 75);

    }

    @Override
    public void notifyPort(double value) {
	if (inputPort != null && !inputPort.isLinked()) {
	    inputPort.setValues(value);
	}
    }

    @Override
    public void setPort(Port port) {
	inputPort = port;
	inputPort.addObserver(this);
    }

    @Override
    public void setState(boolean enabled) {
	button.setEnabled(enabled);
    }

    @Override
    public void update(Observable o, Object arg) {
	value = ((Port) o).getValues().getDouble(0);
	if (value > 0)
	    turnSwitch(true);
	else
	    turnSwitch(false);
	repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (button.isSelected()) {
	    value = 1;
	    str = enabledTitle;
	} else {
	    value = 0;
	    str = disabledTitle;
	}
	repaint();
	notifyPort(value);

    }
}
