package org.usfirst.frc.team2590.Controllers;

import util.NemesisVector;

/**
 * A segment of two points , can calculate the closest point on segment to the robot
 * A segment will always be straight
 * @author Connor_Hofenbitzer
 *
 */
public class PathSegment {

  private double slope;
  private double theta;
  private double length;
  private Point endPoint;
  private Point startPoint;
  private NemesisVector unitVector;

  public PathSegment(Point start , Point end) {

    endPoint = end;
    startPoint = start;

    //arc length calculations
    //field coordinates
    double dX = end.getX() - start.getX();
    double dY = end.getY() - start.getY();
    
    length = Math.hypot(dX, dY);
    theta = Math.toDegrees(Math.atan2(dY, dX));
    unitVector = new NemesisVector(dX/length, dY/length, 1);
    slope = dY/dX;
  }

  /**
   * gets the closest point on the segment given the robots current position
   * @param current : the robots current position
   * @return the closest point on the line segment
   */
  public Point getClosestOnPath( Point current ) {
   
    double distance = unitVector.getX()*(current.getY() - startPoint.getY()) -
        unitVector.getY()*(current.getX() - startPoint.getX());

    double xPoint = current.getX() + distance*unitVector.getY();
    double yPoint = current.getY() - distance*unitVector.getX();

    return new Point(xPoint , yPoint , endPoint.getTheta());
  }

  /**
   * the index on the segment , ex. if the robot is 5m along on a 10m. segment this will return 0.5
   * @param closest : the robots position on the segment
   * @return : a double representing percentage across
   */
  public double getPercentAcross(double partLength) {
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
    double x = start.getX() + (distance/r);
    double y = start.getY() + ((distance*slope)/r);

    return new Point(x , y , 0);
  }
  
  public Point getEnd() {
    return endPoint;
  }
  
  public Point getStart() {
    return startPoint;
  }
  
 
  
  public double getTheta() {
    return theta;
  }

  public double getLength() {
    return length;
  }

  public double getSlope() {
    return slope;
  }
}
