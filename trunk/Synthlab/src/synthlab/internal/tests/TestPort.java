package synthlab.internal.tests;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

import org.junit.Test;

import synthlab.api.Module;
import synthlab.api.Port;
import synthlab.api.Port.ValueRange;
import synthlab.api.Port.ValueType;
import synthlab.api.Port.ValueUnit;
import synthlab.api.Scheduler;
import synthlab.internal.BasicPort;
import synthlab.internal.modules.ModuleVCO;

public class TestPort extends TestCase
{

  final String     name         = "test";
  final double     defaultValue = 50.0;
  final ValueType  type         = ValueType.CONTINUOUS;
  final ValueUnit  unit         = ValueUnit.HERTZ;
  final ValueRange range        = new ValueRange(0.0, 100.0);

  public TestPort()
  {
    super();
  }

  @Test
  public void testPortBasic()
  {
    final Port port = new BasicPort(name, defaultValue, type, unit, range, "");

    port.getValues().clear();

    assertEquals(name, port.getName());
    assertEquals(defaultValue, port.getValues().getDouble());
    assertEquals(type, port.getValueType());
    assertEquals(unit, port.getValueUnit());
    assertEquals(range, port.getValueRange());

  }

  @Test
  public void testData()
  {
    final Port port = new BasicPort(name, defaultValue, type, unit, range, "");
    final double value = 25.0;
    port.setValues(value);
    port.getValues().clear();
    for (int index = 0; index < Scheduler.SamplingBufferSize; index++)
    {
      assertEquals(value, port.getValues().getDouble(), 0.0);
    }

    final ByteBuffer buffer = ByteBuffer.allocate(Scheduler.SamplingBufferSize
        * Double.SIZE / 8);
    for (int index = 0; index < Scheduler.SamplingBufferSize; index++)
    {
      buffer.putDouble(value);
    }
    port.setValues(buffer);
    port.getValues().clear();
    for (int index = 0; index < Scheduler.SamplingBufferSize; index++)
    {
      assertEquals(value, port.getValues().getDouble(), 0.0);
    }
  }

  @Test
  public void testConnection()
  {
    final Port port = new BasicPort(name, defaultValue, type, unit, range, "");
    assertNull(port.getModule());
    assertFalse(port.isInput());
    assertFalse(port.isOutput());

    final Module module = new ModuleVCO();
    module.addInput(port);

    assertEquals(module, port.getModule());
    assertTrue(port.isInput());
    assertFalse(port.isOutput());

    module.removeInput(name);
    assertNull(port.getModule());
    assertFalse(port.isInput());
    assertFalse(port.isOutput());

    module.addOutput(port);
    assertFalse(port.isInput());
    assertTrue(port.isOutput());

    module.removeOutput(name);
    assertNull(port.getModule());
    assertFalse(port.isInput());
    assertFalse(port.isOutput());

    port.setLinked(true);
    assertTrue(port.isLinked());

    port.setLinked(false);
    assertFalse(port.isLinked());

  }

}
