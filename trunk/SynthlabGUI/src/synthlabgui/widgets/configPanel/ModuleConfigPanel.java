package synthlabgui.widgets.configPanel;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import synthlab.api.Module;
import synthlab.api.Port;
import synthlabgui.widgets.configPanel.knob.numberKnob.NumberKnobPanel;
import synthlabgui.widgets.configPanel.knob.waveChooser.WaveShapeChooserPanel;

public class ModuleConfigPanel extends JPanel
{
  public ModuleConfigPanel(Module module)
  {
    super(new FlowLayout(FlowLayout.CENTER, 5, 5));
    // this.module = module;
    setVisible(false);
    AbstractConfigPanel knob;
    for (Port p : module.getInputs())
    {
      if (p.getName().equals("iShape")) // TODO just pour test.
        knob = new WaveShapeChooserPanel(p.getName());
      else
        knob = new NumberKnobPanel(p.getName(), -1.0, 1.0, "", "0.0", !p.isLinked(), false);
      knob.setPort(p);
      add((JPanel) knob);
    }
  }

  public int getDisplayHeight()
  {
    if (isVisible())
      return getHeight();
    else
      return 0;
  }

  private static final long serialVersionUID = -4976066992806565864L;
  // private Module module;
}
