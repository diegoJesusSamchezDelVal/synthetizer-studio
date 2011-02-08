package widgets;

import javax.swing.*;
import synthlab.api.*;
import synthlab.internal.modules.*;

public class ModuleRegistryPanel extends JPanel
{
  private static final long serialVersionUID = -2727139434040925986L;

  public ModuleRegistryPanel()
  {
    super();

    registry_ = ModuleRegistryFactory.createDefault();

    // TODO insane hack to hard code some prototypes in the registry
    registry_.register(new ModuleLFO());
    registry_.register(new ModuleVCO());
    registry_.register(new ModuleVCA());
    registry_.register(new ModuleOut());

    refresh();

    setupGeneral();
  }

  private void setupGeneral()
  {
    setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS) );
    setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    
    setVisible(true);
  }

  public void refresh()
  {
    for (synthlab.api.Module prototype : registry_.getModules())
    {
      add(new ModulePrototype(prototype));
    }
  }

  private ModuleRegistry registry_;
}
