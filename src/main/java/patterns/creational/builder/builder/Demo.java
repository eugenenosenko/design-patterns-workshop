package patterns.creational.builder.builder;

import java.util.ArrayList;
import java.util.Collections;

public class Demo {
  public static void main(String[] args) {
    // string builder
    StringBuilder sb = new StringBuilder();
    String[] words = new String[] {"hello", "there", "Eugene", "Nosenko"};

    for (String w : words) {
      sb.append(String.format("%s\n", w));
    }

    System.out.println(sb.toString());

    HtmlBuilder builder = new HtmlBuilder("ul");
    builder.addChild("li", "hello").addChild("li", "world");

    System.out.println(builder.toString());
  }
}

class HtmlElement {
  private final String newLine = System.lineSeparator();
  private final int indentSize = 2;
  public ArrayList<HtmlElement> elements = new ArrayList<>();
  public String name, text;

  public HtmlElement() {}

  public HtmlElement(String name, String text) {
    this.name = name;
    this.text = text;
  }

  private String toStringImplementation(int indent) {
    StringBuilder builder = new StringBuilder();
    String i = String.join("", Collections.nCopies(indent * indentSize, ""));
    builder.append(String.format("%s<%s>%s", i, name, newLine));
    if (text != null && !text.isEmpty()) {
      builder
          .append(String.join("", Collections.nCopies(indentSize * (indent + 1), "")))
          .append(text)
          .append(newLine);

      for (HtmlElement e : elements) {
        builder.append(e.toStringImplementation(indent + 1));
      }
      builder.append(String.format("%s</%s>%s", i, name, newLine));
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    return toStringImplementation(0);
  }
}

class HtmlBuilder {
  private HtmlElement root = new HtmlElement();
  private String rootName;

  public HtmlBuilder(String rootName) {
    this.rootName = rootName;
    this.root.name = rootName;
  }

  public HtmlBuilder addChild(String childName, String childText) {
    HtmlElement e = new HtmlElement(childName, childText);
    root.elements.add(e);
    return this;
  }

  public void clear() {
    root = new HtmlElement();
    root.name = rootName;
  }

  @Override
  public String toString() {
    return root.toString();
  }
}
