package synthlabgui.widgets.configPanel.knob;

import java.awt.Color;
import java.awt.Dimension;
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
		setOpaque(false);
		centerPoint = new Point(size.width / 2, size.height / 2);
		pointer = new Point(size.width / 2, 0);
		addMouseListener(this);
		addMouseMotionListener(this);
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

	public void paintComponent(Graphics gc) {
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		((Graphics2D) gc).setRenderingHints(renderHints);
		gc.setColor(Color.white);
		gc.fillOval(0, 0, getWidth(), getHeight());
		gc.setColor(Color.black);
		gc.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
		gc.setColor(Color.red);
		gc.drawLine(centerPoint.x, centerPoint.y, pointer.x, pointer.y);
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
		repaint();
		pointer = computePointer(e.getPoint());
		notifyListener();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		repaint();
	}

	/**
	 * Calule la position de règlage par rapport de la position de la souris.
	 * 
	 * @param point
	 *            La position de la souris
	 * @param le
	 *            Le point de la position de règlage
	 * */
	protected abstract Point computePointer(Point point);

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
	public final static Dimension size = new Dimension(40, 40);

	/**
	 * */
	public final static int RADIUS = size.width / 2 - 2;

	/**
	 * Liste des écouteurs
	 * */
	protected ArrayList<KnobListener> listeners = new ArrayList<KnobListener>();
}
