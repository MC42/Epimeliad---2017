package org.usfirst.frc.team2590.navigation;


/**
 * Describes a point on the XY-plane
 * X-axis points at 0 rad
 * @author Connor_Hofenbitzer
 *
 */

public class Point {
  private static final double kEp = 1E-9;

  double _x;
  double _y;
  public double _theta;

  public Point(double x, double y, double theta){
    _x = x;
    _y = y;
    _theta = theta;
  }

  public double getX() {
    return _x;
  }


  public double getY() {
    return _y;
  }


  public double getTheta() {
    return _theta;
  }


  /**
   * Returns this rotated about the origin
   * @param theta The amount to rotate the point by (radians, positive counterclockwise)
   * @return New point with the updated x, y, and theta
   */
  public Point rotateAboutOrigin(double theta){
    double newX = _x * Math.cos(theta) - _y * Math.sin(theta);
    double newY = _x * Math.sin(theta) + _y * Math.cos(theta);
    //System.out.printf("_theta: %.5f inTheta: %.5f%n", _theta, theta);

    return new Point(newX, newY, _theta + theta);
  }

  /**
   * Fits a circular arc to a given length and exit angle
   * @param arcLength Length of the arc
   * @param dTheta Central angle of the arc (radians)
   * @return End point of the arc (start at origin)
   */
  public static Point fromVelocity(double arcLength, double dTheta){
    if ((Math.abs(dTheta) % (2*Math.PI)) < kEp){
      return new Point(arcLength, 0, 0);
    } else {
      double radius = arcLength / dTheta;
      double arcX = radius * Math.sin(dTheta);
      double arcY = radius - (radius * Math.cos(dTheta));
      return new Point(arcX, arcY, dTheta);
    }
  }

  /**
   * Translates this point
   * @param tPoint The point to translate by
   * @return Translated point
   */
  public Point translateBy(Point tPoint){
    Point preRotate = tPoint.rotateAboutOrigin(_theta);
    return new Point(_x+preRotate._x, _y+preRotate._y, preRotate._theta);
  }
}