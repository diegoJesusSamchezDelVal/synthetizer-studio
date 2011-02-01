package synthlab.internal.modules;

import synthlab.api.Port;

public class BasicPort implements Port
{
  public BasicPort(String name, Integer value, Port link)
  {
    setName(name);
    setValue(value);
    setLink(link);
  }

  @Override
  public void setName(String name)
  {
    name_ = name;
  }

  @Override
  public String getName()
  {
    return name_;
  }

  @Override
  public void setValue(Integer value)
  {
    value_ = value;
  }

  @Override
  public Integer getValue()
  {
    return value_;
  }

  @Override
  public void setLink(Port link)
  {
    link_ = link;
  }

  @Override
  public Port getLink()
  {
    return link_;
  }

  @Override
  public void refresh()
  {

  }

  private String  name_;
  private Integer value_;
  private Port    link_;
}
