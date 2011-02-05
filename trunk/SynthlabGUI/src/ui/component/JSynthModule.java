package ui.component;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class JSynthModule extends JPanel implements MouseListener, MouseMotionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5261300899410794950L;
	
	protected Point dragStartPoint;
	
	protected JPanel inputPortPanel;
	
	protected JPanel outputPortPanel;
	
	protected JPanel mainPanel;

	public JSynthModule() {
		super(new BorderLayout());
		
		ResourceBundle res = ResourceBundle.getBundle("lang");
		inputPortPanel = new JPanel();
		BoxLayout layout = new BoxLayout(inputPortPanel, BoxLayout.Y_AXIS);
		inputPortPanel.setLayout(layout);
		inputPortPanel.setBorder(BorderFactory.createTitledBorder(res.getString("input_title")));
		
		outputPortPanel = new JPanel();
		layout = new BoxLayout(inputPortPanel, BoxLayout.Y_AXIS);
		outputPortPanel.setLayout(layout);
		outputPortPanel.setBorder(BorderFactory.createTitledBorder(res.getString("output_title")));
		
		mainPanel = new JPanel(new GridBagLayout());
		
		add(inputPortPanel, BorderLayout.WEST);
		add(outputPortPanel, BorderLayout.EAST);
		add(mainPanel, BorderLayout.CENTER);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		initComponents();
	}
	
	public void initComponents(){
		
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
	public void mouseDragged(MouseEvent event) {
		int dx = event.getPoint().x - dragStartPoint.x;
		int dy = event.getPoint().y - dragStartPoint.y;
		Point p = getLocation();
		p.translate(dx, dy);
		setLocation(p);		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

}
