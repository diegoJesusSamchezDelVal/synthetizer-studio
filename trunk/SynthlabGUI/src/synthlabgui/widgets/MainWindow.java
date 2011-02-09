package synthlabgui.widgets;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import synthlab.internal.Audio;

public class MainWindow extends JFrame implements WindowListener
{
  private static final long   serialVersionUID = 1910008960712226631L;

  private JMenuBar            menuBar_;

  private ModuleRegistryPanel moduleRegistryPanel_;

  private ModulePoolPanel     modulePoolPanel_;

  public MainWindow()
  {
    super();
    setupMenuBar();
    setupPanels();
    setupGeneral();
  }

  private void setupPanels()
  {
    moduleRegistryPanel_ = new ModuleRegistryPanel();
    modulePoolPanel_ = new ModulePoolPanel();
    JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, moduleRegistryPanel_, modulePoolPanel_);
    splitter.setDividerLocation(250);
    getContentPane().add(splitter);
  }

  private void setupGeneral()
  {
    setTitle("Synthlab GUI");
    setSize(1000, 800);
    addWindowListener(this);
    setVisible(true);
  }

  private void setupMenuBar()
  {
    menuBar_ = new JMenuBar();
    JMenu menuFile = new JMenu("File");
    JMenu menuEdit = new JMenu("Edit");
    JMenu menuSynth = new JMenu("Synthetizer");
    JMenu menuHelp = new JMenu("Help");
    JMenuItem itemFileNew = new JMenuItem("New");
    JMenuItem itemFileOpen = new JMenuItem("Open");
    JMenuItem itemFileSave = new JMenuItem("Save");
    JMenuItem itemFileSaveAs = new JMenuItem("Save as...");
    JMenuItem itemFileClose = new JMenuItem("Close");
    JMenuItem itemFileQuit = new JMenuItem("Quit");
    JMenuItem itemEditCopy = new JMenuItem("Copy");
    JMenuItem itemEditCut = new JMenuItem("Cut");
    JMenuItem itemEditPaste = new JMenuItem("Paste");
    JMenuItem itemSynthAdd = new JMenuItem("Add");
    JMenuItem itemSynthRemove = new JMenuItem("Remove");
    JMenuItem itemHelpAbout = new JMenuItem("About");
    menuFile.add(itemFileNew);
    menuFile.add(itemFileOpen);
    menuFile.add(itemFileSave);
    menuFile.add(itemFileSaveAs);
    menuFile.add(itemFileClose);
    menuFile.add(itemFileQuit);
    menuEdit.add(itemEditCopy);
    menuEdit.add(itemEditCut);
    menuEdit.add(itemEditPaste);
    menuSynth.add(itemSynthAdd);
    menuSynth.add(itemSynthRemove);
    menuHelp.add(itemHelpAbout);
    menuBar_.add(menuFile);
    menuBar_.add(menuEdit);
    menuBar_.add(menuSynth);
    menuBar_.add(menuHelp);
    setJMenuBar(menuBar_);
  }

  @Override
  public void windowActivated(WindowEvent e)
  {
  }

  @Override
  public void windowClosed(WindowEvent e)
  {
  }

  @Override
  public void windowClosing(WindowEvent e)
  {
    Audio.stopLine();
    Audio.closeLine();
    System.out.println("Au revoir:)");
    System.exit(0);
  }

  @Override
  public void windowDeactivated(WindowEvent e)
  {
  }

  @Override
  public void windowDeiconified(WindowEvent e)
  {
  }

  @Override
  public void windowIconified(WindowEvent e)
  {
  }

  @Override
  public void windowOpened(WindowEvent e)
  {
  }
}
