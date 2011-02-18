package synthlabgui.presentation.impl;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ModuleTransferable implements Transferable
{
  public ModuleTransferable(synthlab.api.Module module)
  {
    mimeType_ = DataFlavor.javaJVMLocalObjectMimeType + ";class=" + synthlab.api.Module.class.getName();
    try
    {
      moduleFlavor = new DataFlavor(mimeType_);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    module_ = module;
  }

  @Override
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
  {
    if (flavor.equals(moduleFlavor))
      return module_;
    else
      return null;
  }

  @Override
  public DataFlavor[] getTransferDataFlavors()
  {
    return new DataFlavor[] { moduleFlavor };
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    return flavor.equals(moduleFlavor);
  }

  private synthlab.api.Module module_;

  private String              mimeType_;

  public static DataFlavor    moduleFlavor;
}
