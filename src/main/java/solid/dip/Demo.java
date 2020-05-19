package solid.dip;

import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// DEPENDENCY INVERSION is not DEPENDENCY INJECTIONS
// those two terms have few things in common but are not the same

// A. High-level modules should not depend on low-level modules.
// Both should depend on abstractions.

// B. Abstractions should not depend on details.
// Details should depend on abstractions.

enum Relationship {
  PARENT,
  CHILD,
  SIBLING
}

// low-level module would implement it
// data management layer
interface RelationshipBrowser {
  List<Person> findAllChildrenOf(String name);
}

class Person {
  public String name;

  public Person(String name) {
    this.name = name;
  }
}

// low-level module. Related to data storage, list of relationships.
// does not contain any business logic
// its SRP is to keep the relationships and allow it's manipulation
class Relationships {
  private List<Triplet<Person, Relationship, Person>> relationships = new ArrayList<>();

  public void addParentAndChild(Person parent, Person child) {
    relationships.add(new Triplet<>(parent, Relationship.PARENT, child));
    relationships.add(new Triplet<>(child, Relationship.CHILD, parent));
  }

  public List<Triplet<Person, Relationship, Person>> getRelationships() {
    return relationships;
  }
}

class BetterRelationships implements RelationshipBrowser {
  private List<Triplet<Person, Relationship, Person>> relationships = new ArrayList<>();

  public void addParentAndChild(Person parent, Person child) {
    relationships.add(new Triplet<>(parent, Relationship.PARENT, child));
    relationships.add(new Triplet<>(child, Relationship.CHILD, parent));
  }

  public List<Triplet<Person, Relationship, Person>> getRelationships() {
    return relationships;
  }

  @Override
  public List<Person> findAllChildrenOf(String name) {
    return relationships.stream()
        .filter(
            x -> Objects.equals(x.getValue0().name, name) && x.getValue1() == Relationship.PARENT)
        .map(Triplet::getValue2)
        .collect(Collectors.toList());
  }
}

// high-level module. It uses a low-level module = Relationships();
// it's closer to the user and contain business logic
class Research {
  public Research(Relationships r) {
    List<Triplet<Person, Relationship, Person>> relations = r.getRelationships();
    relations.stream()
        .filter(x -> x.getValue0().name.equals("John") && x.getValue1() == Relationship.PARENT)
        .forEach(ch -> System.out.println("John has a child called " + ch.getValue2().name));
  }

  public Research(RelationshipBrowser browser) {
    List<Person> children = browser.findAllChildrenOf("John");
    for (Person p : children) {
      System.out.println("John has a child called " + p.name);
    }
  }
}

public class Demo {
  public static void main(String[] args) {
    Person parent = new Person("John");
    Person child1 = new Person("Chris");
    Person child2 = new Person("Matt");

    Relationships r = new Relationships();
    r.addParentAndChild(parent, child1);
    r.addParentAndChild(parent, child2);

    new Research(r);
  }
}
