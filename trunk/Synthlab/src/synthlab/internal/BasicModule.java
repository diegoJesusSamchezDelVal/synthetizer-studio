package synthlab.internal;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import synthlab.api.Module;
import synthlab.api.Port;

public abstract class BasicModule implements Module
{
  public BasicModule(String name)
  {
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
    if (inputExists(input.getName()))
      return;

    input.setModule(this);
    inputs_.put(input.getName(), input);
  }

  @Override
  public void addInputs(List<Port> inputs)
  {
    for (Port port : inputs)
      addInput(port);
  }

  @Override
  public void removeInput(String name)
  {
    if (!inputExists(name))
      return;

    inputs_.get(name).setModule(null);
    inputs_.remove(name);
  }

  @Override
  public void removeInputs(List<String> names)
  {
    for (String name : names)
      removeInput(name);
  }

  @Override
  public Port getInput(String name)
  {
    if (!inputExists(name))
      return null;

    return inputs_.get(name);
  }

  @Override
  public List<Port> getInputs()
  {
    List<Port> result = new ArrayList<Port>();

    for (Port input : inputs_.values())
      result.add(input);

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
    if (outputExists(output.getName()))
      return;

    output.setModule(this);
    outputs_.put(output.getName(), output);
  }

  @Override
  public void addOutputs(List<Port> outputs)
  {
    for (Port output : outputs)
      addOutput(output);
  }

  @Override
  public void removeOutput(String name)
  {
    if (!outputExists(name))
      return;

    outputs_.get(name).setModule(null);
    outputs_.remove(name);
  }

  @Override
  public void removeOutputs(List<String> names)
  {
    for (String name : names)
      removeOutput(name);
  }

  @Override
  public Port getOutput(String name)
  {
    if (!outputExists(name))
      return null;

    return outputs_.get(name);
  }

  @Override
  public List<Port> getOutputs()
  {
    List<Port> result = new ArrayList<Port>();

    for (Port output : outputs_.values())
      result.add(output);

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
    return inputs_.containsKey(name);
  }

  private boolean outputExists(String name)
  {
    return outputs_.containsKey(name);
  }

  protected String            name_;
  protected Map<String, Port> inputs_;
  protected Map<String, Port> outputs_;
}
