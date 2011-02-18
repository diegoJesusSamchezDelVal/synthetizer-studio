package synthlabgui.controller.impl;

import java.util.List;
import synthlab.api.Module;
import synthlab.api.ModuleFactory;
import synthlab.api.Port;
import synthlabgui.controller.ModuleController;
import synthlabgui.presentation.ModulePresentation;

public class BasicModuleController implements ModuleController
{
  private synthlab.api.Module moduleAbstraction;

  private ModulePresentation  ui;

  public BasicModuleController(synthlabgui.presentation.impl.Module module)
  {
    moduleAbstraction = ModuleFactory.createFromPrototype(module.getWrapped());
    ui = new synthlabgui.presentation.impl.Module(moduleAbstraction.getName(), moduleAbstraction, null);
  }

  @Override
  public void addInput(Port input)
  {
    moduleAbstraction.addInput(input);
  }

  @Override
  public void addInputs(List<Port> inputs)
  {
    moduleAbstraction.addInputs(inputs);
  }

  @Override
  public void addOutput(Port output)
  {
    moduleAbstraction.addOutput(output);
  }

  @Override
  public void addOutputs(List<Port> outputs)
  {
    moduleAbstraction.addOutputs(outputs);
  }

  @Override
  public void compute()
  {
    moduleAbstraction.compute();
  }

  @Override
  public Port getInput(String name)
  {
    return moduleAbstraction.getInput(name);
  }

  @Override
  public List<Port> getInputs()
  {
    return moduleAbstraction.getOutputs();
  }

  @Override
  public String getName()
  {
    return moduleAbstraction.getName();
  }

  @Override
  public Port getOutput(String name)
  {
    return moduleAbstraction.getOutput(name);
  }

  @Override
  public List<Port> getOutputs()
  {
    return moduleAbstraction.getOutputs();
  }

  @Override
  public void removeInput(String name)
  {
    moduleAbstraction.removeInput(name);
  }

  @Override
  public void removeInputs(List<String> names)
  {
    moduleAbstraction.removeInputs(names);
  }

  @Override
  public void removeOutput(String name)
  {
    moduleAbstraction.removeOutput(name);
  }

  @Override
  public void removeOutputs(List<String> names)
  {
    moduleAbstraction.removeOutputs(names);
  }

  public Module clone() throws CloneNotSupportedException
  {
    return moduleAbstraction.clone();
  }
}
