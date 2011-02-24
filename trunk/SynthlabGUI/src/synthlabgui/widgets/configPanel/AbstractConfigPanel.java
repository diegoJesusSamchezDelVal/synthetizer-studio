package synthlabgui.widgets.configPanel;

import java.util.Observer;
import synthlab.api.Port;

/**
 * Interface of configure component. All configure component should implement this interface.
 * */
public interface AbstractConfigPanel extends Observer
{
  /**
   * Reference the port to be configured.
   * 
   * @param port
   *          the port to be configured
   * */
  public void setPort(Port port);

  /**
   * Notify its port to change to a new value. Nothing will be sent if this component has not connected to a port.
   * 
   * @param value
   *          value to notify
   * */
  public void notifyPort(double value);

  /**
   * enable or disable this component. If the component is disabled, nothing will be sent to its port.
   * 
   * @param enabled
   *          condition of the state
   * */
  public void setState(boolean enabled);
}
