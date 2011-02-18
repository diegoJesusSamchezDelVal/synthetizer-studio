package synthlabgui.controller.impl;

import java.util.HashMap;
import java.util.List;
import synthlab.api.Module;
import synthlab.api.ModuleRegistry;
import synthlab.api.ModuleRegistryFactory;
import synthlab.internal.modules.ModuleEnvelope;
import synthlab.internal.modules.ModuleKeyboard;
import synthlab.internal.modules.ModuleLFO;
import synthlab.internal.modules.ModuleMixer;
import synthlab.internal.modules.ModuleMultiplexer;
import synthlab.internal.modules.ModuleOut;
import synthlab.internal.modules.ModuleOutToFile;
import synthlab.internal.modules.ModuleSequencer;
import synthlab.internal.modules.ModuleVCA;
import synthlab.internal.modules.ModuleVCF;
import synthlab.internal.modules.ModuleVCO;
import synthlabgui.controller.RegistryController;
import synthlabgui.presentation.RegistryPresentation;
import synthlabgui.presentation.impl.ModulePrototype;
import synthlabgui.presentation.impl.ModuleRegistryPanel;

public class BasicRegistryController implements RegistryController
{
  private ModuleRegistry           registryAbstraction;

  private RegistryPresentation     ui;

  private HashMap<String, Integer> counterTable = new HashMap<String, Integer>();

  public BasicRegistryController()
  {
    registryAbstraction = ModuleRegistryFactory.createDefault();
    ui = new ModuleRegistryPanel();
    // Add default modules
    addModule(new ModuleLFO());
    addModule(new ModuleVCO());
    addModule(new ModuleVCA());
    addModule(new ModuleMixer());
    addModule(new ModuleMultiplexer());
    addModule(new ModuleVCF());
    addModule(new ModuleEnvelope());
    addModule(new ModuleOut());
    addModule(new ModuleOutToFile());
    addModule(new ModuleKeyboard());
    addModule(new ModuleSequencer());
  }

  @Override
  public List<Module> getModules()
  {
    return registryAbstraction.getModules();
  }

  @Override
  public void register(Module moduleClass)
  {
    registryAbstraction.register(moduleClass);
  }

  @Override
  public boolean registered(Module moduleClass)
  {
    return registryAbstraction.registered(moduleClass);
  }

  @Override
  public void unregister(Module moduleClass)
  {
    registryAbstraction.unregister(moduleClass);
  }

  @Override
  public void addModule(Module module)
  {
    registryAbstraction.register(module);
    ui.addModule(new ModulePrototype(module));
    counterTable.put(module.getName(), 1);
  }

  @Override
  public int getCounter(String name)
  {
    return counterTable.get(name);
  }

  @Override
  public void increaseCounter(String name)
  {
    int i = counterTable.get(name) + 1;
    counterTable.put(name, i);
  }

  @Override
  public RegistryPresentation getUI()
  {
    return ui;
  }
}
