package synthlab.internal;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import synthlab.api.Module;
import synthlab.api.Port;

/**
 * This is the default implementation of a module provided by the synthesizer.
 */
public abstract class BasicModule implements Module
{
  public BasicModule(String name)
  {
    // These maps store the name->port mapping for a faster lookup (since most
    // accessors works by port name)
    inputs_ = new HashMap<String, Port>();
    outputs_ = new HashMap<String, Port>();
    name_ = name;
  }

  @Override
  public String getName()
  {
    return name_;
  }

  @Override
  public void addInput(Port input)
  {
    // Check that the input port is not already present
    if (inputExists(input.getName()))
      return;

    // Assign this module as the owner of the port
    input.setModule(this);
    
    // Add this port to our map
    inputs_.put(input.getName(), input);
  }

  @Override
  public void addInputs(List<Port> inputs)
  {
    // Call addInput() for each port provided
    for (Port port : inputs)
      addInput(port);
  }

  @Override
  public void removeInput(String name)
  {
    // Check that this port is already present
    if (!inputExists(name))
      return;

    // Reset the owner module of this port
    inputs_.get(name).setModule(null);
    
    // Remove the port from our map
    inputs_.remove(name);
  }

  @Override
  public void removeInputs(List<String> names)
  {
    // Call removeInput for all provided port
    for (String name : names)
      removeInput(name);
  }

  @Override
  public Port getInput(String name)
  {
    // Check that the input exists or return null otherwise
    if (!inputExists(name))
      return null;

    // Return it
    return inputs_.get(name);
  }

  @Override
  public List<Port> getInputs()
  {
    // Performance tip: do not use this function too often this its
    // cost is heavy! We store the input port in a map, we have to
    // manually create a list to send it back to the user
    List<Port> result = new ArrayList<Port>();

    for (Port input : inputs_.values())
      result.add(input);

    // Sort the port list for nicer output (the hash sorting is often
    // not appropriate for a nice display)
    Collections.sort(result, new Comparator<Port>()
    {
      public int compare(Port p1, Port p2)
      {
        return p1.getName().compareTo(p2.getName());
      }
    });

    return result;
  }

  @Override
  public void addOutput(Port output)
  {
    // Checl the the port is not already present
    if (outputExists(output.getName()))
      return;

    // Set this module as the owner of the port
    output.setModule(this);
    
    // Add this port to our port map
    outputs_.put(output.getName(), output);
  }

  @Override
  public void addOutputs(List<Port> outputs)
  {
    // Call addOutput on each provided output port
    for (Port output : outputs)
      addOutput(output);
  }

  @Override
  public void removeOutput(String name)
  {
    // Check that this port is present
    if (!outputExists(name))
      return;

    // Reset the owner module of this port
    outputs_.get(name).setModule(null);
    
    // Remove it from our map
    outputs_.remove(name);
  }

  @Override
  public void removeOutputs(List<String> names)
  {
    // Call removeOutput on each provided port
    for (String name : names)
      removeOutput(name);
  }

  @Override
  public Port getOutput(String name)
  {
    // Check that this port is present
    if (!outputExists(name))
      return null;

    return outputs_.get(name);
  }

  @Override
  public List<Port> getOutputs()
  {
    // Performance tip: do not use this function too often this its
    // cost is heavy! We store the input port in a map, we have to
    // manually create a list to send it back to the user
    List<Port> result = new ArrayList<Port>();

    for (Port output : outputs_.values())
      result.add(output);

    // Sort the port list for nicer output (the hash sorting is often
    // not appropriate for a nice display)
    Collections.sort(result, new Comparator<Port>()
    {
      public int compare(Port p1, Port p2)
      {
        return p1.getName().compareTo(p2.getName());
      }
    });

    return result;
  }

  private boolean inputExists(String name)
  {
    // Check that our input port map contains this port name mapping
    return inputs_.containsKey(name);
  }

  private boolean outputExists(String name)
  {
    // Check that our output port map contains this port name mapping
    return outputs_.containsKey(name);
  }
  
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    // We cannot clone basic module. only subclasses of it since a basic module have
    // no compute method
    throw new CloneNotSupportedException("Cannot clone BasicModule: abstract class");
  }

  /**
   * The name of the module
   */
  protected String            name_;
  
  /**
   * A map from the port name to the port instance. This will speedup port lookup since we often
   * need to retrieve them by name.
   */
  protected Map<String, Port> inputs_;
  
  /**
   * A map from the port name to the port instance. This will speedup port lookup since we often
   * need to retrieve them by name.
   */
  protected Map<String, Port> outputs_;
}
