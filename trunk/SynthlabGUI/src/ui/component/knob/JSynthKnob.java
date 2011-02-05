package ui.component.knob;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.component.JSynthResources;


public class JSynthKnob extends JPanel implements MouseListener, MouseMotionListener, ImageObserver {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double minValue;
	
	private double maxValue;

	private Point dragStartPoint;

	private Point centerPoint;

	private double currentValue;

	private ArrayList<JSynthKnobTurnListener> turnListeners = new ArrayList<JSynthKnobTurnListener>();
	
	private ImageIcon knobImage;
	
	private ImageIcon backgroundImage;
	
	private ImageIcon pointerImage;

	private Point knobPosition;

	private Point pointerPosition = new Point();
	/**
	 * Constructeur par défault
	 * */
	public JSynthKnob(double min, double max) {
		super(null);
		minValue = min;
		maxValue = max;
		currentValue = minValue;
		
		
				
		backgroundImage = new ImageIcon(JSynthResources.KNOB_SCALE_MINMAX_IMAGE);
		knobImage = new ImageIcon(JSynthResources.KNOB_BASE_IMAGE);
		pointerImage = new ImageIcon(JSynthResources.KNOB_GREEN_POINTER_IMAGE);
		
		knobPosition = new Point(
				(backgroundImage.getIconWidth()-knobImage.getIconWidth())/2,
				(backgroundImage.getIconHeight()-knobImage.getIconHeight())/2);
		Dimension dim = new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
		setMinimumSize(dim);
		setPreferredSize(dim);
		setSize(dim);
		
		centerPoint = new Point(getSize().width/2, getSize().width/2);
		
		// setBorder(BorderFactory.createEtchedBorder());
		addMouseListener(this);
		addMouseMotionListener(this);
		
	}
	
	public void addKnobTurnListener(JSynthKnobTurnListener listener) {
		turnListeners.add(listener);
	}
	
	public void removeKnobTurnListener(JSynthKnobTurnListener listener) {
		turnListeners.remove(listener);
	}
	
	
	
	private void notifyListener(double value) {
		KnobTurnEvent event = new KnobTurnEvent(this, value);
		for(JSynthKnobTurnListener listener : turnListeners) {
			listener.knobTurned(event);
		}		
	}
	
	public void paintComponent(Graphics gc){
		Graphics2D g = (Graphics2D)gc;
			
		g.drawImage(backgroundImage.getImage(), 0, 0, this);
		g.drawImage(knobImage.getImage(), knobPosition.x, knobPosition.y, this);
		// g.drawImage(pointerImage.getImage(), 0, 0, this);
		g.setColor(Color.GREEN);
		g.fillOval(pointerPosition.x, pointerPosition.y, 5, 5);
		
	
	}

	@Override
	public void mousePressed(MouseEvent e) {		
		dragStartPoint = e.getPoint();		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point currentPoint = e.getPoint();
		System.out.println(currentPoint.toString());
		int dx = centerPoint.x -  currentPoint.x;
		int dy = centerPoint.y - currentPoint.y;
		double currentAngle = Math.atan2(dy, dx);
		dx = centerPoint.x - dragStartPoint.x;
		dy = centerPoint.y - dragStartPoint.y;
		double startAngle = Math.atan2(dy, dx);
		
		double dAngle = currentAngle - startAngle;
		double percent = Math.abs(dAngle / Math.PI * 1.5);
		
		currentValue = percent * (maxValue - minValue);
		
		// mise à jour la position de pointer
		
		notifyListener(currentValue);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		
	}
	
	/**
	 * Test 
	 * */
	public static void main(String [] args) {
		JFrame f = new JFrame();
		f.setLayout(new FlowLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400,400);
		f.setVisible(true);
		final JLabel label = new JLabel("value");
		JSynthKnob knob = new JSynthKnob(1,10);
		f.add(knob);
		f.add(label);
		
		knob.addKnobTurnListener(new JSynthKnobTurnListener() {
			
			@Override
			public void knobTurned(KnobTurnEvent event) {
				System.out.println("turned");
				label.setText(String.valueOf(event.getValue()));
				
			}
		});
	}
}
