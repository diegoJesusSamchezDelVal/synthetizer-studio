package synthlabgui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import synthlab.api.Port;

/**
 * gerer l'état de la led allume/éteinte
 * 
 */
public class JLed extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    /**
     * état de led
     */
    private boolean allume;

    private Color colorOn = new Color(100, 200, 100);
    private Color colorOff = new Color(200, 50, 50);

    /**
     * Constructeur par défaut
     */
    public JLed() {
	Dimension size = new Dimension(13, 8);
	setMinimumSize(size);
	setMaximumSize(size);
	setPreferredSize(size);
	setSize(size);
	allume = false;
	setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    public void allumer() {
	allume = true;
    }

    /**
     * {@inheritDoc}
     */
    public void eteindre() {
	allume = false;
    }

    /**
     * @return vrai si la led est allumé, faux sinon.
     */
    public boolean isAllume() {
	return allume;
    }

    /**
     * @param allume
     */
    public void setAllume(boolean allume) {
	this.allume = allume;
    }

    /**
     * Dessine la LED.
     * 
     * TODO A changer
     */
    @Override
    public void paint(Graphics g) {
	if (allume) {
	    g.setColor(colorOn);
	} else {
	    g.setColor(colorOff);
	}
	g.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
    }

    @Override
    public void update(Observable o, Object arg) {
	double value = ((Port) o).getValues().getDouble(0);
	if (value > 0)
	    allumer();
	else
	    eteindre();
	repaint();
    }

}