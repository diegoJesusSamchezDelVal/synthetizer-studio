package widgets.knob;

public class KnobEvent
{
  
  private int value;
  
  private NumberKnob source;
  
  public KnobEvent(AbstractKnob knob, int value) {
      this.value = value;
      this.source = source;
  }
  
  public int getValue() {
    return value;
  }
  
  

}
