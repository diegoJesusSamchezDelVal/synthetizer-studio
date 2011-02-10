package synthlabgui.widgets.configPanel.keyboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Keyboard extends JPanel {
	public Keyboard(int nbKeys) {
		setLayout(null);
		setBackground(Color.black);
		WhiteKey whiteKey = new WhiteKey();
		BlackKey blackKey = new BlackKey();
		int width = whiteKey.getWidth() * nbKeys;
		int height = whiteKey.getHeight();
		setSize(width, height);
		setPreferredSize(getSize());
		int decx = 0;
		int decy = 0;
		whiteKeys = new ArrayList<Key>();
		blackKeys = new ArrayList<Key>();

		for (int i = 0; i < nbKeys; i++) {
			WhiteKey key = new WhiteKey();
			key.addMouseListener(new InternalMouseListener());
			key.setLocation(i * whiteKey.getWidth() + decx, decy);
			whiteKeys.add(key);
			add(key);
		}
		int decalBlack = decx + whiteKey.getWidth() - blackKey.getWidth() / 2;
		for (int i = 0; i < nbKeys; i++) {
			if (i % 7 == 0 || i % 7 == 1 || i % 7 == 3 || i % 7 == 4
					|| i % 7 == 5) {
				BlackKey key = new BlackKey();
				key.addMouseListener(new InternalMouseListener());
				key.setLocation(decalBlack + whiteKey.getWidth() * i, decy);
				blackKeys.add(key);
				add(key, 0);
			}
		}
	}

	public void paint(Graphics gc) {
		super.paint(gc);
		requestFocus(true);
	}

	public void addKeyboardListener(KeyboardListener listener) {
		listeners.add(listener);
	}

	public void removeKeyboardListener(KeyboardListener listener) {
		listeners.remove(listener);
	}

	private void notifyListeners(KeyboardEvent keyboardEvent) {
		for (KeyboardListener l : listeners) {
			l.keyPressed(keyboardEvent);
		}
	}

	private class InternalMouseListener implements MouseListener {

		private Color keyPressedColor = Color.lightGray;

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			((JPanel) e.getSource()).setBackground(keyPressedColor);
			double value = 0;
			if (e.getSource() instanceof WhiteKey) {
				int modulo = whiteKeys.indexOf(e.getSource()) % 7;
				int i = whiteKeys.indexOf(e.getSource()) / 7;
				value = whiteKeyNumbers[modulo] + 12 * i;
			} else {
				int modulo = blackKeys.indexOf(e.getSource()) % 5;
				int i = blackKeys.indexOf(e.getSource()) / 5;
				value = blackKeyNumbers[modulo] + 12 * i;
			}
			notifyListeners(new KeyboardEvent((Key) e.getSource(), value));
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Key key = (Key) e.getSource();
			((JPanel) e.getSource()).setBackground(key.getColor());
			notifyListeners(new KeyboardEvent((Key) e.getSource(), -1));
			repaint();
		}
	}

	private static final long serialVersionUID = -4122417491103561122L;

	private ArrayList<KeyboardListener> listeners = new ArrayList<KeyboardListener>();

	private double[] whiteKeyNumbers = { 0, 2, 4, 5, 7, 9, 11 };

	private double[] blackKeyNumbers = { 1, 3, 6, 8, 10 };

	ArrayList<Key> whiteKeys;

	ArrayList<Key> blackKeys;
}
