package widgets;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Map;

import javax.swing.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import synthlab.api.*;

public class ModulePoolPanel extends JPanel implements MouseListener, MouseMotionListener, DropTargetListener
{
  private static final long serialVersionUID = 1943028163107894975L;

  public ModulePoolPanel()
  {
    super();

    setupGeneral();

    pool_ = ModulePoolFactory.createDefault();
    
    setTransferHandler(new ModuleTransferHandler());
    try
    {
      getDropTarget().addDropTargetListener(this);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    links_ =  HashBiMap.create();
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
    
    if ( linking_ )
    {
      drawLink( g, linkStart_.x, linkStart_.y, linkCurrent_.x, linkCurrent_.y);
    }

    // Render modules
    super.paint(g);
  }
  
  public boolean isLinking()
  {
    return linking_;
  }

  private void drawLinks(Graphics g)
  {
    // TODO insane hack to display hard-coded links
    for ( Map.Entry<PortHandler, PortHandler> entry : links_.entrySet() )
    {
      drawLink( g,
          (int)(entry.getKey().getBounds().getX() + entry.getKey().getParent().getBounds().getX()),
          (int)(entry.getKey().getBounds().getY() + entry.getKey().getParent().getBounds().getY())+6,
          (int)(entry.getValue().getBounds().getX() + entry.getValue().getParent().getBounds().getX()),
          (int)(entry.getValue().getBounds().getY() + entry.getValue().getParent().getBounds().getY())+6
              );
    }
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
    
    linking_ = false;
    
    setVisible(true);
  }

  public void updateLinks()
  {
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    linkCurrent_ = e.getPoint();
    repaint();
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
    endDrag( null, null );
  }

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
  
  public void updateDrag( Module module, PortHandler port, Point e, Point p )
  {
    linking_ = true;
    linkStart_ = e;
    linkCurrent_ = p;
    portStart_ = port;
    repaint();
  }
  
  public void endDrag( Module module, PortHandler port )
  {
    linking_ = false;
    repaint();
    Component c = findComponentAt(linkCurrent_);
    if ( c.getClass().equals(PortHandler.class) )
    {
      link( portStart_, (PortHandler)c );
    }
  }
  
  public void link( PortHandler output, PortHandler input )
  {
    System.out.println( output.getWrapped().getName() + " -> " + input.getWrapped().getName() );
    pool_.link(output.getWrapped(), input.getWrapped());
    links_.put(output, input);
  }
  
  private ModulePool        pool_;
  private Image background_;
  private Point dropPosition_;
  private boolean linking_;
  private Point linkStart_;
  private Point linkCurrent_;
  private PortHandler portStart_; 
  private BiMap<PortHandler,PortHandler> links_;
}
