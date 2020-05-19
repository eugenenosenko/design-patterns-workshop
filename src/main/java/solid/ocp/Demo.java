package solid.ocp;

import java.util.List;
import java.util.stream.Stream;

/**
 * OCP stands for open-closed-principle that means that objects should be open for extension but
 * closed for modification
 */
enum Color {
  RED,
  GREEN,
  BLUE;
}

enum Size {
  SMALL,
  MEDIUM,
  LARGE,
  HUGE;
}

/** Specification Patter */
interface Specification<T> {
  boolean isSatisfied(T item);
}

interface Filter<T> {
  Stream<T> filter(List<T> items, Specification<T> specification);
}

// Product DTO
class Product {
  public String name;
  public Color color;
  public Size size;

  public Product(String name, Color color, Size size) {
    this.name = name;
    this.color = color;
    this.size = size;
  }

  @Override
  public String toString() {
    return "Product{" + "name='" + name + '\'' + ", color=" + color + ", size=" + size + '}';
  }
}

// Say you need to filter the products
class ProductFilter {
  // Initial filter
  public Stream<Product> filterByColor(List<Product> products, Color color) {
    return products.stream().filter(p -> p.color == color);
  }

  // Second filter
  public Stream<Product> filterBySize(List<Product> products, Size size) {
    return products.stream().filter(p -> p.size == size);
  }

  // Third filter. Add another one, i.e. by price, by manufacturer etc etc.
  // Soon enough the number of parameters will grow to 6-7
  public Stream<Product> filterBySizeAndColor(List<Product> products, Color color, Size size) {
    return products.stream().filter(p -> p.size == size && p.color == color);
  }
}

public class Demo {
  public static void main(String[] args) {
    Product apple = new Product("Apple", Color.GREEN, Size.SMALL);
    Product tree = new Product("Tree", Color.GREEN, Size.LARGE);
    Product house = new Product("House", Color.BLUE, Size.HUGE);

    List<Product> products = List.of(apple, tree, house);
    ProductFilter pf = new ProductFilter();

    // BAD
    System.out.println("(OLD) Green products: ");
    pf.filterByColor(products, Color.GREEN)
        .forEach(p -> System.out.println(" - " + p.name + " is green"));

    // GOOD
    BetterFilter bf = new BetterFilter();
    System.out.println("(NEW) Green products: ");
    bf.filter(products, new ColorSpecification(Color.GREEN))
        .forEach(product -> System.out.println(" - " + product.name + " is green"));
    System.out.println("(NEW) Small products: ");
    bf.filter(products, new SizeSpecification(Size.SMALL))
        .forEach(p -> System.out.println(" - " + p.name + " is small"));

    System.out.println("(NEW) Small AND Green products: ");
    bf.filter(
            products,
            new AndSpecification<Product>(
                new ColorSpecification(Color.GREEN), new SizeSpecification(Size.SMALL)))
        .forEach(product -> System.out.println(" - " + product.name + " is green and small"));

    System.out.println("(NEW) Small OR Green products: ");
    bf.filter(
            products,
            new OrSpecification<>(
                new ColorSpecification(Color.GREEN), new SizeSpecification(Size.SMALL)))
        .forEach(product -> System.out.println(" - " + product.name + " is green and small"));
  }
}

class ColorSpecification implements Specification<Product> {
  private Color color;

  public ColorSpecification(Color color) {
    this.color = color;
  }

  @Override
  public boolean isSatisfied(Product item) {
    return item.color == color;
  }
}

class SizeSpecification implements Specification<Product> {
  private Size size;

  public SizeSpecification(Size size) {
    this.size = size;
  }

  @Override
  public boolean isSatisfied(Product item) {
    return item.size == size;
  }
}

class AndSpecification<T> implements Specification<T> {
  private Specification<T> first;
  private Specification<T> second;

  public AndSpecification(Specification<T> first, Specification<T> second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public boolean isSatisfied(T item) {
    return first.isSatisfied(item) && second.isSatisfied(item);
  }
}

class OrSpecification<T> implements Specification<T> {
  private Specification<T> first;
  private Specification<T> second;

  public OrSpecification(Specification<T> first, Specification<T> second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public boolean isSatisfied(T item) {
    return first.isSatisfied(item) || second.isSatisfied(item);
  }
}

/** You no longer need to jump into BF and try to modify it, since it's open for extension */
class BetterFilter implements Filter<Product> {

  @Override
  public Stream<Product> filter(List<Product> items, Specification<Product> specification) {
    return items.stream().filter(specification::isSatisfied);
  }
}
