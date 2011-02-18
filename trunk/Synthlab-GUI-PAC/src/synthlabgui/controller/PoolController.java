package synthlabgui.controller;

import synthlab.api.ModulePool;
import synthlab.api.Scheduler;
import synthlabgui.presentation.PoolPresentation;
import synthlabgui.presentation.impl.PortHandler;
import com.google.common.collect.BiMap;

public interface PoolController extends ModulePool
{
  public void addModule(ModuleController module);

  public void removeModule(ModuleController module);

  public boolean isLinking();

  public void setLinking(boolean state);

  public void updateLinks();

  public void link(PortHandler output, PortHandler input);

  public void unlink(PortHandler port);

  public Scheduler getScheduler();

  public PoolPresentation getUI();

  public BiMap<PortHandler, PortHandler> getlinks();
}
