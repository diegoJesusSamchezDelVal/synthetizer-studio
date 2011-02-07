package widgets.keyboard;

import java.awt.Color;

public class BlackKey extends Key {
  
    public BlackKey() {
        super();
        setSize(10, 40);
        setPreferredSize(this.getSize());
        setBackground(Color.BLACK);
    }
    
    public Color getColor(){
      return Color.BLACK;
    }
}
