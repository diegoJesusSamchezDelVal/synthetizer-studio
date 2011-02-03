package synthlab.internal;

import java.util.List;

import com.google.common.collect.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Set;

import synthlab.api.Module;
import synthlab.api.ModulePool;
import synthlab.api.Port;

public class BasicModulePool implements ModulePool
{
  public BasicModulePool()
  {
    modules_ = new ArrayList<Module>();
    uuids2modules_ = new HashMap<UUID, Module>();
    modules2uuids_ = new HashMap<Module, UUID>();
    links_ = HashBiMap.create();
  }

  @Override
  public void register(Module module)
  {
    // TODO should remove all links ?
    if (contains(module))
      return;

    modules_.add(module);
    UUID uuid = UUID.randomUUID();
    uuids2modules_.put(uuid, module);
    modules2uuids_.put(module, uuid);
  }

  @Override
  public void unregister(Module module)
  {
    // TODO should remove all links ?
    if (!contains(module))
      return;

    modules_.remove(module);
    UUID uuid = modules2uuids_.get(module);
    uuids2modules_.remove(uuid);
    modules2uuids_.remove(module);
  }

  @Override
  public boolean contains(Module module)
  {
    return modules_.contains(module);
  }

  @Override
  public List<Module> getModules()
  {
    return modules_;
  }

  @Override
  public void link(Port p1, Port p2)
  {
    // Check which port is the output and which one is the input
    Port input;
    Port output;

    if (p1.getModule().getInputs().contains(p1))
    {
      if (p2.getModule().getInputs().contains(p2))
      {
        // Error, both ports are inputs
        return;
      }
      else
      {
        input = p1;
        output = p2;
      }
    }
    else
    {
      if (p2.getModule().getOutputs().contains(p2))
      {
        // Error, both ports are outputs
        return;
      }
      else
      {
        output = p1;
        input = p2;
      }
    }

    links_.put(output, input);
  }

  @Override
  public void unlink(Port p1, Port p2)
  {
    // Check which port is the output and which one is the input
    Port output;

    if (p1.getModule().getInputs().contains(p1))
    {
      if (p2.getModule().getInputs().contains(p2))
      {
        // Error, both ports are inputs
        return;
      }
      else
      {
        output = p2;
      }
    }
    else
    {
      if (p2.getModule().getOutputs().contains(p2))
      {
        // Error, both ports are outputs
        return;
      }
      else
      {
        output = p1;
      }
    }

    links_.remove(output);
  }

  @Override
  public boolean linked(Port p1, Port p2)
  {
    return links_.get(p1) == p2 || links_.inverse().get(p1) == p2;
  }

  @Override
  public BiMap<Port, Port> getLinks()
  {
    return links_;
  }

  private List<Module>      modules_;
  private Map<UUID, Module> uuids2modules_;
  private Map<Module, UUID> modules2uuids_;
  private BiMap<Port, Port> links_;
}
