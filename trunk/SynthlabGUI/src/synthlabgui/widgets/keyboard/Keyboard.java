package synthlabgui.widgets.keyboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Keyboard extends JPanel
{
  public Keyboard(int nbKeys)
  {
    setLayout(null);
    setBackground(Color.black);
    WhiteKey whiteKey = new WhiteKey();
    BlackKey blackKey = new BlackKey();
    int width = whiteKey.getWidth() * nbKeys;
    int height = whiteKey.getHeight();
    setSize(width, height);
    setPreferredSize(getSize());
    int decx = 0;
    int decy = 0;
    whiteKeys = new ArrayList<Key>();
    blackKeys = new ArrayList<Key>();
    for (int i = 0; i < nbKeys; i++)
    {
      WhiteKey key = new WhiteKey();
      key.addMouseListener(new InternalListener());
      key.setLocation(i * whiteKey.getWidth() + decx, decy);
      whiteKeys.add(key);
      add(key);
    }
    int decalBlack = decx + whiteKey.getWidth() - blackKey.getWidth() / 2;
    for (int i = 0; i < nbKeys; i++)
    {
      if (i % 7 == 0 || i % 7 == 1 || i % 7 == 3 || i % 7 == 4 || i % 7 == 5)
      {
        BlackKey key = new BlackKey();
        key.addMouseListener(new InternalListener());
        key.setLocation(decalBlack + whiteKey.getWidth() * i, decy);
        blackKeys.add(key);
        add(key, 0);
      }
    }
  }

  public void paint(Graphics gc)
  {
    super.paint(gc);
    requestFocus(true);
  }

  public void addKeyboardListener(KeyboardListener listener)
  {
    listeners.add(listener);
  }

  public void removeKeyboardListener(KeyboardListener listener)
  {
    listeners.remove(listener);
  }

  private void notifyListeners(KeyboardEvent keyboardEvent)
  {
    for (KeyboardListener l : listeners)
    {
      l.keyPressed(keyboardEvent);
    }
  }

  private class InternalListener implements MouseListener
  {
    @SuppressWarnings("unused")
    private class KeyboardListener implements MouseListener
    {
      private Color keyPressedColor = Color.lightGray;

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
      public void mousePressed(MouseEvent e)
      {
        ((JPanel) e.getSource()).setBackground(keyPressedColor);
        double value = 0;
        if (e.getSource() instanceof WhiteKey)
        {
          value = whiteKeyFrequences[whiteKeys.indexOf(e.getSource())];
        }
        else
        {
          value = blackKeyFrequences[blackKeys.indexOf(e.getSource())];
        }
        notifyListeners(new KeyboardEvent((Key) e.getSource(), value));
        repaint();
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
        Key key = (Key) e.getSource();
        ((JPanel) e.getSource()).setBackground(key.getColor());
        repaint();
      }
    }

    @Override
    public void mouseClicked(MouseEvent arg0)
    {
      // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
      // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
      // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
      // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
      // TODO Auto-generated method stub
    }
  }

  private static final long           serialVersionUID   = -4122417491103561122L;

  // private float[] frequences = { 261.6f, // DO
  // 293.7f, // RE
  // 329.7f, // MI
  // 349.2f, // FA
  // 392.0f, // SOL
  // 440.0f, // LA
  // 493.9f, // SI
  // 523.2f, // DO
  // 587.4f, // RE
  // 659.4f // MI
  // };
  private ArrayList<KeyboardListener> listeners          = new ArrayList<KeyboardListener>();

  private double[]                    whiteKeyFrequences = { 261.6, // DO
      293.7, // RE
      329.7, // MI
      349.2, // FA
      392.0, // SOL
      440.0, // LA
      493.9, // SI
      523.2, // DO
      587.4, // RE
      659.4                                             };                                  // MI

  private double[]                    blackKeyFrequences = { 277.18, // DO#
      311.13, // RE#
      369.99, // FA#
      415.30, // SO#
      466.16, // LA#
      554.37, // DO#
      622.25                                            // RE#
                                                         };

  ArrayList<Key>                      whiteKeys;

  ArrayList<Key>                      blackKeys;
}
