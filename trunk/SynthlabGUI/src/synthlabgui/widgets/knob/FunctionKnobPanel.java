package synthlabgui.widgets.knob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import synthlab.api.Port;
import synthlabgui.widgets.configPanel.AbstractConfigPanel;

public class FunctionKnobPanel extends JPanel implements KnobListener,
	AbstractConfigPanel {
    public FunctionKnobPanel(String title) {
	this(title, null, null, null, null);
	icon0 = new ImageIcon("res/image/knob/0.png");
	icon1 = new ImageIcon("res/image/knob/1.png");
	icon2 = new ImageIcon("res/image/knob/2.png");
	icon3 = new ImageIcon("res/image/knob/3.png");
    }

    public FunctionKnobPanel(String title, ImageIcon icon0, ImageIcon icon1,
	    ImageIcon icon2, ImageIcon icon3) {
	this.title = title;
	setLayout(null);
	setMinimumSize(dim);
	setPreferredSize(dim);
	setSize(dim);
	knob = new FunctionKnob();
	knob.addKnobListener(this);
	add(knob);
	knob.setLocation(imgSize, imgSize + NumberKnobPanel.TITLE_HEIGHT);
	knob.value = 1;
	this.icon0 = icon0;
	this.icon1 = icon1;
	this.icon2 = icon2;
	this.icon3 = icon3;
    }

    public void paintComponent(Graphics gc) {
	super.paintComponent(gc);
	RenderingHints renderHints = new RenderingHints(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	renderHints.put(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
	((Graphics2D) gc).setRenderingHints(renderHints);
	gc.setColor(Color.black);
	gc.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	gc.setColor(Color.white);
	gc.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
	gc.setColor(Color.black);
	gc.drawRect(0, 0, getWidth() - 1, TITLE_HEIGHT);
	gc.setColor(Color.black);
	gc.drawString(title, 10, 15);
	gc.drawImage(icon0.getImage(), 2, TITLE_HEIGHT + imgSize
		+ (AbstractKnob.size.height / 2) - 5, null);
	gc.drawImage(icon1.getImage(), getWidth() / 2 - 5, TITLE_HEIGHT + 2,
		null);
	gc.drawImage(icon2.getImage(), getWidth() - imgSize - 2, TITLE_HEIGHT
		+ imgSize + (AbstractKnob.size.height / 2) - 5, null);
	gc.drawImage(icon3.getImage(), getWidth() / 2 - 7, TITLE_HEIGHT
		+ imgSize + (AbstractKnob.size.height), null);
    }

    @Override
    public void setPort(Port p) {
	inputPort = p;
    }

    @Override
    public void notifyPort(double value) {
	if (inputPort != null && !inputPort.isLinked()) {
	    inputPort.setValues(value);
	    // DecimalFormat df = new DecimalFormat("0.00");
	}
    }

    @Override
    public void knobTurned(KnobEvent e) {
	value = e.getValue();
	notifyPort(value);
    }

    private static final long serialVersionUID = 1L;

    private int value;

    private Port inputPort;

    private ImageIcon icon0;

    private ImageIcon icon1;

    private ImageIcon icon2;

    private ImageIcon icon3;

    private FunctionKnob knob;

    private String title;

    private int imgSize = 15;

    public static int TITLE_HEIGHT = 20;

    private Dimension dim = new Dimension((imgSize * 2)
	    + AbstractKnob.size.width + 2, NumberKnobPanel.TITLE_HEIGHT
	    + (imgSize * 2) + AbstractKnob.size.height + 2);

    @Override
    public void setState(boolean enabled) {

    }
}
