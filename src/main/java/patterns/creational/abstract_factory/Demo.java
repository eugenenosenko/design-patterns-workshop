package patterns.creational.abstract_factory;

import javafx.util.Pair;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

interface HotDrink {
  void consume();
}

interface AbstractFactory {
  HotDrink prepare(int amount);
}

public class Demo {
  public static void main(String[] args) throws Exception {
    TeaFactory tf = new TeaFactory();
    HotDrink prepare = tf.prepare(3);

    CoffeeFactory cf = new CoffeeFactory();
    HotDrink prepare1 = cf.prepare(55);

    System.out.println("Starting hotdrinks machine");
    HotDrinkMachine m = new HotDrinkMachine();
    m.initHotDrinkFactories();

    m.makeHotDrink();
  }
}

class Tea implements HotDrink {
  @Override
  public void consume() {
    System.out.println("Delicious tea");
  }
}

class Coffee implements HotDrink {
  @Override
  public void consume() {
    System.out.println("Delicious coffee");
  }
}

class TeaFactory implements AbstractFactory {
  @Override
  public HotDrink prepare(int amount) {
    System.out.println("Preparing tea");
    return new Tea();
  }
}

class CoffeeFactory implements AbstractFactory {
  @Override
  public HotDrink prepare(int amount) {
    System.out.println("Preparing coffee");
    return new Coffee();
  }
}

class HotDrinkMachine {
  private List<Pair<String, AbstractFactory>> list = new ArrayList<>();

  public void initHotDrinkFactories() throws Exception {
    Set<Class<? extends AbstractFactory>> types =
        new Reflections("").getSubTypesOf(AbstractFactory.class);

    for (Class<? extends AbstractFactory> c : types) {
      list.add(
          new Pair<>(
              c.getSimpleName().replace("Factory", ""), c.getDeclaredConstructor().newInstance()));
    }
  }

  public HotDrink makeHotDrink() throws Exception {
    System.out.println("Available drinks: ");
    System.out.println("0: Tea");
    System.out.println("1: Coffee");
    for (Pair<String, AbstractFactory> p : list) {
      System.out.println(" - " + p.getKey());
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      String s;
      int i, amount;

      System.out.print("> ");
      if (((s = reader.readLine()) != null) && (i = Integer.parseInt(s)) >= 0 && i < list.size()) {
        System.out.println("Specifying amount");
        if (s != null && (amount = Integer.parseInt(s)) > 0) {
          return list.get(i).getValue().prepare(amount);
        }
      }
      System.out.println("Input was incorrect, try again");
    }
  }
}
