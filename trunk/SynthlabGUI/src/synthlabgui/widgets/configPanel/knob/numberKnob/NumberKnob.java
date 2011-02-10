package synthlabgui.widgets.configPanel.knob.numberKnob;

import java.awt.Point;

import synthlabgui.widgets.configPanel.knob.AbstractKnob;

public class NumberKnob extends AbstractKnob {
	public NumberKnob() {
		super();
	}

	protected Point computePointer(Point currentPoint) {
		if ((centerPoint.x - currentPoint.x) != 0) {
			double k = ((double) (centerPoint.y - currentPoint.y))
					/ ((double) (centerPoint.x - currentPoint.x));
			double b = centerPoint.y - (k * centerPoint.x);
			if (hasBlock
					&& currentPoint.y > centerPoint.y
					&& (k > Math.tan(Math.PI / 6) || k < Math.tan(-Math.PI / 6))) {
				if (pointer.x < centerPoint.x)
					value = 0;
				else
					value = MAX_VALUE - 1;
				return pointer;
			} else {
				value = computeValue(k, currentPoint);
				boolean found = false;
				double x = currentPoint.x;
				while (!found) {
					double y = k * x + b;
					double r = Math.sqrt(Math.pow((centerPoint.y - y), 2)
							+ Math.pow((centerPoint.x - x), 2));
					if (Math.abs(r - RADIUS) <= 2.0) {
						found = true;
						break;
					} else {
						if (x < centerPoint.x && r > RADIUS)
							x += 0.01;
						else if (x < centerPoint.x && r < RADIUS)
							x -= 0.01;
						else if (x > centerPoint.x && r > RADIUS)
							x -= 0.01;
						else if (x > centerPoint.x && r < RADIUS)
							x += 0.01;
					}
				}
				int y = (int) (k * x + b);
				return new Point((int) x, y);
			}
		} else {
			if (currentPoint.y < centerPoint.y) {
				value = MAX_VALUE / 2;
				return new Point(centerPoint.x, centerPoint.y - RADIUS);
			} else {
				return pointer;
			}
		}
	}

	private static final long serialVersionUID = 1L;

	private boolean hasBlock = true;

	@Override
	protected int computeValue(double k, Point currentPoint) {
		double angle = Math.atan(k);
		int result;
		if (currentPoint.x < centerPoint.x) {
			angle += Math.PI / 6;
			result = (int) (angle / (Math.PI / 7500));
		} else {
			angle += Math.PI / 2;
			result = (int) (angle / (Math.PI / 7500)) + 5000;
		}
		return result;
	}
}
