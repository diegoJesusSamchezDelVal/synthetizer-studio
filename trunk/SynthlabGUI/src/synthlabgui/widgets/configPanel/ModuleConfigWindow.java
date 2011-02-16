package synthlabgui.widgets.configPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
	super();
	setModal(false);
	setAlwaysOnTop(true);
	setLayout(new BorderLayout());
	JPanel contentPanel = new JPanel(
		new FlowLayout(FlowLayout.CENTER, 0, 0));
	add(contentPanel, BorderLayout.CENTER);

	setUndecorated(true);
	setDefaultLookAndFeelDecorated(false);

	initUnitList();

	module_ = module;
	AbstractConfigPanel knob;
	KeyboardPanel keyboard = null;
	for (Port p : module_.getInputs()) {
	    knob = createPanel(p);
	    if (knob != null) {
		if (knob instanceof KeyboardPanel) {
		    if (keyboard == null) {
			keyboard = (KeyboardPanel) knob;
			knobList.put(p.getName(), keyboard);
		    }
		    keyboard.addPort(p);
		} else {
		    knob.setPort(p);
		    contentPanel.add((JPanel) knob);
		    knobList.put(p.getName(), knob);
		}
	    }
	}
	if (keyboard != null) {
	    contentPanel.add(keyboard);
	}
	if (getContentPane().getComponentCount() == 0) {
	    contentPanel.add(new JLabel("No configuration for this module."));
	}
	setLocation(point);
	setResizable(false);
	pack();

	InternalMouseListener listener = new InternalMouseListener();
	addMouseListener(listener);
	addMouseMotionListener(listener);

	addTitleBar(listener);
    }

    private void addTitleBar(InternalMouseListener mouseListener) {
	JPanel titleBar = new JPanel(null);
	Dimension dim = new Dimension(getWidth(), 20);
	titleBar.setMinimumSize(dim);
	titleBar.setPreferredSize(dim);
	titleBar.setSize(dim);

	titleBar.addMouseListener(mouseListener);
	titleBar.addMouseMotionListener(mouseListener);

	add(titleBar, BorderLayout.NORTH);

	JLabel title = new JLabel(module_.getName());
	title.setLocation(getWidth() / 2 - 10, 4);
	title.setVisible(true);
	title.setBackground(Color.black);
	titleBar.add(title);

	CloseButton cb = new CloseButton();
	cb.setLocation(getWidth() - cb.getWidth() - 6, 4);
	titleBar.add(cb);

	setSize(getWidth(), getHeight() + titleBar.getHeight() - 20);
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
    private static void initUnitList() {
	unitList.put(Port.ValueUnit.AMPLITUDE, "");
	unitList.put(Port.ValueUnit.PERCENTAGE, "%");
	unitList.put(Port.ValueUnit.DECIBELS, "db");
	unitList.put(Port.ValueUnit.HERTZ, "Hz");
	unitList.put(Port.ValueUnit.MILLISECONDS, "ms");
	unitList.put(Port.ValueUnit.VOLT, "v");
    }

    public void refresh() {
	for (Port p : module_.getInputs()) {
	    String name = p.getName();
	    AbstractConfigPanel knob = knobList.get(name);
	    if (knob != null)
		knob.setState(!p.isLinked());
	}
	repaint();
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

    public void unshow() {
	setVisible(false);
    }

    private class CloseButton extends JPanel implements MouseListener,
	    MouseMotionListener {
	/**
		 * 
		 */
	private static final long serialVersionUID = -7952186173963083283L;

	private boolean mouseOver = false;

	private boolean mousePressed = false;

	private Color normalColor = Color.black;

	private Color mouseOverColor = Color.red;

	private int scale = 1;

	public CloseButton() {
	    Dimension dim = new Dimension(8, 8);
	    setMinimumSize(dim);
	    setMaximumSize(dim);
	    setSize(dim);
	    setLayout(null);
	    setOpaque(false);
	    setVisible(true);
	    addMouseListener(this);
	    addMouseMotionListener(this);
	}

	public void paint(Graphics gc) {
	    // Enable anti-aliasing
	    RenderingHints renderHints = new RenderingHints(
		    RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    renderHints.put(RenderingHints.KEY_RENDERING,
		    RenderingHints.VALUE_RENDER_QUALITY);
	    ((Graphics2D) gc).setRenderingHints(renderHints);
	    if (mouseOver)
		gc.setColor(mouseOverColor);
	    else
		gc.setColor(normalColor);
	    if (!mousePressed) {
		gc.drawLine(0, 0, getWidth() - 1, getHeight() - 1);
		gc.drawLine(0, getHeight() - 1, getWidth() - 1, 0);
	    } else {
		gc.drawLine(0 + scale, 0 + scale, getWidth() - 1 - scale,
			getHeight() - 1 - scale);
		gc.drawLine(0 + scale, getHeight() - 1 - scale, getWidth()
			- scale - 1, 0 + scale);
	    }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	    mouseOver = true;
	    repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
	    mouseOver = false;
	    repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    mousePressed = true;
	    repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    mousePressed = false;
	    repaint();
	    if (this.contains(e.getPoint()))
		unshow();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
    }

    /**
     * Ecouteur interne pour déplacment de dialog
     * */
    private class InternalMouseListener implements MouseListener,
	    MouseMotionListener {
	Point start;

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    start = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	    int x = getLocation().x;
	    int y = getLocation().y;
	    setLocation(x + e.getPoint().x - start.x, y + e.getPoint().y
		    - start.y);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
    }
}
