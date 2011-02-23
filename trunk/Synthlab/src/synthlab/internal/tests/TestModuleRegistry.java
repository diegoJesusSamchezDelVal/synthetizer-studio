package synthlab.internal.tests;

import junit.framework.TestCase;
import synthlab.api.ModuleRegistry;
import synthlab.internal.BasicModuleRegistry;
import synthlab.internal.modules.ModuleLFO;
import synthlab.internal.modules.ModuleOut;

public class TestModuleRegistry extends TestCase
{

  public TestModuleRegistry()
  {
    super();
  }

  public void testBasicModuleRegistry()
  {
    final ModuleLFO lfo = new ModuleLFO();
    final ModuleOut out = new ModuleOut();

    final ModuleRegistry moduleRegistry = new BasicModuleRegistry();

    assertFalse(moduleRegistry.registered(lfo));
    moduleRegistry.register(out);
    moduleRegistry.register(lfo);
    assertTrue(moduleRegistry.registered(lfo));

    moduleRegistry.unregister(out);
    assertFalse(moduleRegistry.registered(out));

  }
}
