package patterns.behavioral.brokercor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// CQS
class Event<Args> {
  private Map<Integer, Consumer<Args>> handlers = new HashMap<>();
  private int index;

  public int subscribe(Consumer<Args> handler) {
    int subscriptionIndex = index;
    handlers.put(index++, handler);
    return subscriptionIndex;
  }

  public void unsubscribe(int key) {
    handlers.remove(key);
  }

  public void fire(Args args) {
    for (Consumer<Args> handler : handlers.values()) {
      handler.accept(args);
    }
  }
}

class Query {
  public String creatureName;
  public Argument argument;
  public int result;

  public Query(String creatureName, Argument argument, int result) {
    this.creatureName = creatureName;
    this.argument = argument;
    this.result = result;
  }

  enum Argument {
    ATTACK,
    DEFENCE
  }
}

class Game {
  public Event<Query> query = new Event<>();
}

class Creature {
  public String name;
  public int baseAttack, baseDefence;
  private Game game;

  public Creature(Game game, String name, int baseAttack, int baseDefence) {
    this.game = game;
    this.name = name;
    this.baseAttack = baseAttack;
    this.baseDefence = baseDefence;
  }

  @Override
  public String toString() {
    return "Creature{"
        + "name='"
        + name
        + '\''
        + ", baseAttack="
        + getAttack()
        + ", baseDefence="
        + getDefence()
        + '}';
  }

  int getAttack() {
    Query query = new Query(name, Query.Argument.ATTACK, baseAttack);
    game.query.fire(query);
    return query.result;
  }

  int getDefence() {
    Query query = new Query(name, Query.Argument.DEFENCE, baseAttack);
    game.query.fire(query);
    return query.result;
  }
}

abstract class CreatureModifier {
  protected Game game;
  protected Creature creature;

  public CreatureModifier(Game game, Creature creature) {
    this.game = game;
    this.creature = creature;
  }

  abstract Consumer<Query> modify();
}

class DoubleDefenceModifier extends CreatureModifier implements AutoCloseable {
  private final int token;

  public DoubleDefenceModifier(Game game, Creature creature) {
    super(game, creature);
    token = game.query.subscribe(modify());
  }

  @Override
  Consumer<Query> modify() {
    return (Query q) -> {
      if (q.creatureName.equals(creature.name) && q.argument == Query.Argument.DEFENCE) {
        q.result *= 2;
      }
    };
  }

  @Override
  public void close() {
    game.query.unsubscribe(token);
  }
}

class DoubleAttackModifier extends CreatureModifier implements AutoCloseable {
  private final int token;

  public DoubleAttackModifier(Game game, Creature creature) {
    super(game, creature);
    token = game.query.subscribe(modify());
  }

  @Override
  Consumer<Query> modify() {
    return (Query q) -> {
      if (q.creatureName.equals(creature.name) && q.argument == Query.Argument.ATTACK) {
        q.result *= 2;
      }
    };
  }

  @Override
  public void close() {
    game.query.unsubscribe(token);
  }
}

public class Demo {
  public static void main(String[] args) {
    Game g = new Game();
    Creature goblin = new Creature(g, "goblin", 2, 2);
    System.out.println("Initial goblin " + goblin);

    DoubleDefenceModifier doubleDefenceModifier = new DoubleDefenceModifier(g, goblin);
    DoubleAttackModifier doubleAttackModifier = new DoubleAttackModifier(g, goblin);

    System.out.println("After attack modifier " + goblin);
    System.out.println("After defence modifier " + goblin);

    doubleAttackModifier.close();
    doubleDefenceModifier.close();
    System.out.println("After closing modification" + goblin);
  }
}
