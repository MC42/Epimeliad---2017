package org.usfirst.frc.team2590.navigation;

/**
 * A segment of two points , can calculate the closest point on segment to the robot
 * A segment will always be straight
 * @author Connor_Hofenbitzer
 *
 */
public class PathSegment {

  public double slope;
  public double theta_;
  public double length;
  public Point endPoint;
  public Point startPoint;

  public PathSegment(Point start , Point end) {

    endPoint = end;
    startPoint = start;

    //arch length calculations
    double dX = end._x - start._x;
    double dY = end._y - start._y;
    length = Math.sqrt( (dX*dX) + (dY*dY) );
    theta_ = Math.toDegrees(Math.asin(dY/length));
    slope = dY/dX;
  }

  /**
   * gets the closest point on the segment given the robots current position
   * @param current : the robots current position
   * @return the closest point on the line segment
   */
  public Point getClosestOnPath( Point current ) {

    double s = (endPoint._y-startPoint._y)/(endPoint._x-startPoint._x);
    double xPoint = (startPoint._x * (s*s) - (startPoint._y - current._y)*s + current._x) / ((s*s)+1);
    double yPoint = s * (xPoint - startPoint._x) + startPoint._y;

    return new Point(xPoint , yPoint , endPoint._theta);
  }

  /**
   * the index on the segment , ex. if the robot is 5m along on a 10m. segment this will return 0.5
   * @param closest : the robots position on the segment
   * @return : a double representing percentage across
   */
  public double getPercentAcross(double partLength) {
    /*double partLength = Math.sqrt( (closest._x - startPoint._x) * (closest._x - startPoint._x) +
										(closest._y - startPoint._y) * (closest._y - startPoint._y));*/
    return (partLength) / length;
  }

  /**
   * Given an index will return how much segment is left
   * @param percent : percent along the segment
   * @return a double representing the amount of segment left in units
   */
  public double getDistanceLeft(double percent) {
    return length - (percent * length);
  }

  /**
   * Translates a point along the segment by a certain distance, the point will be constrained onto the segment,
   * given that the start point is on the segment
   * @param start : robots starting position
   * @param distance : amount to translate it by
   * @return : a new point which is translated
   */
  public Point pointTransform(Point start , double distance) {
    double r = Math.sqrt(1 + (slope*slope));
    double x = start._x + (distance/r);
    double y = start._y + ((distance*slope)/r);

    return new Point(x , y , 0);
  }



  public double getLength() {
    return length;
  }

  public double getSlope() {
    return slope;
  }

  public Point getStartPoint() {
    return startPoint;
  }

  public Point getEndPoint() {
    return endPoint;
  }

}
