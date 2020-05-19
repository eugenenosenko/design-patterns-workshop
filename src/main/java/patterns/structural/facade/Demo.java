package patterns.structural.facade;

public class Demo {
  public static void main(String[] args) {
    BufferCache cache = new BufferCache(1000);
    Buffer buffer = new Buffer(cache, 10);
    ViewPort viewPort = new ViewPort(buffer, 0, 0);
    // not facaded
    Console console = new Console(viewPort, true);

    // facade method
    Console console2 = Console.newConsole(false);
  }
}

class Buffer {
  private BufferCache cache;
  private int size;

  public Buffer(BufferCache cache, int size) {
    this.cache = cache;
    this.size = size;
  }
}

class ViewPort {
  private Buffer buffer;
  private int x, y;

  public ViewPort(Buffer buffer, int x, int y) {
    this.buffer = buffer;
    this.x = x;
    this.y = y;
  }
}

class Console {
  private ViewPort viewPort;
  private boolean active;

  public Console(ViewPort viewPort, boolean active) {
    this.viewPort = viewPort;
    this.active = active;
  }

  public static Console newConsole(boolean active) {
    BufferCache cache = new BufferCache(1000);
    Buffer buffer = new Buffer(cache, 10);
    ViewPort viewPort = new ViewPort(buffer, 0, 0);
    // not facaded
    return new Console(viewPort, active);
  }
}

class BufferCache {
  private int size;

  public BufferCache(int size) {
    this.size = size;
  }
}
