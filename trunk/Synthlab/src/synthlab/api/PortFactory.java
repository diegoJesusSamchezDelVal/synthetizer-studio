package synthlab.api;

import synthlab.api.Port.ValueRange;
import synthlab.api.Port.ValueType;
import synthlab.api.Port.ValueUnit;
import synthlab.internal.BasicPort;

public class PortFactory
{
  public static Port createFromDescription(String name, double value, ValueType type, ValueUnit unit,
      ValueRange range, String description)
  {
    return new BasicPort( name, value, type, unit, range, description );
  }
}
