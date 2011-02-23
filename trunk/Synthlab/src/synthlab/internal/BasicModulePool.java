package synthlab.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import synthlab.api.Module;
import synthlab.api.ModulePool;
import synthlab.api.Port;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * This is the default implementation of the module pool interface
 */
public class BasicModulePool implements ModulePool
{
  public BasicModulePool()
  {
    // Our modules are stored in an array list since we don't care of their
    // order
    // and have no need to access them using a specific way
    modules_ = new ArrayList<Module>();
    // We associate universely unique ids with each module added to the pool.
    // This is
    // not used currently but might be needed when we will want to save/restore
    // module pools
    uuids_ = HashBiMap.create();
    // This contains all the links between ports of this module
    links_ = HashBiMap.create();
  }

  @Override
  public void register(final Module module)
  {
    // TODO should remove all links ?
    synchronized (this)
    {
      // Check that we do not already contain this module
      if (contains(module))
      {
        return;
      }

      // Add it to our list and generate an uuid
      modules_.add(module);
      uuids_.put(UUID.randomUUID(), module);
    }
  }

  @Override
  public void unregister(final Module module)
  {
    // Remove all links to and from this module beforehand
    unlinkAll(module);
    synchronized (this)
    {
      // Check that we own this module
      if (!contains(module))
      {
        return;
      }

      // Remove it from our list along with its uuid
      modules_.remove(module);
      uuids_.inverse().remove(module);
    }
  }

  @Override
  public boolean contains(final Module module)
  {
    // Check in our list for the presence of this module
    return modules_.contains(module);
  }

  @Override
  public List<Module> getModules()
  {
    // Return the list of all currently owned modules
    return modules_;
  }

  @Override
  public void link(final Port p1, final Port p2)
  {
    // Check which port is the output and which one is the input
    Port input;
    Port output;

    // Sanity check
    if (p1 == null || p2 == null)
    {
      return;
    }

    // Added after unit test
    if (!contains(p1.getModule()) || !contains(p2.getModule()))
    {
      return;
    }

    if (p1.isInput() && p2.isOutput())
    {
      input = p1;
      output = p2;
    }
    else if (p1.isOutput() && p2.isInput())
    {
      input = p2;
      output = p1;
    }
    else
    {
      // Error, both ports are inputs or outputs
      return;
    }

    // Added after unit test
    if (links_.keySet().contains(output) || links_.values().contains(input))
    {
      return;
    }

    // Update the port statuses to linked and add them to the pool
    output.setLinked(true);
    input.setLinked(true);
    links_.put(output, input);
  }

  @Override
  public void unlinkAll(final Module module)
  {
    // We simply call unlink on all inputs and outputs
    for (final Port p : module.getOutputs())
    {
      unlink(p, links_.get(p));
    }
    for (final Port p : module.getInputs())
    {
      unlink(links_.inverse().get(p), p);
    }
  }

  @Override
  public void unlink(final Port p1, final Port p2)
  {
    // Check which port is the output and which one is the input
    Port input;
    Port output;

    // Sanity check
    if (p1 == null || p2 == null)
    {
      return;
    }

    if (p1.isInput() && p2.isOutput())
    {
      input = p1;
      output = p2;
    }
    else if (p1.isOutput() && p2.isInput())
    {
      input = p2;
      output = p1;
    }
    else
    {
      // Error, both ports are inputs or outputs
      return;
    }

    // Update ports statuses
    output.setLinked(false);
    input.setLinked(false);

    // Remove the link
    links_.remove(output);

    // Restore default port value
    output.resetValue();
    input.resetValue();
  }

  @Override
  public boolean linked(final Port p1, final Port p2)
  {
    // Check whether we have a link (any side input->output or output->input)
    // for the provided ports
    return links_.get(p1) == p2 || links_.inverse().get(p1) == p2;
  }

  @Override
  public BiMap<Port, Port> getLinks()
  {
    // Return a bidirectional map of our port mappings (here the default mapping
    // will
    // be output->input but do not trust take this for granted. This is not
    // mandatory
    // for the interface.
    return links_;
  }

  /**
   * This is the list of all the owned modules of this pool
   */
  private final List<Module>        modules_;

  /**
   * This map associate universely unique ids to ours modules. This is not used
   * at the moment but might be needed in the future if we want to save and
   * restore module pools.
   */
  private final BiMap<UUID, Module> uuids_;

  /**
   * This map stores the mapping between output and input ports (i.e. links)
   * that are available inside this module pool.
   */
  private final BiMap<Port, Port>   links_;
}
