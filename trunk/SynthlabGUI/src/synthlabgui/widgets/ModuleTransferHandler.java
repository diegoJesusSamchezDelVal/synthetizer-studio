package synthlabgui.widgets;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class ModuleTransferHandler extends TransferHandler
{
  private static final long serialVersionUID = 5108008059069726758L;

  @Override
  public boolean canImport(JComponent cp, DataFlavor[] df)
  {
    for (int i = 0; i < df.length; ++i)
      if (df[i].equals(ModuleTransferable.moduleFlavor) && cp.getClass().equals(ModulePoolPanel.class))
        return true;
    return false;
  }

  @Override
  public boolean importData(JComponent cp, Transferable df)
  {
    if (df.isDataFlavorSupported(ModuleTransferable.moduleFlavor))
    {
      try
      {
        synthlab.api.Module m = (synthlab.api.Module) df.getTransferData(ModuleTransferable.moduleFlavor);
        Module module = new Module(m);
        ((ModulePoolPanel) cp).addModule(module);
        ((ModulePoolPanel) cp).repaint();
        return true;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return false;
  }

  @Override
  protected Transferable createTransferable(JComponent cp)
  {
    return new ModuleTransferable(((ModulePrototype) cp).getWrapped());
  }

  @Override
  protected void exportDone(JComponent cp, Transferable t, int type)
  {
    if (type == TransferHandler.COPY)
    {
      // Nothing to do atm
    }
  }

  @Override
  public int getSourceActions(JComponent cp)
  {
    return COPY;
  }
}
