package synthlabgui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import synthlabgui.widgets.MainWindow;

public class SynthlabGUI
{
  public static void main(String[] args)
  {
    // Look up for 'Nimbus" look and feel
    try
    {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
      {
        if ("Nimbus".equals(info.getName()))
        {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    }
    catch (Exception e)
    {
      // Nimbus look and feel not available :(
      // Fallback to default style
    }
    new MainWindow();
  }
}
