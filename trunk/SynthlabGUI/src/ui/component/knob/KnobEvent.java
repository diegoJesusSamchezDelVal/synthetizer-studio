package ui.component.knob;

public class KnobEvent
{
  
  private int value;
  
  private Knob2 source;
  
  public KnobEvent(Knob2 knob, int value) {
      this.value = value;
      this.source = source;
  }
  
  public int getValue() {
    return value;
  }
  
  

}
