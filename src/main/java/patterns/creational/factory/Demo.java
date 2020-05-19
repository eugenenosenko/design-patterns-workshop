package patterns.creational.factory;

import static patterns.creational.factory.CoordinateSystem.CARTESIAN;
import static patterns.creational.factory.CoordinateSystem.POLAR;

enum CoordinateSystem {
  CARTESIAN,
  POLAR;
}

public class Demo {
  public static void main(String[] args) {
    Point p = Point.newCartesianPoint(4, 5);
    Point p1 = Point.newPolarPoint(4, 5);
    System.out.println(p);
    System.out.println(p1);
  }
}

// separation of object creation and logic
// behind object creation into a PointFactory
// package level for Point constructor
class PointFactory {
  public static Point newCartesianPoint(double x, double y) {
    return new Point(x, y, CARTESIAN);
  }
  // factory method

  public static Point newPolarPoint(double x, double y) {
    return new Point(x, y, POLAR);
  }
}

class Point {
  private double x, y;

  // Initial constructor
  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  // Unified constructor
  Point(double a, double b, CoordinateSystem s) {
    // ugly switch statement
    // initialisation login within the constructor
    // to the user creating the object it's not apparent
    switch (s) {
      case POLAR:
        x = a * Math.cos(b);
        y = a * Math.sin(b);
        break;
      case CARTESIAN:
        x = a;
        y = a;
    }
  }
  // factory method

  public static Point newCartesianPoint(double x, double y) {
    return new Point(x, y, CARTESIAN);
  }
  // factory method

  public static Point newPolarPoint(double x, double y) {
    return new Point(x, y, POLAR);
  }

  @Override
  public String toString() {
    return "Point{" + "x=" + x + ", y=" + y + '}';
  }
}
