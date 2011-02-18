package synthlabgui.controller;

import synthlab.api.ModuleRegistry;
import synthlabgui.presentation.RegistryPresentation;

public interface RegistryController extends ModuleRegistry
{
  public void addModule(synthlab.api.Module module);

  public int getCounter(String name);

  public void increaseCounter(String name);

  public RegistryPresentation getUI();
}
