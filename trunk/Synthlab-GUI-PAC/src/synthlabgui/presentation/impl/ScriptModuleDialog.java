package synthlabgui.presentation.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import synthlab.api.ModuleFactory;
import synthlab.api.Port;
import synthlab.api.PortFactory;

public class ScriptModuleDialog extends JDialog
{
  private static final long serialVersionUID = 2663092149264882952L;

  public ScriptModuleDialog(MainWindow f)
  {
    super(f);
    mainWindow_ = f;
    setupGeneral();
  }

  private void setupGeneral()
  {
    setVisible(true);
    setBounds(0, 0, 400, 800);
    setSize(400, 800);
    setPreferredSize(new Dimension(400, 800));
    setMaximumSize(new Dimension(400, 800));
    setMinimumSize(new Dimension(400, 800));
    setResizable(false);
    setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
    // --- Module meta
    // Module name
    JPanel moduleMeta = new JPanel();
    moduleMeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    moduleMeta.setLayout(new GridLayout(0, 2));
    moduleMeta.setBorder(BorderFactory.createTitledBorder("Module configuration"));
    moduleName_ = new JTextField();
    JLabel moduleNameLbl = new JLabel("Module name:");
    moduleNameLbl.setLabelFor(moduleName_);
    moduleMeta.add(moduleNameLbl);
    moduleMeta.add(moduleName_);
    add(moduleMeta);
    // --- Port meta
    // Port name
    JPanel portMeta = new JPanel();
    portMeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    portMeta.setLayout(new GridLayout(0, 2));
    portMeta.setBorder(BorderFactory.createTitledBorder("Ports configuration"));
    // Port name
    JLabel portNameLbl = new JLabel("Port name:");
    portName_ = new JTextField();
    portNameLbl.setLabelFor(portName_);
    portMeta.add(portNameLbl);
    portMeta.add(portName_);
    // Port type
    JLabel portTypeLbl = new JLabel("Port type:");
    portType_ = new JComboBox(Port.ValueType.values());
    portTypeLbl.setLabelFor(portType_);
    portMeta.add(portTypeLbl);
    portMeta.add(portType_);
    // Port unit
    JLabel portUnitLbl = new JLabel("Port unit:");
    portUnit_ = new JComboBox(Port.ValueUnit.values());
    portUnitLbl.setLabelFor(portUnit_);
    portMeta.add(portUnitLbl);
    portMeta.add(portUnit_);
    // Port value range
    JLabel portRangeStartLbl = new JLabel("Port range min");
    JLabel portRangeEndLbl = new JLabel("Port range max:");
    portRangeStart_ = new JTextField("-1");
    portRangeEnd_ = new JTextField("1");
    portRangeStartLbl.setLabelFor(portRangeStart_);
    portRangeEndLbl.setLabelFor(portRangeEnd_);
    portMeta.add(portRangeStartLbl);
    portMeta.add(portRangeStart_);
    portMeta.add(portRangeEndLbl);
    portMeta.add(portRangeEnd_);
    // Port description
    JLabel portDescriptionLbl = new JLabel("Port description:");
    portDescription_ = new JTextField();
    portDescriptionLbl.setLabelFor(portDescription_);
    portMeta.add(portDescriptionLbl);
    portMeta.add(portDescription_);
    // Port add
    JButton addInputButton = new JButton("Add input port");
    JButton addOutputButton = new JButton("Add output port");
    addInputButton.addActionListener(new AddInputPort());
    addOutputButton.addActionListener(new AddOutputPort());
    portMeta.add(addInputButton);
    portMeta.add(addOutputButton);
    // Port list
    inputListModel_ = new DefaultListModel();
    outputListModel_ = new DefaultListModel();
    JList portInputsList = new JList(inputListModel_);
    JList portOutputsList = new JList(outputListModel_);
    portInputsList.setMinimumSize(new Dimension(0, 300));
    portMeta.add(portInputsList);
    portMeta.add(portOutputsList);
    // add port meta
    add(portMeta);
    // --- Script meta
    JPanel scriptMeta = new JPanel();
    // portMeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    scriptMeta.setBorder(BorderFactory.createTitledBorder("Script configuration"));
    scriptMeta.setLayout(new GridLayout(0, 1));
    // script text
    JLabel scriptTextLbl = new JLabel("Script content:");
    StyledEditorKit editorKit = new StyledEditorKit() {
      private static final long serialVersionUID = -4282735963888956486L;

      @Override
      public Document createDefaultDocument()
      {
        return new SyntaxDocument();
      }
    };
    scriptText_ = new JEditorPane();
    scriptText_.setEditorKitForContentType("text/java", editorKit);
    scriptText_.setContentType("text/java");
    scriptText_.setEditorKit(new StyledEditorKit());
    scriptText_.setDocument(new SyntaxDocument());
    scriptText_
        .setText("// Type in your module computation here (javascript)\n// You have access to all your inputs and outputs as a direct bindings from Java ByteBuffers.\n// You also have access to the SamplingBufferSize variable.\n\n// Example script:\nvar i;\nfor ( i=0; i<SamplingBufferSize; ++i )\n{\n  oSignal.putDouble( iSignal.getDouble() / 2 );\n}\n");
    scriptTextLbl.setLabelFor(scriptText_);
    scriptMeta.add(new JScrollPane(scriptText_));
    // add script meta
    add(scriptMeta);
    // add buttons
    JButton createModuleButton = new JButton("Add module to registry");
    createModuleButton.addActionListener(new CreateModule());
    add(createModuleButton);
    portMeta.validate();
  }

  private Port createPort()
  {
    String portName = portName_.getText();
    Port.ValueType portType = (Port.ValueType) portType_.getSelectedItem();
    Port.ValueUnit portUnit = (Port.ValueUnit) portUnit_.getSelectedItem();
    double portRangeStart = 0;
    double portRangeEnd = 0;
    try
    {
      portRangeStart = Double.valueOf(portRangeStart_.getText()).doubleValue();
      portRangeEnd = Double.valueOf(portRangeEnd_.getText()).doubleValue();
    }
    catch (Exception e)
    {
      JOptionPane
          .showMessageDialog(
              this,
              "<html>There was an error while trying to convert port range values (min/max) to double.<br/>Check the information you entered and try again.</html>",
              "Error creating port module", JOptionPane.ERROR_MESSAGE);
      return null;
    }
    String portDescription = portDescription_.getText();
    return PortFactory.createFromDescription(portName, 0, portType, portUnit, new Port.ValueRange(portRangeStart,
        portRangeEnd), portDescription);
  }

  public synthlab.api.Module getModule()
  {
    return module_;
  }

  private void createModule()
  {
    List<Port> inputs = new ArrayList<Port>();
    List<Port> outputs = new ArrayList<Port>();
    for (Object p : inputListModel_.toArray())
      inputs.add((Port) p);
    for (Object p : outputListModel_.toArray())
      outputs.add((Port) p);
    module_ = ModuleFactory.createFromScript(moduleName_.getText(), inputs, outputs, scriptText_.getText());
    if (module_ != null)
    {
      mainWindow_.getRegistryPanel().addModule(module_);
      dispose();
    }
    else
    {
      JOptionPane
          .showMessageDialog(
              this,
              "<html>There was an error while trying to register your module.<br/>Check the information you entered and try again.</html>",
              "Error registrering module", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void addInputPort(Port p)
  {
    inputListModel_.addElement(p);
  }

  private void addOutputPort(Port p)
  {
    outputListModel_.addElement(p);
  }

  private MainWindow          mainWindow_;

  private DefaultListModel    outputListModel_;

  private DefaultListModel    inputListModel_;

  private JTextField          portName_;

  private JComboBox           portType_;

  private JComboBox           portUnit_;

  private JTextField          portRangeStart_;

  private JTextField          portRangeEnd_;

  private JTextField          portDescription_;

  private JTextField          moduleName_;

  private JEditorPane         scriptText_;

  private synthlab.api.Module module_;

  // ====================================================================
  // ACTION LISTENERS
  // ====================================================================
  private class AddInputPort implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      Port p = createPort();
      if (p != null)
        addInputPort(p);
    }
  }

  private class AddOutputPort implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      Port p = createPort();
      if (p != null)
        addOutputPort(p);
    }
  }

  private class CreateModule implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      createModule();
    }
  }

  // ====================================================================
  // SYNTAX HIGHLIGHTER
  // ====================================================================
  class SyntaxDocument extends DefaultStyledDocument
  {
    private static final long     serialVersionUID = -1832399623044280794L;

    private DefaultStyledDocument doc;

    private Element               rootElement;

    private boolean               multiLineComment;

    private MutableAttributeSet   normal;

    private MutableAttributeSet   keyword;

    private MutableAttributeSet   comment;

    private MutableAttributeSet   quote;

    private HashSet<String>       keywords;

    public SyntaxDocument()
    {
      doc = this;
      rootElement = doc.getDefaultRootElement();
      putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
      normal = new SimpleAttributeSet();
      StyleConstants.setForeground(normal, Color.black);
      comment = new SimpleAttributeSet();
      StyleConstants.setForeground(comment, Color.gray);
      StyleConstants.setItalic(comment, true);
      keyword = new SimpleAttributeSet();
      StyleConstants.setForeground(keyword, Color.blue);
      quote = new SimpleAttributeSet();
      StyleConstants.setForeground(quote, Color.red);
      keywords = new HashSet<String>();
      String[] jsKeywords = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
          "continue", "debugger", "default", "delete", "do", "double", "else", "enum", "export", "extends", "false",
          "final", "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof",
          "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return",
          "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
          "typeof", "var", "void", "volatile", "while", "with" };
      for (String k : jsKeywords)
        keywords.add(k);
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
    {
      if (str.equals("{"))
        str = addMatchingBrace(offset);
      super.insertString(offset, str, a);
      processChangedLines(offset, str.length());
    }

    public void remove(int offset, int length) throws BadLocationException
    {
      super.remove(offset, length);
      processChangedLines(offset, 0);
    }

    public void processChangedLines(int offset, int length) throws BadLocationException
    {
      String content = doc.getText(0, doc.getLength());
      // The lines affected by the latest document update
      int startLine = rootElement.getElementIndex(offset);
      int endLine = rootElement.getElementIndex(offset + length);
      // Make sure all comment lines prior to the start line are commented
      // and determine if the start line is still in a multi line comment
      setMultiLineComment(commentLinesBefore(content, startLine));
      // Do the actual highlighting
      for (int i = startLine; i <= endLine; i++)
      {
        applyHighlighting(content, i);
      }
      // Resolve highlighting to the next end multi line delimiter
      if (isMultiLineComment())
        commentLinesAfter(content, endLine);
      else
        highlightLinesAfter(content, endLine);
    }

    private boolean commentLinesBefore(String content, int line)
    {
      int offset = rootElement.getElement(line).getStartOffset();
      // Start of comment not found, nothing to do
      int startDelimiter = lastIndexOf(content, getStartDelimiter(), offset - 2);
      if (startDelimiter < 0)
        return false;
      // Matching start/end of comment found, nothing to do
      int endDelimiter = indexOf(content, getEndDelimiter(), startDelimiter);
      if (endDelimiter < offset & endDelimiter != -1)
        return false;
      // End of comment not found, highlight the lines
      doc.setCharacterAttributes(startDelimiter, offset - startDelimiter + 1, comment, false);
      return true;
    }

    private void commentLinesAfter(String content, int line)
    {
      int offset = rootElement.getElement(line).getEndOffset();
      // End of comment not found, nothing to do
      int endDelimiter = indexOf(content, getEndDelimiter(), offset);
      if (endDelimiter < 0)
        return;
      // Matching start/end of comment found, comment the lines
      int startDelimiter = lastIndexOf(content, getStartDelimiter(), endDelimiter);
      if (startDelimiter < 0 || startDelimiter <= offset)
      {
        doc.setCharacterAttributes(offset, endDelimiter - offset + 1, comment, false);
      }
    }

    private void highlightLinesAfter(String content, int line) throws BadLocationException
    {
      int offset = rootElement.getElement(line).getEndOffset();
      // Start/End delimiter not found, nothing to do
      int startDelimiter = indexOf(content, getStartDelimiter(), offset);
      int endDelimiter = indexOf(content, getEndDelimiter(), offset);
      if (startDelimiter < 0)
        startDelimiter = content.length();
      if (endDelimiter < 0)
        endDelimiter = content.length();
      int delimiter = Math.min(startDelimiter, endDelimiter);
      if (delimiter < offset)
        return;
      // Start/End delimiter found, reapply highlighting
      int endLine = rootElement.getElementIndex(delimiter);
      for (int i = line + 1; i < endLine; i++)
      {
        Element branch = rootElement.getElement(i);
        Element leaf = doc.getCharacterElement(branch.getStartOffset());
        AttributeSet as = leaf.getAttributes();
        if (as.isEqual(comment))
          applyHighlighting(content, i);
      }
    }

    private void applyHighlighting(String content, int line) throws BadLocationException
    {
      int startOffset = rootElement.getElement(line).getStartOffset();
      int endOffset = rootElement.getElement(line).getEndOffset() - 1;
      int lineLength = endOffset - startOffset;
      int contentLength = content.length();
      if (endOffset >= contentLength)
        endOffset = contentLength - 1;
      // check for multi line comments
      // (always set the comment attribute for the entire line)
      if (endingMultiLineComment(content, startOffset, endOffset) || isMultiLineComment()
          || startingMultiLineComment(content, startOffset, endOffset))
      {
        doc.setCharacterAttributes(startOffset, endOffset - startOffset + 1, comment, false);
        return;
      }
      // set normal attributes for the line
      doc.setCharacterAttributes(startOffset, lineLength, normal, true);
      // check for single line comment
      int index = content.indexOf(getSingleLineDelimiter(), startOffset);
      if ((index > -1) && (index < endOffset))
      {
        doc.setCharacterAttributes(index, endOffset - index + 1, comment, false);
        endOffset = index - 1;
      }
      // check for tokens
      checkForTokens(content, startOffset, endOffset);
    }

    private boolean startingMultiLineComment(String content, int startOffset, int endOffset)
        throws BadLocationException
    {
      int index = indexOf(content, getStartDelimiter(), startOffset);
      if ((index < 0) || (index > endOffset))
        return false;
      else
      {
        setMultiLineComment(true);
        return true;
      }
    }

    private boolean endingMultiLineComment(String content, int startOffset, int endOffset) throws BadLocationException
    {
      int index = indexOf(content, getEndDelimiter(), startOffset);
      if ((index < 0) || (index > endOffset))
        return false;
      else
      {
        setMultiLineComment(false);
        return true;
      }
    }

    private boolean isMultiLineComment()
    {
      return multiLineComment;
    }

    private void setMultiLineComment(boolean value)
    {
      multiLineComment = value;
    }

    private void checkForTokens(String content, int startOffset, int endOffset)
    {
      while (startOffset <= endOffset)
      {
        // skip the delimiters to find the start of a new token
        while (isDelimiter(content.substring(startOffset, startOffset + 1)))
        {
          if (startOffset < endOffset)
            startOffset++;
          else
            return;
        }
        // Extract and process the entire token
        if (isQuoteDelimiter(content.substring(startOffset, startOffset + 1)))
          startOffset = getQuoteToken(content, startOffset, endOffset);
        else
          startOffset = getOtherToken(content, startOffset, endOffset);
      }
    }

    /*
     *
     */
    private int getQuoteToken(String content, int startOffset, int endOffset)
    {
      String quoteDelimiter = content.substring(startOffset, startOffset + 1);
      String escapeString = getEscapeString(quoteDelimiter);
      int index;
      int endOfQuote = startOffset;
      // skip over the escape quotes in this quote
      index = content.indexOf(escapeString, endOfQuote + 1);
      while ((index > -1) && (index < endOffset))
      {
        endOfQuote = index + 1;
        index = content.indexOf(escapeString, endOfQuote);
      }
      // now find the matching delimiter
      index = content.indexOf(quoteDelimiter, endOfQuote + 1);
      if ((index < 0) || (index > endOffset))
        endOfQuote = endOffset;
      else
        endOfQuote = index;
      doc.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, quote, false);
      return endOfQuote + 1;
    }

    private int getOtherToken(String content, int startOffset, int endOffset)
    {
      int endOfToken = startOffset + 1;
      while (endOfToken <= endOffset)
      {
        if (isDelimiter(content.substring(endOfToken, endOfToken + 1)))
          break;
        endOfToken++;
      }
      String token = content.substring(startOffset, endOfToken);
      if (isKeyword(token))
      {
        doc.setCharacterAttributes(startOffset, endOfToken - startOffset, keyword, false);
      }
      return endOfToken + 1;
    }

    private int indexOf(String content, String needle, int offset)
    {
      int index;
      while ((index = content.indexOf(needle, offset)) != -1)
      {
        String text = getLine(content, index).trim();
        if (text.startsWith(needle) || text.endsWith(needle))
          break;
        else
          offset = index + 1;
      }
      return index;
    }

    private int lastIndexOf(String content, String needle, int offset)
    {
      int index;
      while ((index = content.lastIndexOf(needle, offset)) != -1)
      {
        String text = getLine(content, index).trim();
        if (text.startsWith(needle) || text.endsWith(needle))
          break;
        else
          offset = index - 1;
      }
      return index;
    }

    private String getLine(String content, int offset)
    {
      int line = rootElement.getElementIndex(offset);
      Element lineElement = rootElement.getElement(line);
      int start = lineElement.getStartOffset();
      int end = lineElement.getEndOffset();
      return content.substring(start, end - 1);
    }

    protected boolean isDelimiter(String character)
    {
      String operands = ";:{}()[]+-/%<=>!&|^~*";
      if (Character.isWhitespace(character.charAt(0)) || operands.indexOf(character) != -1)
        return true;
      else
        return false;
    }

    protected boolean isQuoteDelimiter(String character)
    {
      String quoteDelimiters = "\"'";
      if (quoteDelimiters.indexOf(character) < 0)
        return false;
      else
        return true;
    }

    protected boolean isKeyword(String token)
    {
      return keywords.contains(token);
    }

    protected String getStartDelimiter()
    {
      return "/*";
    }

    protected String getEndDelimiter()
    {
      return "*/";
    }

    protected String getSingleLineDelimiter()
    {
      return "//";
    }

    protected String getEscapeString(String quoteDelimiter)
    {
      return "\\" + quoteDelimiter;
    }

    protected String addMatchingBrace(int offset) throws BadLocationException
    {
      StringBuffer whiteSpace = new StringBuffer();
      int line = rootElement.getElementIndex(offset);
      int i = rootElement.getElement(line).getStartOffset();
      while (true)
      {
        String temp = doc.getText(i, 1);
        if (temp.equals(" ") || temp.equals("\t"))
        {
          whiteSpace.append(temp);
          i++;
        }
        else
          break;
      }
      return "{\n" + whiteSpace.toString() + "  \n" + whiteSpace.toString() + "}";
    }
  }
}
