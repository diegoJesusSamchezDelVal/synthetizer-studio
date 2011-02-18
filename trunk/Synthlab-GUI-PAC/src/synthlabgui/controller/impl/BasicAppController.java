package synthlabgui.controller.impl;

import synthlabgui.controller.AppController;
import synthlabgui.controller.PoolController;
import synthlabgui.controller.RegistryController;
import synthlabgui.presentation.impl.MainWindow;

public class BasicAppController implements AppController
{
  private RegistryController registryController;

  private PoolController     poolController;

  private MainWindow         ui;

  public BasicAppController()
  {
    registryController = new BasicRegistryController();
    poolController = new BasicPoolController();
    ui = new MainWindow(registryController.getUI(), poolController.getUI());
  }

  @Override
  public PoolController getPoolController()
  {
    return poolController;
  }

  @Override
  public RegistryController getRegistryController()
  {
    return registryController;
  }
}
