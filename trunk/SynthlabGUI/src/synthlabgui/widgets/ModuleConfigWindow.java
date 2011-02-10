package synthlabgui.widgets;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlabgui.widgets.configPanel.AbstractConfigPanel;
import synthlabgui.widgets.keyboard.KeyboardPanel;
import synthlabgui.widgets.knob.WaveShapeChooserPanel;
import synthlabgui.widgets.knob.NumberKnobPanel;

public class ModuleConfigWindow extends JDialog {
	private static final long serialVersionUID = -2827881534087119283L;

	public static HashMap<Port.ValueUnit, String> unitList = new HashMap<Port.ValueUnit, String>();

	private Module module_;

	private HashMap<String, AbstractConfigPanel> knobList = new HashMap<String, AbstractConfigPanel>();

	private void initUnitList() {
		unitList.put(Port.ValueUnit.AMPLITUDE, "");
		unitList.put(Port.ValueUnit.PERCENTAGE, "%");
		unitList.put(Port.ValueUnit.DECIBELS, "db");
		unitList.put(Port.ValueUnit.HERTZ, "Hz");
		unitList.put(Port.ValueUnit.MILLISECONDS, "ms");
		unitList.put(Port.ValueUnit.VOLT, "v");
	}

	public ModuleConfigWindow(synthlab.api.Module module, JFrame parent,
			Point point) {
		super(parent, true);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		initUnitList();
		module_ = module;
		AbstractConfigPanel knob;
		for (Port p : module_.getInputs()) {
			if (p.getValueType() == Port.ValueType.DISCRETE && p.getValueRange().count == 4) {
			    knob = new WaveShapeChooserPanel(p.getName());
			} else if (p.getValueType() == Port.ValueType.DISCRETE) {
			    double min = p.getValueRange().minimum;
				double max = p.getValueRange().maximum;
			    knob = new NumberKnobPanel(p.getName(), min, max, unitList
					.get(p.getValueUnit()), "0", !p.isLinked(), false);
			} else if (p.getValueType() == Port.ValueType.CONTINUOUS) {
				double min = p.getValueRange().minimum;
				double max = p.getValueRange().maximum;
				knob = new NumberKnobPanel(p.getName(), min, max, unitList
						.get(p.getValueUnit()), "0.0", !p.isLinked(), true);
			} else if (p.getValueType() == Port.ValueType.KEYBOARD) {
				knob = new KeyboardPanel();
			} else {
				knob = null;
			}
			if (knob != null) {
				knob.setPort(p);
				add((JPanel) knob);
				knobList.put(p.getName(), knob);
			}
		}

		if (getContentPane().getComponentCount() == 0) {
			add(new JLabel("No configuration for this module."));
		}

		setLocation(point);
		setResizable(false);
		pack();
	}

	/**
	 * Met a jour et affiche la dialog de configuration à un point donné en
	 * paramètre.
	 * 
	 * @param point
	 *            le point en haut a gauche de la fenetre
	 * */
	public void show(Point point) {
		for (Port p : module_.getInputs()) {
			String name = p.getName();
			AbstractConfigPanel knob = knobList.get(name);
			if (knob != null)
				knob.setState(!p.isLinked());

		}
		setLocation(point);
		setVisible(true);
	}

	public void paintComponent(Graphics gc) {
		gc.setColor(Color.white);
		gc.fillRect(0, 0, getWidth(), getHeight());
	}
}
