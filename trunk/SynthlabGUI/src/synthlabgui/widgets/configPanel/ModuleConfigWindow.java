package synthlabgui.widgets.configPanel;

import java.awt.FlowLayout;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlabgui.widgets.configPanel.keyboard.KeyboardPanel;
import synthlabgui.widgets.configPanel.knob.numberKnob.NumberKnobPanel;
import synthlabgui.widgets.configPanel.knob.waveChooser.WaveShapeChooserPanel;

/**
 * Un dialog contenant tous les composants de configuration pour un module donné
 * en paramètre.
 * */
public class ModuleConfigWindow extends JDialog {

	private static final long serialVersionUID = -2827881534087119283L;

	/**
	 * hashmap cotenant tous les string des noms des unité de mesure. *
	 * 
	 * @see Port.ValueUnit
	 * */
	public static HashMap<Port.ValueUnit, String> unitList = new HashMap<Port.ValueUnit, String>();

	/**
	 * model abstract de module
	 * 
	 * @see Module
	 * */
	private Module module_;

	/**
	 * Une liste contenant tous les panneaux de configuration
	 * */
	private HashMap<String, AbstractConfigPanel> knobList = new HashMap<String, AbstractConfigPanel>();

	/**
	 * Crée une fenêtre de configuration pour un module.
	 * 
	 * @param module
	 *            Le module abstrait
	 * 
	 * @param parent
	 *            Son fenêtre parent, normalement la fenetre principale
	 * 
	 * @param point
	 *            Location de la fenetre
	 * */
	public ModuleConfigWindow(synthlab.api.Module module, JFrame parent,
			Point point) {
		super(parent, true);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		setTitle(module.getName());
		initUnitList();
		module_ = module;
		AbstractConfigPanel knob;
		for (Port p : module_.getInputs()) {
			knob = createPanel(p);
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
	 * Cette méthode vérifie le type du port et crée un panneau de configuration
	 * correspondant.
	 * 
	 * @param port
	 *            le port à configurer
	 * */
	private AbstractConfigPanel createPanel(Port port) {
		AbstractConfigPanel knob;
		if (port.getValueType() == Port.ValueType.DISCRETE
				&& port.getValueRange().count == 4) {
			knob = new WaveShapeChooserPanel(port.getName());
		} else if (port.getValueType() == Port.ValueType.DISCRETE) {
			double min = port.getValueRange().minimum;
			double max = port.getValueRange().maximum;
			knob = new NumberKnobPanel(port.getName(), min, max, unitList
					.get(port.getValueUnit()), "0", !port.isLinked(), false);
		} else if (port.getValueType() == Port.ValueType.CONTINUOUS) {
			double min = port.getValueRange().minimum;
			double max = port.getValueRange().maximum;
			knob = new NumberKnobPanel(port.getName(), min, max, unitList
					.get(port.getValueUnit()), "0.0", !port.isLinked(), true);
		} else if (port.getValueType() == Port.ValueType.KEYBOARD) {
			knob = new KeyboardPanel();
		} else {
			knob = null;
		}
		return knob;
	}

	/**
	 * Initialisation de tous les unités de mesure
	 * 
	 * @see Port.ValueUnit
	 * */
	private void initUnitList() {
		unitList.put(Port.ValueUnit.AMPLITUDE, "");
		unitList.put(Port.ValueUnit.PERCENTAGE, "%");
		unitList.put(Port.ValueUnit.DECIBELS, "db");
		unitList.put(Port.ValueUnit.HERTZ, "Hz");
		unitList.put(Port.ValueUnit.MILLISECONDS, "ms");
		unitList.put(Port.ValueUnit.VOLT, "v");
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

}
