package synthlabgui.widgets.configPanel;

import synthlab.api.Port;

public interface AbstractConfigPanel {
    public void setPort(Port port);

    public void notifyPort(double value);

    public void setState(boolean enabled);
}
