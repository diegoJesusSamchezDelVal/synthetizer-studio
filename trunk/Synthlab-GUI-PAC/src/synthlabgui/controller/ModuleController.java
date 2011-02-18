package synthlabgui.controller;

import synthlab.api.Module;
import synthlabgui.presentation.ModulePresentation;

public interface ModuleController extends Module
{
  public Module getWrapped();

  public ModulePresentation getUI();
}
