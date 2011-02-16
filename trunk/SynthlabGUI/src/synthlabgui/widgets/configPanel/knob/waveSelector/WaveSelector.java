package synthlabgui.widgets.configPanel.knob.waveSelector;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;

import synthlabgui.widgets.configPanel.knob.numberKnob.NumberKnobPanel;

public class WaveSelector extends NumberKnobPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1151583474514288158L;

    public WaveSelector(String title, double min, double max, boolean enable) {
	super(title, min, max, "", "", enable, true);
	initName();
    }

    private static HashMap<Integer, String> waveShapeNames = new HashMap<Integer, String>();

    private static void initName() {
	waveShapeNames.put(0, "PULSE");
	waveShapeNames.put(1, "SINE");
	waveShapeNames.put(2, "TRIANGLE");
	waveShapeNames.put(3, "SAWTOOTH");
    }

    private int computeValue() {
	return ((int) (value * 4));
    }

    protected void paintValue(Graphics gc) {
	// dessine la valeur
	String str = "";
	str += waveShapeNames.get(computeValue());
	gc.setColor(Color.gray);
	gc.setFont(new Font("Arial", Font.PLAIN, 10));
	gc.drawString(str,
		(getWidth() - gc.getFontMetrics().stringWidth(str)) / 2, 75);

    }
}
