package synthlabgui.widgets.configPanel.knob;

import java.awt.FlowLayout;
import java.awt.Point;

import javax.swing.JFrame;

public class WaveShapeChooser extends AbstractKnob {
	public WaveShapeChooser() {
		super();
	}

	@Override
	protected Point computePointer(Point currentPoint) {
		int RADIUS = 28;
		if ((centerPoint.x - currentPoint.x) != 0) {
			double k = ((double) (centerPoint.y - currentPoint.y))
					/ ((double) (centerPoint.x - currentPoint.x));
			// double b = centerPoint.y - (k * centerPoint.x);
			if (Math.abs(k) <= 0.3) { // horizonal
				if (currentPoint.x < centerPoint.x) {
					value = 0;
					return new Point(centerPoint.x - RADIUS, centerPoint.y);
				} else {
					value = 2;
					return new Point(centerPoint.x + RADIUS, centerPoint.y);
				}
			} else {
				return pointer;
			}
		} else { // vertical
			if (currentPoint.y < centerPoint.y) {
				value = 1;
				return new Point(centerPoint.x, centerPoint.y - RADIUS);
			} else {
				value = 3;
				return new Point(centerPoint.x, centerPoint.y + RADIUS);
			}
		}
	}

	/**
	 * Inutile pour cette classe.
	 * */
	@Override
	protected int computeValue(double k, Point currentPoint) {
		return 0;
	}

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setLayout(new FlowLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400, 400);
		f.setVisible(true);
		WaveShapeChooser k = new WaveShapeChooser();
		f.add(k);
	}
}
