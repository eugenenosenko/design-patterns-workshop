package patterns.creational.prototype;

import java.util.Arrays;

public class Demo {
  public static void main(String[] args) throws CloneNotSupportedException {
    Address a = new Address("Street", new int[] {11, 11});
    Person p = new Person("Eugene", 22, a);

    // copies reference only
    Person p1 = p;
    p1.address.zipCode = new int[] {2342};
    p1.name = "Jane";

    System.out.println(
        p); // Person{address=Address{street='Street', zipCode=[2342]}, name='Jane', age=22}
    System.out.println(
        p1); // Person{address=Address{street='Street', zipCode=[2342]}, name='Jane', age=22}

    CloneableAddress cA = new CloneableAddress("Street", new int[] {11, 11});
    CloneablePerson cP = new CloneablePerson("Eugene", 22, cA);

    CloneablePerson cP1 = cP.clone();
    cP1.address.zipCode = new int[] {2342};
    cP1.name = "Jane";

    System.out.println(cP);
    System.out.println(cP1);
  }
}

class Person {
  public Address address;
  public String name;
  public int age;

  public Person(String name, int age, Address address) {
    this.name = name;
    this.age = age;
    this.address = address;
  }

  @Override
  public String toString() {
    return "Person{" + "address=" + address + ", name='" + name + '\'' + ", age=" + age + '}';
  }
}

class ConstructorCloneablePerson extends Person {
  public String name;
  public int age;
  public Address address;

  public ConstructorCloneablePerson(String name, int age, Address address) {
    super(name, age, address);
    this.name = name;
    this.age = age;
    this.address = address;
  }

  public ConstructorCloneablePerson(ConstructorCloneablePerson other) {
    super(other.name, other.age, other.address);
    this.name = other.name;
    this.age = other.age;
    this.address = other.address;
  }
}

class CloneablePerson extends Person implements Cloneable {
  public String name;
  public int age;
  public CloneableAddress address;

  public CloneablePerson(String name, int age, CloneableAddress address) {
    super(name, age, address);
    this.name = name;
    this.age = age;
    this.address = address;
  }

  @Override
  public CloneablePerson clone() throws CloneNotSupportedException {
    return new CloneablePerson(name, age, address.clone());
  }
}

class CloneableAddress extends Address implements Cloneable {
  public String street;
  public int[] zipCode;

  public CloneableAddress(String street, int[] zipCode) {
    super(street, zipCode);
    this.street = street;
    this.zipCode = zipCode;
  }

  @Override
  protected CloneableAddress clone() throws CloneNotSupportedException {
    return new CloneableAddress(street, zipCode);
  }
}

class Address {
  public String street = "";
  public int[] zipCode = {0};

  public Address(String street, int[] zipCode) {
    this.street = street;
    this.zipCode = zipCode;
  }

  @Override
  public String toString() {
    return "Address{" + "street='" + street + '\'' + ", zipCode=" + Arrays.toString(zipCode) + '}';
  }
}
