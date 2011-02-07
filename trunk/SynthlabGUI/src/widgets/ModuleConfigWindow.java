package widgets;

import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

import synthlab.api.Module;
import synthlab.api.Port;
import widgets.knob.NumberKnobPanel;

public class ModuleConfigWindow extends JDialog {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Module module_;

  public ModuleConfigWindow(synthlab.api.Module module, JFrame parent, Point point) {
      super(parent, true);
      module_ = module;
      
      
      for(Port p : module_.getInputs()) {
         NumberKnobPanel knob = new NumberKnobPanel(p.getName(), 1, 100, "%", "0.0", p.isLinked());
         knob.setPort(p);
         add(knob);
      }
      
      setLocation(point);
      setResizable(false);
      pack();
      setVisible(true);
  }
  
  
  
  }
