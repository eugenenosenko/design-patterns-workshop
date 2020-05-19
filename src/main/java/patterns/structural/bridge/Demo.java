package patterns.structural.bridge;

interface Render {
  void renderCircle(float radius);
}

// Bridge decouples hierarchy
// decouple abstraction from implementation
// stronger from of encapsulation
// prevent complexity explosion
// avoid 2x2 scenario
//      ThreadScheduler
//             |
//      ----------------
//      |               |
//   PreEmptThrSc    CoopEmptThrSc
//      |               |
//  --------         ----------
//  |      |         |        |
// WindPTS LinPTS   WinCTS   LinuxCTS
//
public class Demo {
  public static void main(String[] args) {
    Render r = new VectorRenderer();
    Circle circle = new Circle(r, 5);

    circle.draw();
    circle.reshape(3);
    circle.draw();
  }
}

class VectorRenderer implements Render {

  @Override
  public void renderCircle(float radius) {
    System.out.println("Drawing a circle with radius " + radius);
  }
}

class RasterRenderer implements Render {
  @Override
  public void renderCircle(float radius) {
    System.out.println("Drawing pixels for a circle with radius " + radius);
  }
}

abstract class Shape {
  protected Render render;

  public Shape(Render render) {
    this.render = render;
  }

  abstract void draw();

  abstract void reshape(float factor);
}

class Circle extends Shape {
  private float radius;

  public Circle(Render render, float radius) {
    super(render);
    this.radius = radius;
  }

  @Override
  void draw() {
    render.renderCircle(radius);
  }

  @Override
  void reshape(float factor) {
    this.radius *= factor;
  }
}
