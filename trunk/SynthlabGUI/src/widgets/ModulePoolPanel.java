package widgets;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import com.google.common.collect.BiMap;
import synthlab.api.*;

public class ModulePoolPanel extends JPanel implements MouseListener, MouseMotionListener, DropTargetListener
{
  private static final long serialVersionUID = 1943028163107894975L;

  public ModulePoolPanel()
  {
    super();

    setupGeneral();

    pool_ = ModulePoolFactory.createDefault();

    // TODO Here start insane test hack
    synthlab.api.Module vco1 = new synthlab.internal.modules.ModuleVCO();
    synthlab.api.Module vco2 = new synthlab.internal.modules.ModuleVCO();
    synthlab.api.Module vco3 = new synthlab.internal.modules.ModuleVCO();
    synthlab.api.Module out = new synthlab.internal.modules.ModuleOut();

    pool_.register(vco1);
    pool_.register(vco2);
    pool_.register(vco3);
    pool_.register(out);

    pool_.link(vco1.getOutput("oSignal"), out.getInput("iSignal"));
    pool_.link(vco2.getOutput("oSignal"), vco1.getInput("iFrequency"));
    pool_.link(vco3.getOutput("oSignal"), vco1.getInput("iConstant"));

    add(new Module(vco1));
    add(new Module(vco2));
    add(new Module(vco3));
    add(new Module(out));
    add(new Knob());
    
    setTransferHandler(new ModuleTransferHandler());
    try
    {
      getDropTarget().addDropTargetListener(this);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void addModule( Module module )
  {
    pool_.register(module.getWrapped());
    add(module);
    module.setBounds( dropPosition_.x-100, dropPosition_.y-20, module.getBounds().width, module.getBounds().height );
  }

  public void paint(Graphics g)
  {
    // Enable anti-aliasing
    RenderingHints renderHints = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderHints.put(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    ((Graphics2D) g).setRenderingHints(renderHints);

    // Draw background
    g.drawImage(background_, getWidth()/2-background_.getWidth(null)/2, getHeight()/2-background_.getHeight(null)/2, null);
    
    // Draw links
    drawLinks(g);

    // Render modules
    super.paint(g);
  }

  private void drawLinks(Graphics g)
  {
    // TODO insane hack to display hard-coded links
    Component[] components = getComponents();
    drawLink(g, components[1].getX() + 193, components[1].getY() + 36,
                components[0].getX() + 6, components[0].getY() + 36);
    drawLink(g, components[2].getX() + 193, components[2].getY() + 36,
                components[0].getX() + 6, components[0].getY() + 36+20);
    drawLink(g, components[0].getX() + 193, components[0].getY() + 36,
                components[3].getX() + 6, components[3].getY() + 36);
  }

  private void drawLink(Graphics g, int x0, int y0, int x1, int y1)
  {
    ((Graphics2D) g).setStroke(new BasicStroke(5));
    g.setColor(Color.darkGray);

    GeneralPath path = new GeneralPath();
    path.moveTo(x0, y0);
    path.curveTo(x0 + 100, y0, x1 - 100, y1, x1, y1);
    ((Graphics2D) g).draw(path);

    ((Graphics2D) g).setStroke(new BasicStroke(3));
    g.setColor(Color.yellow);
    ((Graphics2D) g).draw(path);
    g.setColor(Color.black);
    ((Graphics2D) g).setStroke(new BasicStroke(1));
  }

  private void setupGeneral()
  {
    setBorder(BorderFactory.createLineBorder(Color.black));
    setLayout(null);
    setBackground(Color.white);

    addMouseListener(this);
    addMouseMotionListener(this);

    setOpaque(false);

    background_ = new ImageIcon("background.jpg").getImage();
    
    setVisible(true);
  }

  public void updateLinks()
  {
    links_ = pool_.getLinks();
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
  }

  @Override
  public void mouseClicked(MouseEvent arg0)
  {
  }

  @Override
  public void mouseEntered(MouseEvent arg0)
  {
  }

  @Override
  public void mouseExited(MouseEvent arg0)
  {
  }

  @Override
  public void mousePressed(MouseEvent arg0)
  {
  }

  @Override
  public void mouseReleased(MouseEvent arg0)
  {
  }

  private ModulePool        pool_;
  private BiMap<Port, Port> links_;
  private Image background_;
  private Point dropPosition_;

  @Override
  public void dragEnter(DropTargetDragEvent arg0)
  {
  }

  @Override
  public void dragExit(DropTargetEvent arg0)
  {
  }

  @Override
  public void dragOver(DropTargetDragEvent e)
  {
    dropPosition_ = e.getLocation();
  }

  @Override
  public void drop(DropTargetDropEvent e)
  {
  }

  @Override
  public void dropActionChanged(DropTargetDragEvent arg0)
  {
  }
}
