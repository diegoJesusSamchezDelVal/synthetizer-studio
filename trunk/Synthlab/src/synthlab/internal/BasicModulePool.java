package synthlab.internal;

import java.util.List;

import com.google.common.collect.*;
import java.util.ArrayList;
import java.util.UUID;

import synthlab.api.Module;
import synthlab.api.ModulePool;
import synthlab.api.Port;

public class BasicModulePool implements ModulePool
{
  public BasicModulePool()
  {
    modules_ = new ArrayList<Module>();
    uuids_ = HashBiMap.create();
    links_ = HashBiMap.create();
  }

  @Override
  public void register(Module module)
  {
    // TODO should remove all links ?
    if (contains(module))
      return;

    modules_.add(module);
    uuids_.put(UUID.randomUUID(), module);
  }

  @Override
  public void unregister(Module module)
  {
    // TODO should remove all links ?
    if (!contains(module))
      return;

    modules_.remove(module);
    uuids_.inverse().remove(module);
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
    
    // Sanity check
    if ( p1==null || p2==null )
      return;
    
    if ( p1.isInput() && p2.isOutput() )
    {
      input = p1;
      output = p2;
    }
    else if ( p1.isOutput() && p2.isInput() )
    {
      input = p2;
      output = p1;
    }
    else
    {
      // Error, both ports are inputs or outputs
      return;
    }

    output.setLinked( true );
    input.setLinked( true );
    links_.put(output, input);
  }

  @Override
  public void unlink(Port p1, Port p2)
  {
    // Check which port is the output and which one is the input
    Port output;
    Port input;

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
        input = p1;
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

    output.setLinked( false );
    input.setLinked( false );
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

  private List<Module>        modules_;
  private BiMap<UUID, Module> uuids_;
  private BiMap<Port, Port>   links_;
}
