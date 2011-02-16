package synthlabgui.widgets.configPanel.knob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * La classe abstraite d'un règlage
 * */
public abstract class AbstractKnob extends JPanel implements MouseListener,
	MouseMotionListener {
    /**
     * Constructeur interne
     * */
    protected AbstractKnob() {
	setMinimumSize(size);
	setPreferredSize(size);
	setSize(size);
	setOpaque(true);
	centerPoint = new Point(size.width / 2, size.height / 2);
	pointer = new Point(size.width / 2, 0);
	addMouseListener(this);
	addMouseMotionListener(this);
	active_ = true;
    }

    public void setActive(boolean active) {
	active_ = active;

    }

    public void addKnobListener(KnobListener l) {
	listeners.add(l);
    }

    public void removeKnobListener(KnobListener l) {
	listeners.remove(l);
    }

    /**
     * Notifie ses écouteurs sa valeur actuelle.
     * */
    protected void notifyListener() {
	for (KnobListener listener : listeners) {
	    KnobEvent e = new KnobEvent(this, value);
	    listener.knobTurned(e);
	}
    }

    public void paint(Graphics gc) {
	Graphics2D g2 = (Graphics2D) gc;
	// Enable anti-aliasing
	RenderingHints renderHints = new RenderingHints(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	renderHints.put(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
	((Graphics2D) gc).setRenderingHints(renderHints);
	// jauge
	if (active_)
	    g2.setColor(new Color(150, 200, 150));
	else
	    g2.setColor(new Color(150, 150, 150));
	g2.fillArc(2, 2, 47, 47,
		(int) (((5. * Math.PI) / 4.) / (2. * Math.PI) * 360.),
		-(int) ((value / 10000.) * ((6. * Math.PI) / 4.)
			/ (2. * Math.PI) * 360.));
	// ruler outer
	g2.setColor(new Color(240, 240, 240));
	g2.drawArc(2, 2, 47, 47,
		(int) (((7. * Math.PI) / 4.) / (2. * Math.PI) * 360.),
		(int) (((6. * Math.PI) / 4.) / (2. * Math.PI) * 360.));
	// ruler lines
	for (double i = -2; i <= 10; ++i)
	    g2.drawLine(25, 25, 25 + (int) (Math.cos(i * Math.PI / 8.) * 24.),
		    25 - (int) (Math.sin(i * Math.PI / 8.) * 24.));
	// Knob inner
	GradientPaint innerGradient = new GradientPaint(new Point(0, 0),
		new Color(220, 220, 220), new Point(50, 50), new Color(180,
			180, 180));
	g2.setPaint(innerGradient);
	g2.fillOval(5, 5, 40, 40);
	// Knob outer
	GradientPaint outerGradient = new GradientPaint(new Point(0, 0),
		new Color(120, 120, 120), new Point(50, 50), new Color(80, 80,
			80));
	g2.setPaint(outerGradient);
	g2.drawOval(5, 5, 40, 40);
	// rotation
	g2.rotate((value / 10000. * 6. * (Math.PI / 4.)) + 5. * (Math.PI / 4.),
		25, 25);
	// selector inner
	GradientPaint innerSelectorGradient = new GradientPaint(
		new Point(0, 0), new Color(150, 150, 150), new Point(20, 0),
		new Color(250, 250, 250));
	g2.setPaint(innerSelectorGradient);
	g2.fillOval(15, 5, 20, 10);
	// selector outer
	GradientPaint outerSelectorGradient = new GradientPaint(
		new Point(0, 0), new Color(160, 160, 160), new Point(50, 50),
		new Color(120, 120, 120));
	g2.setPaint(outerSelectorGradient);
	g2.drawOval(15, 5, 20, 10);
	// selector dash
	g2.drawLine(25, 5, 25, 8);
    }

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
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	if (active_) {
	    repaint();
	    pointer = computePointer(e.getPoint());
	    notifyListener();
	}
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Calule la position de règlage par rapport de la position de la souris.
     * 
     * @param point
     *            La position de la souris
     * @return Le point de la position de règlage
     * */
    protected abstract Point computePointer(Point point);

    protected abstract Point computePointer(int value);

    /**
     * Calcule la valeur interne en fonction de la position de règlage actuelle.
     * 
     * @param k
     *            coefficient angulaire de la droite
     * @param currentPoint
     *            la position du règlage
     * */
    protected abstract int computeValue(double k, Point currentPoint);

    private static final long serialVersionUID = -5381272966044226174L;

    private boolean active_;

    /**
     * Valeur maximale
     * */
    public static final int MAX_VALUE = 10000;

    protected Point centerPoint;

    protected Point pointer;

    /**
     * Valeur actuelle
     * */
    protected int value = MAX_VALUE / 2;

    /**
     * Taille du panneau
     * */
    public final static Dimension size = new Dimension(55, 60);

    /**
	 * */
    public final static int RADIUS = size.width / 2 - 2;

    /**
     * Liste des écouteurs
     * */
    protected ArrayList<KnobListener> listeners = new ArrayList<KnobListener>();

    public int getValue() {
	return value;
    }

    public void setValue(int value) {
	this.value = value;
    }

}
