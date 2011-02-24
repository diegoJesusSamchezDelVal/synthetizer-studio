package synthlabgui.widgets.configPanel.keyboard;

/**
 * Interface of a KeyboardListener. Any class that wants to receive KeyboardEvent should implement this itnerface.
 * */
public interface KeyboardListener
{
  /**
   * Invoked when a key action is occurred.
   * 
   * @param event
   *          the keyboard event.
   * */
  public void keyActionPerformed(KeyboardEvent event);
}
