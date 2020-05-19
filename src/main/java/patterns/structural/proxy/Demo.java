package patterns.structural.proxy;

interface Drivable {
  void drive();
}

public class Demo {
  public static void main(String[] args) {}
}

class Car implements Drivable {
  protected final Driver driver;

  Car(Driver driver) {
    this.driver = driver;
  }

  @Override
  public void drive() {
    System.out.println(driver.getName() + " is driving this car...");
  }
}

class Driver {
  private final String name;
  private int age;

  Driver(String name) {
    this.name = name;
  }

  public Driver(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public int getAge() {
    return age;
  }

  public String getName() {
    return name;
  }
}

class CarProxy extends Car {

  CarProxy(Driver driver) {
    super(driver);
  }

  @Override
  public void drive() {
    if (driver.getAge() <= 16) {
      System.out.println(driver.getName() + " is too young to drive this car");
    } else {
      super.drive();
    }
  }
}

class BadObject {
  // you cannot proxy the access or changes to the field
  // or the call operator
  // since you can't override '=' operator
  private int property = 0;
}

class BetterObject {
  private final MyPropProxy<Integer> my_prop = new MyPropProxy<>(29);
}

class MyPropProxy<T> {
  private T prop;

  public MyPropProxy(T prop) {
    this.prop = prop;
  }

  public T getProp() {
    return prop;
  }

  public void setProp(T prop) {
    this.prop = prop;
  }
}
