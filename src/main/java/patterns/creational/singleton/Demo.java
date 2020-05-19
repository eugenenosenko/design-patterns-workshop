package patterns.creational.singleton;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

enum Sybsystem {
  PRIMARY,
  AUXILARY,
  FALLBACK
}

enum EnumSingleton {
  INSTANCE;
}

public class Demo {

  // Regular singleton is not strictly enforced and single-instance can
  // be worked-around using : reflection and serialisation
  public static void main(String[] args) throws Exception {
    // reflection
    Constructor<BasicSingleton> declaredConstructor = BasicSingleton.class.getDeclaredConstructor();
    declaredConstructor.setAccessible(true);

    BasicSingleton bs = declaredConstructor.newInstance();
    bs.setValue(4444);
    System.out.println(bs);

    // serialization
    BasicSingleton b = BasicSingleton.getInstance();
    b.setValue(555);

    saveToFile(b, "file.bin");

    b.setValue(222);
    BasicSingleton b1 = readFromFile("file.bin");

    System.out.println(b); // instance one
    System.out.println(b1); // instance two

    ThreadSafeSingleton instance = ThreadSafeSingleton.getInstance();
  }

  static BasicSingleton readFromFile(String filename) throws Exception {
    try (FileInputStream out = new FileInputStream(filename)) {
      ObjectInputStream in = new ObjectInputStream(out);
      return (BasicSingleton) in.readObject();
    }
  }

  static void saveToFile(BasicSingleton singleton, String filename) throws Exception {
    try (FileOutputStream out = new FileOutputStream(filename)) {
      ObjectOutputStream o = new ObjectOutputStream(out);
      o.writeObject(singleton);
    }
  }
}

class BasicSingleton implements Serializable {
  private static BasicSingleton instance = new BasicSingleton();
  private int value;

  private BasicSingleton() {}

  public static BasicSingleton getInstance() {
    return instance;
  }

  @Override
  public String toString() {
    return "BasicSingleton{" + "value=" + value + '}';
  }

  public void setValue(int i) {
    this.value = i;
  }
}

class MultiThreadedSingleton {
  private static MultiThreadedSingleton singleton;

  public static MultiThreadedSingleton getInstance() {
    // double check lock
    if (singleton == null) {
      synchronized (MultiThreadedSingleton.class) {
        if (singleton == null) {
          singleton = new MultiThreadedSingleton();
        }
      }
    }

    return singleton;
  }
}

class ThreadSafeSingleton {

  ThreadSafeSingleton(String s) {
    System.out.println(s);
  }

  public static ThreadSafeSingleton getInstance() {
    return Singleton.INSTANCE;
  }

  private static class Singleton {
    private static final ThreadSafeSingleton INSTANCE = new ThreadSafeSingleton("Hello");
  }
}

class LazyBasicSingleton {
  private static LazyBasicSingleton instance;

  private LazyBasicSingleton() {}

  public static LazyBasicSingleton getInstance() {
    if (instance == null) {
      instance = new LazyBasicSingleton();
      return instance;
    }
    return instance;
  }
}

class AnitReflectiveSingleton {
  private static AnitReflectiveSingleton instance;

  private AnitReflectiveSingleton() {
    throw new RuntimeException();
  }

  public static AnitReflectiveSingleton getInstance() {
    return instance;
  }
}

class Multiton {
  private static final ConcurrentHashMap<Sybsystem, Multiton> instances =
      new ConcurrentHashMap<>(Sybsystem.values().length);

  public static Multiton getInstance(Sybsystem ss) {
    if (!instances.containsKey(ss)) {
      synchronized (Multiton.class) {
        Multiton i = new Multiton();
        instances.put(ss, i);
      }
    }
    return instances.get(ss);
  }
}
