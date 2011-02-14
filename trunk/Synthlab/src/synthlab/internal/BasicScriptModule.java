package synthlab.internal;

import java.nio.ByteBuffer;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Scheduler;
import synthlab.internal.modules.ModuleEnvelope;

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
    
    module.addInputs( this.getInputs() );
    module.addOutputs( this.getOutputs() );
    
    module.setScript( this.getScript() );
    
    return module;
  }
}
