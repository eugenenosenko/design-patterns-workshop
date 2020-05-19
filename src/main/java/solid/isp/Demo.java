package solid.isp;

interface Machine {
  void print(Document d);

  void scan(Document d);

  void fax(Document d);
}

// ISP implementation
interface Printer {
  void print(Document document);
}

interface Scanner {
  void scan(Document document);
}

interface Fax {
  void fax(Document document);
}

class Document {}

class NewAmazingPrinter implements Machine {

  @Override
  public void print(Document d) {
    // print
  }

  @Override
  public void scan(Document d) {
    // scan
  }

  @Override
  public void fax(Document d) {
    // fax
  }
}

// Violation of ISP
class OldFashionedPrinter implements Machine {

  @Override
  public void print(Document d) {
    // print
  }

  @Override
  public void scan(Document d) {
    throw new RuntimeException("This printer doesn't support scanning");
  }

  @Override
  public void fax(Document d) {
    throw new RuntimeException("This printer doesn't support faxing");
  }
}

// YAGNI = You ain't going to need it
// This implementation doesn't need any other functionality
class JustAPrinter implements Printer {

  @Override
  public void print(Document document) {
    // print
  }
}

class PhotoCopier implements Printer, Scanner {

  @Override
  public void print(Document document) {
    // print
  }

  @Override
  public void scan(Document document) {
    // scan
  }
}

public class Demo {
  public static void main(String[] args) {
    // This will work
    NewAmazingPrinter amazingPrinter = new NewAmazingPrinter();
    amazingPrinter.print(new Document());

    // This will break. ISP violation
    OldFashionedPrinter oldFashionedPrinter = new OldFashionedPrinter();
    try {
      oldFashionedPrinter.scan(new Document()); // RuntimeException
    } catch (Exception e) {
      System.out.println(e);
    }

    // This will work. ISP adhered to
    PhotoCopier photoCopier = new PhotoCopier();
    photoCopier.scan(new Document());
  }
}
