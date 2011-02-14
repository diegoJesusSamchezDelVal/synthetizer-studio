package synthlab.api;

import java.util.List;


public interface Module
{
  // --- Name
  public String getName();

  // --- Inputs
  public void addInput(Port input);

  public void addInputs(List<Port> inputs);

  public void removeInput(String name);

  public void removeInputs(List<String> names);
  
  public Port getInput( String name );
  
  public List<Port> getInputs();

  // --- Outputs
  public void addOutput(Port output);

  public void addOutputs(List<Port> outputs);

  public void removeOutput(String name);

  public void removeOutputs(List<String> names);
  
  public Port getOutput( String name );

  public List<Port> getOutputs();
  
  // --- Computation
  public void compute();
  
  // --- Cloneable
  public Module clone() throws CloneNotSupportedException;
}
