package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class CloseButton extends JPanel implements MouseListener, MouseMotionListener {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4424643932151754593L;
    
    Module module;
    private Color normalColor = Color.black;
    private Color mouseOverColor = Color.red;
    
    private int scale = 2;
    
    boolean mouseOver = false;
    private boolean mousePressed = false;
    
    public CloseButton(Module parent) {
	module = parent;
	
	Dimension dim = new Dimension(12, 12);
	
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
	if (mouseOver)
	    gc.setColor(mouseOverColor );
	else 
	    gc.setColor(normalColor);	
	
	if (!mousePressed) {
	    gc.drawLine(0, 0, getWidth()-1, getHeight()-1);
	    gc.drawLine(0, getHeight()-1, getWidth()-1, 0);
	} else {
	    gc.drawLine(0 + scale, 0 + scale, getWidth()-1-scale, getHeight()-1-scale);
	    gc.drawLine(0 + scale, getHeight() -1 - scale, getWidth()-scale-1, 0+scale);
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
	//if(this.contains(e.getPoint())
	//TODO module.close();  enleve ce module
    }


    @Override
    public void mouseDragged(MouseEvent arg0) {
    }
    
    @Override
    public void mouseMoved(MouseEvent arg0) {
    }
}
