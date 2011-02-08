package widgets;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import synthlab.api.Module;
import synthlab.api.Port;
import widgets.configPanel.AbstractConfigPanel;
import widgets.knob.FunctionKnobPanel;
import widgets.knob.NumberKnobPanel;

/**
 * Test
 * */
public class ModuleConfigPanel extends JPanel {
    
    private Module module;
    
    public ModuleConfigPanel(Module module) {
	super(new FlowLayout(FlowLayout.CENTER, 5, 5));
	this.module = module;
	
	setVisible(false);
	
	AbstractConfigPanel knob;
	for(Port p : module.getInputs()) {
	    if(p.getName().equals("iShape")) //TODO just pour test.  
		knob = new FunctionKnobPanel(p.getName());
	    else 
		knob = new NumberKnobPanel(p.getName(), -1.0, 1.0, "", "0.0", !p.isLinked());
	    knob.setPort(p);
	    add((JPanel)knob);
	}	
    }
    
    /**
     * Renvoie 0 s'il est invisible
     * @return hauteur
     * */
    public int getDisplayHeight() {
	if(isVisible())
	    return getHeight();
	else 
	    return 0;	
    }
    
    

}
