package synthlab.internal;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;

public class BasicScriptModule extends BasicModule
{
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
    ScriptEngine javascriptEngine = Scripting.getJavascriptEngine();

    // TODO should synchronize inputs&ouputs here
    for (Port p : getInputs())
      p.getValues().clear();
    for (Port p : getOutputs())
      p.getValues().clear();

    for (Port p : getInputs())
      javascriptEngine.put( p.getName(), p.getValues() );
    for (Port p : getOutputs())
      javascriptEngine.put(p.getName(), p.getValues() );
    javascriptEngine.put( "SamplingBufferSize", Scheduler.SamplingBufferSize );
    
    try
    {
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
    
    for ( Port p : getInputs() )
      module.addInput( p.clone() );
    
    for ( Port p : getOutputs() )
      module.addOutput( p.clone() );
    
    module.setScript( this.getScript() );
    
    return module;
  }
}
