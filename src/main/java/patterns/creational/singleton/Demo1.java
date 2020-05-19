package patterns.creational.singleton;

interface Connection<T> {
  T getConnection();
}

interface DataLoader {
  String loadData();
}

public class Demo1 {
  // correct implementation of lazy singleton
  // with DIP and DI
  public static void main(String[] args) {}
}

class DataLoaderImpl implements DataLoader {
  private Connection connection;

  public DataLoaderImpl(Connection connection) {
    this.connection = connection;
  }

  public String loadData() {
    return connection.getConnection().getClass().toString();
  }
}

class DbConnection implements Connection<DbConnection> {
  private static final DbConnection INSTANCE = new DbConnection();

  @Override
  public DbConnection getConnection() {
    System.out.println("Logic");
    return INSTANCE;
  }
}
