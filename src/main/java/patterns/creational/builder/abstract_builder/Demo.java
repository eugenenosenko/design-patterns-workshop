package patterns.creational.builder.abstract_builder;

public class Demo {
  public static void main(String[] args) {
    ConcreteBuilder b = new ConcreteBuilder("first", "second");
    b.withName("name").withPosition("position").withConcretePosition("concrete-position");
  }
}

class Person {
  private final String mandatory;
  private final String mandatory2;
  private String positon;
  private String concretePositon;

  public Person(String mandatory, String mandatory2) {
    this.mandatory = mandatory;
    this.mandatory2 = mandatory2;
  }

  public Person(String mandatory, String mandatory2, String positon, String concretePositon) {
    this.mandatory = mandatory;
    this.mandatory2 = mandatory2;
    this.positon = positon;
    this.concretePositon = concretePositon;
  }
}

abstract class AbstractBuilder<T extends AbstractBuilder<T>> {
  protected final String mandatoryField;
  protected final String mandatoryField2;

  protected String position;
  protected Person person;
  private String name;

  public AbstractBuilder(String mandatoryField, String mandatoryField2) {
    this.mandatoryField = mandatoryField;
    this.mandatoryField2 = mandatoryField2;
  }

  protected abstract T self();

  public T withName(String name) {
    this.name = name;
    return self();
  }

  public T withPosition(String position) {
    this.position = position;
    return self();
  }

  public Person build() {
    return new Person(mandatoryField, mandatoryField2, position, name);
  }
}

class ConcreteBuilder extends AbstractBuilder<ConcreteBuilder> {

  public ConcreteBuilder(String mandatoryField, String mandatoryField2) {
    super(mandatoryField, mandatoryField2);
  }

  public ConcreteBuilder withConcretePosition(String position) {
    System.out.println(position);
    return this;
  }

  @Override
  protected ConcreteBuilder self() {
    return this;
  }
}
