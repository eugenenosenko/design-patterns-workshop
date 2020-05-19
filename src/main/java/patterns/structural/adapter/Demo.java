package patterns.structural.adapter;

import java.util.ArrayList;
import java.util.List;

public class Demo {

  public static void main(String[] args) {
    Line line = new Line(new Point(1, 3), new Point(3, 5));

    VectorRectangle lines = new VectorRectangle(2, 5, 6, 7);
    VectorRectangle lines2 = new VectorRectangle(3, 6, 7, 9);
    List<VectorRectangle> lines1 = List.of(lines, lines2);

    draw(lines1);
  }

  public static void drawPoint(Point point) {
    System.out.println('.');
  }

  public static void draw(List<VectorRectangle> v) {
    for (VectorRectangle ve : v) {
      for (Line l : ve) {
        LineToPointAdapter points = new LineToPointAdapter(l);
        points.forEach(Demo::drawPoint);
      }
    }
  }
}

class VectorObject extends ArrayList<Line> {}

class LineToPointAdapter extends ArrayList<Point> {
  public LineToPointAdapter(Line l) {
    add(l.end);
    add(l.start);
  }
}

class VectorRectangle extends VectorObject {
  public VectorRectangle(int x, int y, int width, int height) {
    add(new Line(new Point(1, 3), new Point(3, 5)));
    add(new Line(new Point(1, 3), new Point(3, 5)));
    add(new Line(new Point(1, 3), new Point(3, 5)));
    add(new Line(new Point(1, 3), new Point(3, 5)));
  }
}

class Point {
  private final int x;
  private final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Point{" + "x=" + x + ", y=" + y + '}';
  }
}

class Line {
  final Point start;
  final Point end;

  Line(Point start, Point end) {
    this.start = start;
    this.end = end;
  }
}
