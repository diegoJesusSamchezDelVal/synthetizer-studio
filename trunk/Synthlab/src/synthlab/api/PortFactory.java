package synthlab.api;

import synthlab.api.Port.ValueRange;
import synthlab.api.Port.ValueType;
import synthlab.api.Port.ValueUnit;
import synthlab.internal.BasicPort;

public class PortFactory
{
  /**
   * This method will return a new port using the information provided.
   * @param name the name of the port (e.g. "VCO")
   * @param value the initial value of the port (most of the time 0)
   * @param type the type of values that this port will handle
   * @param unit the unit in which the values are stored
   * @param range the range of values that are stored in this port
   * @param description a short description of the usage of this port
   * @return a new instance of a subclass of the Port interface respecting the values provided.
   */
  public static Port createFromDescription(String name, double value, ValueType type, ValueUnit unit,
      ValueRange range, String description)
  {
    return new BasicPort( name, value, type, unit, range, description );
  }
}
