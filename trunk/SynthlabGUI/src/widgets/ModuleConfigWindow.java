package widgets;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import synthlab.api.Module;
import synthlab.api.Port;
import widgets.knob.AbstractKnobPanel;
import widgets.knob.FunctionKnob;
import widgets.knob.FunctionKnobPanel;
import widgets.knob.NumberKnobPanel;

/**
 * Panneau de configuration de moule.
 * */
public class ModuleConfigWindow extends JDialog {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * Le module abstrait
   * */
  private Module module_;

  
  public ModuleConfigWindow(synthlab.api.Module module, JFrame parent, Point point) {
      super(parent, true);
      setLayout(new FlowLayout(FlowLayout.CENTER, 5,5));
      
      module_ = module;
      
      AbstractKnobPanel knob;
      for(Port p : module_.getInputs()) {
	  if(p.getName().equals("iShape")) //TODO just pour test.  
	      knob = new FunctionKnobPanel(p.getName());
	  else 
	      knob = new NumberKnobPanel(p.getName(), -1.0, 1.0, "", "0.0", !p.isLinked());
         knob.setPort(p);
         add((JPanel)knob);
      }
      
      setLocation(point);
      setResizable(false);
      pack();
      setVisible(true);
  }
  
  public void paintComponent(Graphics gc) {
      	gc.setColor(Color.white);
      	gc.fillRect(0, 0, getWidth(), getHeight());
  }
  
  
  
  }
