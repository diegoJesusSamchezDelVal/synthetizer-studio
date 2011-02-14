package synthlab.internal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Scripting
{
  public static ScriptEngine getJavascriptEngine()
  {
    if ( javascriptEngine_ == null )
    {
      // Retrieve a javascript interpreter (JRE 1.6 should contain Mozilla Rhino
      // by default)
      engineManager_ = new ScriptEngineManager();
      javascriptEngine_ = engineManager_.getEngineByName("JavaScript");
    }
    return javascriptEngine_;
  }
  
  private static ScriptEngine javascriptEngine_ = null;
  private static ScriptEngineManager engineManager_ = null;
}
