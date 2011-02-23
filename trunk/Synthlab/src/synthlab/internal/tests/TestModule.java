package synthlab.internal.tests;

import junit.framework.TestCase;

import org.junit.Test;

import synthlab.api.Port;
import synthlab.api.Port.ValueRange;
import synthlab.api.Port.ValueType;
import synthlab.api.Port.ValueUnit;
import synthlab.internal.BasicModule;
import synthlab.internal.BasicPort;
import synthlab.internal.modules.ModuleVCO;

public class TestModule extends TestCase
{

  final String     name         = "test";
  final double     defaultValue = 50.0;
  final ValueType  type         = ValueType.CONTINUOUS;
  final ValueUnit  unit         = ValueUnit.HERTZ;
  final ValueRange range        = new ValueRange(0.0, 100.0);

  public TestModule()
  {
    super();
  }

  @Test
  public void testPortBasic()
  {
    final BasicModule module = new ModuleVCO();
    final Port port = new BasicPort(name, defaultValue, type, unit, range, "");
    module.addInput(port);
    assertEquals(port, module.getInput(name));

    module.removeInput(name);
    assertNull(module.getInput(name));

    module.addOutput(port);
    assertEquals(port, module.getOutput(name));

    module.removeOutput(name);
    assertNull(module.getOutput(name));

  }
}
