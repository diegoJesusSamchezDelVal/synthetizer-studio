package synthlab.api;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;

import synthlab.internal.BasicScriptModule;

public class ModuleFactory
{
  /**
   * This will create a new module from a javascript source string. The module
   * will have the specified input and output ports. The javascript source can
   * use binding of ByteBuffers to read and write to its ports. For instance, if
   * you have a port named "iInput", you can access it in javascript as if you
   * have a ByteBuffer named iInput (and thus use getDouble() / putDouble() ).
   * You also have access to the SamplingBufferSize integer constant describing
   * the number of sample contained in each ByteBuffer (copied from
   * Scheduler.SamplingBufferSize).
   * 
   * @param name
   *          the name of the module
   * @param inputs
   *          the list of input ports
   * @param outputs
   *          the list of output ports
   * @param script
   *          the javascript source file
   * @return a new module, or null if there was an error.
   */
  public static Module createFromScript(String name, List<Port> inputs,
      List<Port> outputs, String script)
  {
    if (name.isEmpty() || script.isEmpty())
      return null;

    BasicScriptModule module = new BasicScriptModule(name);

    try
    {
      for (Port p : inputs)
        module.addInput(p.clone());

      for (Port p : outputs)
        module.addOutput(p);
    }
    catch (CloneNotSupportedException e)
    {
      e.printStackTrace();
    }

    module.setScript(script);

    return module;
  }

  /**
   * This will create a new module from an XML text file. There is a Schema
   * provided to help you create your own module. The computation done by the
   * module should be in javascript, following the convention described by
   * createFromScript() static function.
   * 
   * @param filename
   *          the file name of the XML file describing the module
   * @return a new module, or null if there was an error
   */
  public static Module createFromXML(String filename)
  {
    // TODO Add DTD/Schema validation here. If the XML document is malformed by
    // any way it will awfully crash!
    String moduleName = new String();
    List<Port> moduleInputs = new ArrayList<Port>();
    List<Port> moduleOutputs = new ArrayList<Port>();
    String moduleScript = new String();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    Document document;
    try
    {
      builder = factory.newDocumentBuilder();
      document = builder.parse(filename);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }

    // --- Start of schema validation
    SchemaFactory schemaFactory = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    String schemaContent = new String();
    schemaContent += "<?xml version=\"1.0\"?>\n";
    schemaContent += "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n";
    schemaContent += "<xs:element name=\"module\">\n";
    schemaContent += "  <xs:complexType>\n";
    schemaContent += "    <xs:sequence>\n";
    schemaContent += "      <xs:element name=\"inputs\" type=\"portListType\" minOccurs=\"1\" maxOccurs=\"1\" />\n";
    schemaContent += "      <xs:element name=\"outputs\" type=\"portListType\" minOccurs=\"1\"  maxOccurs=\"1\" />\n";
    schemaContent += "      <xs:element name=\"script\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />\n";
    schemaContent += "    </xs:sequence>\n";
    schemaContent += "  <xs:attribute name=\"name\" type=\"xs:string\" use=\"required\" />\n";
    schemaContent += "  </xs:complexType>\n";
    schemaContent += "</xs:element>\n";
    schemaContent += "<xs:complexType name=\"portListType\">\n";
    schemaContent += "  <xs:sequence>\n";
    schemaContent += "    <xs:element name=\"port\" type=\"portType\" minOccurs=\"0\" maxOccurs=\"unbounded\" />\n";
    schemaContent += "  </xs:sequence>\n";
    schemaContent += "</xs:complexType>\n";
    schemaContent += "<xs:complexType name=\"portType\">\n";
    schemaContent += "  <xs:attribute name=\"name\" type=\"xs:string\" use=\"required\" />\n";
    schemaContent += "  <xs:attribute name=\"type\" type=\"portTypeType\" use=\"required\" />\n";
    schemaContent += "  <xs:attribute name=\"unit\" type=\"portUnitType\" use=\"required\" />\n";
    schemaContent += "  <xs:attribute name=\"min\" type=\"xs:decimal\" use=\"required\" />\n";
    schemaContent += "  <xs:attribute name=\"max\" type=\"xs:decimal\" use=\"required\" />\n";
    schemaContent += "  <xs:attribute name=\"description\" type=\"xs:string\" use=\"required\" />\n";
    schemaContent += "</xs:complexType>\n";
    schemaContent += "<xs:simpleType name=\"portTypeType\">\n";
    schemaContent += "  <xs:restriction base=\"xs:string\">\n";
    for ( Port.ValueType t : Port.ValueType.values() )
      schemaContent += "    <xs:enumeration value=\"" + t.toString() +"\"/>\n";
    schemaContent += "  </xs:restriction>\n";
    schemaContent += "</xs:simpleType>\n";
    schemaContent += "<xs:simpleType name=\"portUnitType\">\n";
    schemaContent += "  <xs:restriction base=\"xs:string\">\n";
    for ( Port.ValueUnit u : Port.ValueUnit.values() )
      schemaContent += "    <xs:enumeration value=\"" + u.toString() +"\"/>\n";
    schemaContent += "  </xs:restriction>\n";
    schemaContent += "</xs:simpleType>\n";
    schemaContent += "</xs:schema>\n";
    Source schemaSource;
    try
    {
      schemaSource = new StreamSource(new ByteArrayInputStream(
          schemaContent.getBytes("UTF-8")));
      Schema schema = schemaFactory.newSchema(schemaSource);
      Validator validator = schema.newValidator();
      validator.validate(new DOMSource(document));
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, "There was an error validating your XML module:\n"+e.getMessage()+"\n\nWe used the following schema to validate your XML module:\n"+schemaContent, "XML Module validation error", JOptionPane.ERROR_MESSAGE);
      return null;
    }
    // --- End of schema validation
    Element moduleElement = document.getDocumentElement();
    Node inputsElement = moduleElement.getElementsByTagName("inputs").item(0);
    Node outputsElement = moduleElement.getElementsByTagName("outputs").item(0);
    Node scriptElement = moduleElement.getElementsByTagName("script").item(0);

    moduleName = moduleElement.getAttribute("name");
    moduleScript = scriptElement.getTextContent();

    for (int i = 0; i < inputsElement.getChildNodes().getLength(); ++i)
    {
      Node currentNode = inputsElement.getChildNodes().item(i);
      if (currentNode.getNodeType() != Node.ELEMENT_NODE)
        continue;
      String portName = currentNode.getAttributes().getNamedItem("name")
          .getTextContent();
      String portType = currentNode.getAttributes().getNamedItem("type")
          .getTextContent();
      String portUnit = currentNode.getAttributes().getNamedItem("unit")
          .getTextContent();
      String portMin = currentNode.getAttributes().getNamedItem("min")
          .getTextContent();
      String portMax = currentNode.getAttributes().getNamedItem("max")
          .getTextContent();
      ;
      String portDescription = currentNode.getAttributes()
          .getNamedItem("description").getTextContent();
      Port port = PortFactory.createFromDescription(
          portName,
          0,
          Port.ValueType.valueOf(portType),
          Port.ValueUnit.valueOf(portUnit),
          new Port.ValueRange(Double.parseDouble(portMin), Double
              .parseDouble(portMax)), portDescription);
      if (port != null)
        moduleInputs.add(port);
    }

    for (int i = 0; i < outputsElement.getChildNodes().getLength(); ++i)
    {
      Node currentNode = outputsElement.getChildNodes().item(i);
      if (currentNode.getNodeType() != Node.ELEMENT_NODE)
        continue;
      String portName = currentNode.getAttributes().getNamedItem("name")
          .getTextContent();
      String portType = currentNode.getAttributes().getNamedItem("type")
          .getTextContent();
      String portUnit = currentNode.getAttributes().getNamedItem("unit")
          .getTextContent();
      String portMin = currentNode.getAttributes().getNamedItem("min")
          .getTextContent();
      String portMax = currentNode.getAttributes().getNamedItem("max")
          .getTextContent();
      ;
      String portDescription = currentNode.getAttributes()
          .getNamedItem("description").getTextContent();
      Port port = PortFactory.createFromDescription(
          portName,
          0,
          Port.ValueType.valueOf(portType),
          Port.ValueUnit.valueOf(portUnit),
          new Port.ValueRange(Double.parseDouble(portMin), Double
              .parseDouble(portMax)), portDescription);
      if (port != null)
        moduleOutputs.add(port);
    }

    return ModuleFactory.createFromScript(moduleName, moduleInputs,
        moduleOutputs, moduleScript);
  }

  /**
   * This will create a new module by using decorating an existing class using
   * annotations. Not implemented yet.
   * 
   * @param object
   *          the annotated class to be decorated
   * @return a new module, or null if there was an error
   */
  public static Module createFromAnnotated(Object object)
  {
    // TODO
    return null;
  }

  /**
   * This will return a new instance of a module by deep copying an existing
   * prototype.
   * 
   * @param m
   *          the module to copy
   * @return a new module, or null if there was an error
   */
  public static Module createFromPrototype(Module m)
  {
    try
    {
      return m.clone();
    }
    catch (CloneNotSupportedException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
