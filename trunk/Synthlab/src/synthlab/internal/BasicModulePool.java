package synthlab.internal;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

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
    outputs2inputs_ = new HashMap<Port,Port>();
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

    outputs2inputs_.put(output, input);
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

    outputs2inputs_.remove(output);
  }

  @Override
  public boolean linked(Port p1, Port p2)
  {
    return outputs2inputs_.get(p1)==p2 || outputs2inputs_.get(p2)==p1;
  }

  private List<Module>      modules_;
  private Map<UUID, Module> uuids2modules_;
  private Map<Module, UUID> modules2uuids_;
  private Map<Port, Port>   outputs2inputs_;
}
