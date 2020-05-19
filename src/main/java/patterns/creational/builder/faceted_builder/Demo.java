package patterns.creational.builder.faceted_builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Demo {
  public static void main(String[] args) {
    PersonBuilder builder = new PersonBuilder();
    Person p =
        builder
            .works()
            .at("Company")
            .asA("Technicial")
            .earning(10000)
            .lives()
            .at("Str. Some street")
            .withPostcode("22421A")
            .build();

    System.out.println(p);
  }
}

class Person {
  public String streetAddress, postCode, city;
  public String companyName, position;
  public int income;

  @Override
  public String toString() {
    return "Person{"
        + "streetAddress='"
        + streetAddress
        + '\''
        + ", postCode='"
        + postCode
        + '\''
        + ", city='"
        + city
        + '\''
        + ", comanyName='"
        + companyName
        + '\''
        + ", position='"
        + position
        + '\''
        + ", income="
        + income
        + '}';
  }
}

//  Builder FACADE
class PersonBuilder {
  protected Person p = new Person();

  public PersonAddressBuilder lives() {
    return new PersonAddressBuilder(p);
  }

  public PersonJobBuilder works() {
    return new PersonJobBuilder(p);
  }

  public Person build() {
    return p;
  }
}

class PersonJobBuilder extends PersonBuilder {

  public PersonJobBuilder(Person person) {
    this.p = person;
  }

  public PersonJobBuilder earning(int annualIncome) {
    p.income = annualIncome;
    return this;
  }

  public PersonJobBuilder asA(String position) {
    p.position = position;
    return this;
  }

  public PersonJobBuilder at(String companyName) {
    p.companyName = companyName;
    return this;
  }
}

class PersonAddressBuilder extends PersonBuilder {

  public PersonAddressBuilder(Person person) {
    this.p = person;
  }

  public PersonAddressBuilder at(String streetAddress) {
    p.streetAddress = streetAddress;
    return this;
  }

  public PersonAddressBuilder withPostcode(String postcode) {
    p.postCode = postcode;
    return this;
  }

  public PersonAddressBuilder in(String city) {
    p.city = city;
    return this;
  }
}

class CodeBuilder {
  private final String qualifier = "public";
  private String className;
  private Map<String, String> fields = new HashMap();

  public CodeBuilder(String className) {
    this.className = className;
  }

  public CodeBuilder addField(String name, String type) {
    fields.put(name, type);
    return this;
  }

  public String buildFields() {
    StringBuilder sb = new StringBuilder();

    for (Entry<String, String> e : fields.entrySet()) {
      sb.append(String.format("%s %s %s;\n", qualifier, e.getValue(), e.getKey()));
    }

    return sb.toString();
  }

  @Override
  public String toString() {
    return String.format("%s class %s\n{\n%s}", qualifier, className, buildFields());
  }
}
