package component;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class JSynthModule extends JPanel implements MouseListener, MouseMotionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5261300899410794950L;
	
	private Point dragStartPoint;
	
	private JPanel inputPortPanel;
	
	private JPanel outputPortPanel;
	
	private JPanel mainPanel;

	public JSynthModule() {
		
		addMouseListener(this);
		addMouseMotionListener(this);
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
	public void mousePressed(MouseEvent e) {
		dragStartPoint = e.getPoint();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		int dx = event.getPoint().x - dragStartPoint.x;
		int dy = event.getPoint().y - dragStartPoint.y;
		Point p = getLocation();
		p.translate(dx, dy);
		setLocation(p);		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
