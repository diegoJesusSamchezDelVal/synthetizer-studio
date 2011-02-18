package synthlabgui.controller.impl;

import java.util.List;
import synthlab.api.Module;
import synthlab.api.ModulePool;
import synthlab.api.ModulePoolFactory;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.api.SchedulerFactory;
import synthlabgui.controller.AppController;
import synthlabgui.controller.ModuleController;
import synthlabgui.controller.PoolController;
import synthlabgui.presentation.PoolPresentation;
import synthlabgui.presentation.impl.PortHandler;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class BasicPoolController implements PoolController
{
  private ModulePool                      pool_;

  private PoolPresentation                ui;

  private Scheduler                       scheduler_;

  private boolean                         reversed_;

  private boolean                         linking_ = false;

  private PortHandler                     portStart_;

  private BiMap<PortHandler, PortHandler> links_;

  private AppController                   appController;

  public BasicPoolController(AppController app)
  {
    appController = app;
    pool_ = ModulePoolFactory.createDefault();
    scheduler_ = SchedulerFactory.createDefault();
    scheduler_.setPool(pool_);
    scheduler_.play(0);
    links_ = HashBiMap.create();
  }

  public Scheduler getScheduler()
  {
    return scheduler_;
  }

  @Override
  public boolean contains(Module module)
  {
    return pool_.contains(module);
  }

  @Override
  public BiMap<Port, Port> getLinks()
  {
    return pool_.getLinks();
  }

  @Override
  public List<Module> getModules()
  {
    return pool_.getModules();
  }

  @Override
  public void link(Port p1, Port p2)
  {
    pool_.link(p1, p2);
  }

  @Override
  public boolean linked(Port p1, Port p2)
  {
    return pool_.linked(p1, p2);
  }

  @Override
  public void register(Module module)
  {
    pool_.register(module);
  }

  @Override
  public void unlink(Port p1, Port p2)
  {
    pool_.unlink(p1, p2);
  }

  @Override
  public void unlinkAll(Module module)
  {
    pool_.unlinkAll(module);
  }

  @Override
  public void unregister(Module module)
  {
    pool_.unregister(module);
  }

  @Override
  public void addModule(ModuleController module)
  {
    ModuleController moduleController = new BasicModuleController(module.getWrapped());
    ui.addModule(moduleController.getUI());
    appController.getRegistryController().increaseCounter(moduleController.getName());
  }

  @Override
  public PoolPresentation getUI()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isLinking()
  {
    return linking_;
  }

  public void setLinking(boolean state)
  {
    linking_ = state;
  }

  @Override
  public void link(PortHandler output, PortHandler input)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void removeModule(ModuleController module)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void unlink(PortHandler port)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void updateLinks()
  {
  }

  @Override
  public BiMap<PortHandler, PortHandler> getlinks()
  {
    return links_;
  }
}
