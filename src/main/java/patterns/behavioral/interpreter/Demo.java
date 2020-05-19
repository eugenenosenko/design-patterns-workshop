package patterns.behavioral.interpreter;

//

// Interpreter is composed of two parts
// Lexing - destructuring text data into lexical tokens
// Parsing - interpreting sequence of lexical tokens

// Lexing basically means divide raw input into repeatable objects
// i.e. -> "(1+2)-2" ->
// List<Token>.listOf(Token.LEFT_BRACKET, NumericToken.of("1"), Token.PLUS, NumericToken.of("2"),
// Token.RIGHT_BRACKET)

// Parsing means take those tokens and transform them into meaningful constructs i.e.
// SubtractionExpression[AdditionExpression[Int(1), Int(2)], Int(2)]

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface Element {
  int eval();
}

class Int implements Element {
  private int value;

  public Int(int value) {
    this.value = value;
  }

  @Override
  public int eval() {
    return value;
  }
}

class BinaryOperation implements Element {
  public Element left, right;
  public Type type;

  @Override
  public int eval() {
    switch (type) {
      case ADDITION:
        return left.eval() + right.eval();
      case SUBTRACTION:
        return left.eval() - right.eval();
      default:
        return 0;
    }
  }

  public enum Type {
    ADDITION,
    SUBTRACTION
  }
}

public class Demo {

  public static void main(String[] args) {
    String input = "(13+4)-(12+1)";
    List<Token> tokens = lex(input);
    System.out.println(tokens.stream().map(Token::toString).collect(Collectors.joining("\t")));

    Element parsed = parse(tokens);
    System.out.println(input + " = " + parsed.eval());
  }

  static Element parse(List<Token> tokens) {
    BinaryOperation result = new BinaryOperation();
    boolean haveLeftHandSide = false;

    for (int i = 0; i < tokens.size(); ++i) {
      Token token = tokens.get(i);
      switch (token.type) {
        case INTEGER:
          Int integer = new Int(Integer.parseInt(token.string));
          if (!haveLeftHandSide) {
            result.left = integer;
            haveLeftHandSide = true;
          } else {
            result.right = integer;
          }
          break;
        case PLUS:
          result.type = BinaryOperation.Type.ADDITION;
          break;
        case MINUS:
          result.type = BinaryOperation.Type.SUBTRACTION;
          break;
        case LPAREN:
          int j = 0; // location of rparen
          for (; j < tokens.size(); ++j) if (tokens.get(j).type == Token.Type.RPAREN) break;

          List<Token> subexpression =
              tokens.stream().skip(i + 1).limit(j - i - 1).collect(Collectors.toList());
          Element element = parse(subexpression);
          if (!haveLeftHandSide) {
            result.left = element;
            haveLeftHandSide = true;
          } else result.right = element;
          i = j;
          break;
      }
      break;
    }
    return result;
  }

  // lexing
  static List<Token> lex(String input) {
    ArrayList<Token> result = new ArrayList<>();
    for (int i = 0; i < input.length(); ++i) {
      switch (input.charAt(i)) {
        case '+':
          result.add(new Token(Token.Type.PLUS, "+"));
          break;
        case '-':
          result.add(new Token(Token.Type.MINUS, "-"));
          break;
        case '(':
          result.add(new Token(Token.Type.LPAREN, "("));
          break;
        case ')':
          result.add(new Token(Token.Type.RPAREN, ")"));
          break;
        default:
          StringBuilder builder = new StringBuilder("" + input.charAt(i));
          for (int j = i + 1; j < input.length(); ++j) {
            if (Character.isDigit(j)) {
              builder.append(j);
              ++i;
            } else {
              result.add(new Token(Token.Type.INTEGER, builder.toString()));
              break;
            }
          }
          break;
      }
    }
    return result;
  }
}

class Token {
  public Type type;
  public String string;

  public Token(Type type, String string) {
    this.type = type;
    this.string = string;
  }

  @Override
  public String toString() {
    return "`" + string + "`";
  }

  enum Type {
    INTEGER,
    PLUS,
    MINUS,
    LPAREN,
    RPAREN
  }
}
