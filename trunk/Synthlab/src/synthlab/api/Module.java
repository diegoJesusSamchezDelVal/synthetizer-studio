package synthlab.api;

import java.util.List;

/**
 * This is the module interface. It represents an analog synthesizer module having
 * input and output ports.
 */
public interface Module
{
  //================================================ Name
  /**
   * This is the name of the module ( e.g. "VCO", "VCF" ... )
   */
  public String getName();

  //================================================ Inputs
  /**
   * Add an input port to the module. This port will not be copied, but
   * rather shared (i.e. you can modify it, but be sure to synchronize
   * it beforehand)
   */
  public void addInput(Port input);

  /**
   * Add input ports to the module. These ports will not be copied, but
   * rather shared (i.e. you can modify them, but be sure to synchronize
   * them beforehand)
   */
  public void addInputs(List<Port> inputs);

  /**
   * The port with the specified name will be removed from the list of
   * input ports.
   * @param name the name of the port to be removed
   */
  public void removeInput(String name);

  /**
   * The ports with the specified names will be removed from the list of
   * input ports.
   * @param name the names of the ports to be removed
   */
  public void removeInputs(List<String> names);
  
  /**
   * Retrieve the input port having the specified name. Or null if this port
   * is not present.
   * @param name the name of the port we want to retrieve
   * @return the port having the given name, or null if not present
   */
  public Port getInput( String name );
  
  /**
   * Retrieve the list of all input ports.
   * @return the list of all input ports
   */
  public List<Port> getInputs();

  //================================================ Outputs
  /**
   * Add an output port to the module. This port will not be copied, but
   * rather shared (i.e. you can modify it, but be sure to synchronize
   * it beforehand)
   */
  public void addOutput(Port output);

  /**
   * Add output ports to the module. These ports will not be copied, but
   * rather shared (i.e. you can modify them, but be sure to synchronize
   * them beforehand)
   */
  public void addOutputs(List<Port> outputs);

  /**
   * The port with the specified name will be removed from the list of
   * output ports.
   * @param name the name of the port to be removed
   */
  public void removeOutput(String name);

  /**
   * The ports with the specified names will be removed from the list of
   * output ports.
   * @param name the names of the ports to be removed
   */
  public void removeOutputs(List<String> names);
  
  /**
   * Retrieve the output port having the specified name. Or null if this port
   * is not present.
   * @param name the name of the port we want to retrieve
   * @return the port having the given name, or null if not present
   */
  public Port getOutput( String name );

  /**
   * Retrieve the list of all output ports.
   * @return the list of all output ports
   */
  public List<Port> getOutputs();
  
  //================================================ Computation
  /**
   * This method is in charge of the actual computation of the module. That is,
   * the module should read its input ports, and write to its output ports.
   */
  public void compute();
  
  //================================================ Cloneable
  /**
   * This method should return a valid copy of the current module. All ports
   * should also be copied (deep copy).
   */
  public Module clone() throws CloneNotSupportedException;
}
