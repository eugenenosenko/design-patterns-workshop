package patterns.structural.flyweight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Demo {

  public static void main(String[] args) {

    // 5 bytes are wasted if UTF-8 string
    BadUser john_wayne = new BadUser("John Wayne");
    BadUser jane_wayne = new BadUser("Jane Wayne");

    // only one one string John Wayne is created
    User u1 = new User("John Wayne");
    User u2 = new User("John Wayne");
    User u3 = new User("John Wayne");
    User u4 = new User("John Wayne");

    // example two
    BadFormattedText badFormattedText = new BadFormattedText("This is hello there");
    badFormattedText.capitalize(0, 10);
    System.out.println(badFormattedText.toString());

    FormattedText ft = new FormattedText("Make America Great Again");
    ft.getRange(13, 18).capitalize = true;
    System.out.println(ft);
  }
}

class FormattedText {
  private List<TextRange> formatting = new ArrayList<>();
  private String plainText;

  public FormattedText(String plainText) {
    this.plainText = plainText;
  }

  public TextRange getRange(int start, int end) {
    TextRange range = new TextRange(start, end);
    formatting.add(range);
    return range;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < plainText.length(); ++i) {
      char c = plainText.charAt(i);
      for (TextRange range : formatting) {
        if (range.covers(i) && range.capitalize) {
          c = Character.toUpperCase(c);
        }

        sb.append(c);
        String intern = String.valueOf(c).intern();
      }
    }
    return sb.toString();
  }

  public class TextRange {
    public boolean capitalize, bold, italic;
    int start, end;

    public TextRange(int start, int end) {
      this.start = start;
      this.end = end;
    }

    public boolean covers(int position) {
      return position >= start && position <= end;
    }
  }
}

class BadFormattedText {
  private String plainText;
  private boolean[] capitalize;

  public BadFormattedText(String plainText) {
    this.plainText = plainText;
    this.capitalize = new boolean[plainText.length()];
  }

  public void capitalize(int start, int end) {
    for (int i = start; i <= end; i++) {
      capitalize[i] = true;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < plainText.length(); ++i) {
      char c = plainText.charAt(i);
      sb.append(capitalize[i] ? Character.toUpperCase(c) : c);
    }
    return sb.toString();
  }
}

class User {
  private static List<String> strings = new ArrayList<>();
  private int[] names;

  User(String fullName) {
    names = assignIndices(fullName);
  }

  private static Function<String, Integer> getOrAdd() {
    return (String s) -> {
      int i = strings.indexOf(s);
      if (i != -1) return i;
      else {
        strings.add(s);
        return strings.size() - 1;
      }
    };
  }

  private int[] assignIndices(String fullName) {
    return Arrays.stream(fullName.split(" ")).mapToInt(s -> getOrAdd().apply(s)).toArray();
  }

  public String getFullName() {
    return strings.get(names[0]) + " " + strings.get(names[1]);
  }
}

class BadUser {
  private String fullName;

  BadUser(String fullName) {
    this.fullName = fullName;
  }
}
