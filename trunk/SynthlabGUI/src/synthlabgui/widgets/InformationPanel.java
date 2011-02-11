package synthlabgui.widgets;

import java.awt.Component;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class InformationPanel extends JPanel
{
  private static final long serialVersionUID = 3415209104738851835L;

  private Oscilloscope      oscilloscope_;

  private JLabel            lblNameV;

  private JLabel            lblUnitV;

  private JLabel            lblTypeV;

  private JLabel            lblStatusV;

  private JLabel            lblDescriptionV;

  public InformationPanel(Oscilloscope o)
  {
    super();
    oscilloscope_ = o;
    setupGeneral();
  }

  public void monitor(PortHandler p)
  {
    oscilloscope_.monitor(p);
    lblNameV.setText(p.getWrapped().getName());
    lblUnitV.setText(p.getWrapped().getValueUnit().toString());
    lblTypeV.setText(p.getWrapped().getValueType().toString());
    lblStatusV.setText(p.getWrapped().isLinked() ? "Linked" : "Unlinked");
    lblDescriptionV.setText(p.getWrapped().getDescription());
    validate();
    repaint();
  }

  private void setupGeneral()
  {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setVisible(true);
    add(oscilloscope_);
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new SpringLayout());
    JLabel lblName = new JLabel("Port name:");
    JLabel lblUnit = new JLabel("Port unit:");
    JLabel lblType = new JLabel("Port type:");
    JLabel lblStatus = new JLabel("Port status:");
    JLabel lblDescription = new JLabel("Port description:");
    lblNameV = new JLabel("N/A");
    lblUnitV = new JLabel("N/A");
    lblTypeV = new JLabel("N/A");
    lblStatusV = new JLabel("N/A");
    lblDescriptionV = new JLabel("N/A");
    lblName.setLabelFor(lblNameV);
    lblUnit.setLabelFor(lblUnitV);
    lblType.setLabelFor(lblTypeV);
    lblStatus.setLabelFor(lblStatusV);
    lblDescription.setLabelFor(lblDescriptionV);
    rightPanel.add(lblName);
    rightPanel.add(lblNameV);
    rightPanel.add(lblUnit);
    rightPanel.add(lblUnitV);
    rightPanel.add(lblType);
    rightPanel.add(lblTypeV);
    rightPanel.add(lblStatus);
    rightPanel.add(lblStatusV);
    rightPanel.add(lblDescription);
    rightPanel.add(lblDescriptionV);
    add(rightPanel);
    add(Box.createHorizontalGlue());
    makeCompactGrid(rightPanel, 5, 2, 6, 6, 6, 6);
  }

  public static void makeCompactGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad,
      int yPad)
  {
    SpringLayout layout;
    try
    {
      layout = (SpringLayout) parent.getLayout();
    }
    catch (ClassCastException exc)
    {
      System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
      return;
    }
    // Align all cells in each column and make them the same width.
    Spring x = Spring.constant(initialX);
    for (int c = 0; c < cols; c++)
    {
      Spring width = Spring.constant(0);
      for (int r = 0; r < rows; r++)
      {
        width = Spring.max(width, getConstraintsForCell(r, c, parent, cols).getWidth());
      }
      for (int r = 0; r < rows; r++)
      {
        SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
        constraints.setX(x);
        constraints.setWidth(width);
      }
      x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
    }
    // Align all cells in each row and make them the same height.
    Spring y = Spring.constant(initialY);
    for (int r = 0; r < rows; r++)
    {
      Spring height = Spring.constant(0);
      for (int c = 0; c < cols; c++)
      {
        height = Spring.max(height, getConstraintsForCell(r, c, parent, cols).getHeight());
      }
      for (int c = 0; c < cols; c++)
      {
        SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
        constraints.setY(y);
        constraints.setHeight(height);
      }
      y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
    }
    // Set the parent's size.
    SpringLayout.Constraints pCons = layout.getConstraints(parent);
    pCons.setConstraint(SpringLayout.SOUTH, y);
    pCons.setConstraint(SpringLayout.EAST, x);
  }

  private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols)
  {
    SpringLayout layout = (SpringLayout) parent.getLayout();
    Component c = parent.getComponent(row * cols + col);
    return layout.getConstraints(c);
  }
}
