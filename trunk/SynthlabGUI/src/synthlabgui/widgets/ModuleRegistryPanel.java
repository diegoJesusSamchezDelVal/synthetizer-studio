package synthlabgui.widgets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import synthlab.api.ModuleRegistry;
import synthlab.api.ModuleRegistryFactory;
import synthlab.internal.modules.ModuleLFO;
import synthlab.internal.modules.ModuleMixer;
import synthlab.internal.modules.ModuleOut;
import synthlab.internal.modules.ModuleVCA;
import synthlab.internal.modules.ModuleVCF;
import synthlab.internal.modules.ModuleVCO;

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
    registry_.register(new ModuleMixer());
    registry_.register(new ModuleVCF());
    registry_.register(new ModuleOut());
    refresh();
    setupGeneral();
  }

  private void setupGeneral()
  {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
