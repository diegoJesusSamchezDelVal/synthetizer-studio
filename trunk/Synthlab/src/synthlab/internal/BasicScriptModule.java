package synthlab.internal;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;

/**
 * This is a simple implementation of a basic module supporting a javascript function
 * in place of its computation function
 */
public class BasicScriptModule extends BasicModule
{
  /**
   * The source file of the javascript computation
   */
  private String script_;

  public BasicScriptModule(String name)
  {
    super(name);
  }
  
  public void setScript( String script )
  {
    script_ = script;
  }
  
  public String getScript()
  {
    return script_;
  }

  @Override
  public void compute()
  {
    // Retrieve the global singleton handling the javascript interpreter & its context
    ScriptEngine javascriptEngine = Scripting.getJavascriptEngine();

    // TODO should synchronize inputs&ouputs here
    for (Port p : getInputs())
      p.getValues().clear();
    for (Port p : getOutputs())
      p.getValues().clear();

    // Insert bindings for our inputs and outputs byte buffers
    for (Port p : getInputs())
      javascriptEngine.put( p.getName(), p.getValues() );
    for (Port p : getOutputs())
      javascriptEngine.put(p.getName(), p.getValues() );
    // Insert binding for our SchedulerSamplingBuffer size variable
    javascriptEngine.put( "SamplingBufferSize", Scheduler.SamplingBufferSize );
    
    try
    {
      // Evaluate the script
      javascriptEngine.eval(script_);
    }
    catch (ScriptException e)
    {
      e.printStackTrace();
    }
  }
  
  @Override
  public Module clone() throws CloneNotSupportedException
  {
    BasicScriptModule module = new BasicScriptModule( getName() );
    
    // Copy input ports
    for ( Port p : getInputs() )
      module.addInput( p.clone() );
    
    // Copy output ports
    for ( Port p : getOutputs() )
      module.addOutput( p.clone() );
    
    // Copy script
    module.setScript( this.getScript() );
    
    return module;
  }
}
