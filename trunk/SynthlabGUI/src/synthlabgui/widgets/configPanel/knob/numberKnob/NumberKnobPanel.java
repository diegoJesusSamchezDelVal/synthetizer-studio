package synthlabgui.widgets.configPanel.knob.numberKnob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.Observable;

import javax.swing.JPanel;

import synthlab.api.Port;
import synthlabgui.widgets.configPanel.AbstractConfigPanel;
import synthlabgui.widgets.configPanel.knob.AbstractKnob;
import synthlabgui.widgets.configPanel.knob.KnobEvent;
import synthlabgui.widgets.configPanel.knob.KnobListener;

public class NumberKnobPanel extends JPanel implements KnobListener,
	AbstractConfigPanel {
    /**
     * @param title
     *            le titre de panneau
     * 
     * @param min
     *            valeur minimale
     * @param max
     *            valeur maximale
     * @param unit
     *            l'unité de mesure à afficher
     * @param pattern
     *            pattern des chiffres
     * 
     * @param enable
     *            vrai si ce panneau est active et dèsative sinon
     * @param continuous
     *            vrai pour générer les valeurs continus et discrètes sison
     * */
    public NumberKnobPanel(String title, double min, double max, String unit,
	    String pattern, boolean enable, boolean continuous) {
	this.title = title;
	maxValue = max;
	minValue = min;
	this.unit = unit;
	this.pattern = pattern;
	enabled = enable;
	this.continous = continuous;
	setLayout(null);
	setMinimumSize(size);
	setPreferredSize(size);
	setSize(size);
	knob = new NumberKnob();
	knob.setActive(enable);
	knob.addKnobListener(this);
	add(knob);
	knob.setLocation((size.width - AbstractKnob.size.width) / 2,
		TITLE_HEIGHT + 5);
	value = computeValue(knob.getValue());
    }

    public void paint(Graphics gc) {
	super.paint(gc);
	RenderingHints renderHints = new RenderingHints(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	renderHints.put(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
	((Graphics2D) gc).setRenderingHints(renderHints);
	// dessine le titre
	gc.setColor(Color.darkGray);
	// 100 - (g.getFontMetrics().stringWidth(module_.getName()) / 2;
	gc.setFont(new Font("Arial", Font.PLAIN, 11));
	gc.drawString(title, (int) ((getWidth() - gc.getFontMetrics()
		.stringWidth(title)) / 2.) - 2, 10);
	// dessine la valeur
	DecimalFormat df = new DecimalFormat(pattern);
	String str = df.format(value) + " " + unit;
	gc.setColor(Color.gray);
	gc.setFont(new Font("Arial", Font.PLAIN, 10));
	gc.drawString(str,
		(getWidth() - gc.getFontMetrics().stringWidth(str)) / 2, 75);

    }

    @Override
    public void knobTurned(KnobEvent e) {
	// 0 .. 9999
	int scale = e.getValue();
	value = computeValue(scale);
	notifyPort(value);
	repaint(0, 0, getWidth(), 20);
    }

    private double computeValue(int rawValue) {
	if (continous) {
	    double piece = (maxValue - minValue) / 10000;
	    return minValue + piece * rawValue;
	} else {
	    int piece = (int) (10000.0 / (maxValue - minValue));
	    return rawValue / piece;
	}
    }

    public void notifyPort(double value) {
	if (inputPort != null && !inputPort.isLinked()) {
	    inputPort.setValues(value);
	    // System.out.println("Send " + value);
	    // DecimalFormat df = new DecimalFormat("0.00");
	}
    }

    public void setPort(Port port) {
	inputPort = port;
	if (port != null) {
	    port.addObserver(this);
	    update(port, null);
	}
    }

    public void setState(boolean enabled) {
	this.enabled = enabled;
    }

    private static final long serialVersionUID = 1L;

    private double value;

    private double maxValue;

    private double minValue;

    private String unit;

    private String pattern;

    private NumberKnob knob;

    private int numberDisplaySize = 20;

    public static int TITLE_HEIGHT = 10;

    private Dimension size = new Dimension(AbstractKnob.size.width + 35 + 2, 20
	    + 2 + AbstractKnob.size.height + TITLE_HEIGHT + numberDisplaySize);

    private String title;

    private boolean enabled = true;

    private boolean continous = true;

    private Port inputPort;

    @Override
    public void update(Observable o, Object arg) {
	value = ((Port) o).getValues().getDouble(0);
    }
}
