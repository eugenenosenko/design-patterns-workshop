package patterns.behavioral.cor;

import java.util.function.Supplier;

public class Demo {
  public static void main(String[] args) throws Throwable {
    Creature goblin = new Creature("goblin", 100, 2);
    System.out.println(goblin);

    CreatureModifier root = new CreatureModifier(goblin);
    System.out.println("Lets double strength of the creature");
    StrengthModifier strengthModifier = new StrengthModifier(goblin, 2);
    root.addModifier(strengthModifier);

    System.out.println("Lets double health of the creature");
    HealthModifier healthModifier = new HealthModifier(goblin, 2);
    root.addModifier(healthModifier);

    root.handle();
    System.out.println(goblin);
  }
}

class CreatureModifier {
  protected final Creature c;
  private CreatureModifier next;

  CreatureModifier(Supplier<? extends Creature> creatureFactory) {
    this.c = creatureFactory.get();
  }

  CreatureModifier(Creature c) {
    this.c = c;
  }

  public void addModifier(CreatureModifier modifier) {
    if (next != null) {
      next.addModifier(modifier);
    } else {
      next = modifier;
    }
  }

  void handle() {
    if (next != null) next.handle();
  }
}

class NoBonusesModifier extends CreatureModifier {

  NoBonusesModifier(Creature c) {
    super(c);
  }

  @Override
  void handle() {
    // nothing
  }
}

class HealthModifier extends CreatureModifier {
  private int healthModifier;

  public HealthModifier(Creature c, int healthModifier) {
    super(c);
    this.healthModifier = healthModifier;
  }

  HealthModifier(Supplier<? extends Creature> creatureFactory) {
    super(creatureFactory);
  }

  @Override
  void handle() {
    c.setHealth(c.getHealth() * healthModifier);
    super.handle();
  }
}

class StrengthModifier extends CreatureModifier {
  private int modifier;

  public StrengthModifier(Creature c, int modifier) {
    super(c);
    this.modifier = modifier;
  }

  private void boostStrength() {
    c.setStrength(c.getStrength() * modifier);
  }

  @Override
  void handle() {
    System.out.println("Boosting strength");
    boostStrength();
    super.handle();
  }
}

class Creature {
  private final String name;
  private int strength = 0;
  private int health = 100;

  Creature(String name, int strength, int health) {
    this.name = name;
    this.strength = strength;
    this.health = health;
  }

  @Override
  public String toString() {
    return "Creature{"
        + "name='"
        + name
        + '\''
        + ", strength="
        + strength
        + ", health="
        + health
        + '}';
  }

  public String getName() {
    return name;
  }

  public int getStrength() {
    return strength;
  }

  public void setStrength(int strength) {
    this.strength = strength;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }
}
