package solid.lsp;

class Rectangle {
    protected int width, height;

    public Rectangle() {
    }

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    public int getArea() {
        return width * height;
    }
}


class Square extends Rectangle {
    public Square() {}

    public Square(int size) {
        width = height = size;
    }

    @Override
    // Violation of LSP
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width);
    }


    @Override
    // Violation of LSP
    public void setHeight(int height) {
        super.setHeight(height);
        super.setWidth(height);
    }
}

public class Demo {
    static void useIt(Rectangle r) {
        int w = r.getWidth();
        r.setHeight(10);
        // area = width * 10
        System.out.println(
                "Expected area of : " + (w * 10) + ", got " + r.getArea()
        );
    }

    public static void main(String[] args) {
        Rectangle rc = new Rectangle(2,3);
        useIt(rc);

        // will not work. LSP violation
        Rectangle square = new Square();
        square.setWidth(5);
        useIt(square);

        // will work
        Square square2 = new Square();
        square2.setWidth(5);
        useIt(square);
    }
}