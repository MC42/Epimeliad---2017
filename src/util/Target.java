package util;

/**
 * Camera target
 * @author James Aikins
 *
 */
public class Target {

  double x;
  double y;
  double area;

  public Target() {
    x = 0;
    y = 0;
    area = 0;
  }

  public Target(double x, double y, double area) {
    this.x = x;
    this.y = y;
    this.area = area;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getArea() {
    return area;
  }

  public void setCoord(double x, double y) {
    this.x = x;
    this.y = y;
  }
}
