package org.usfirst.frc.team2590.navigation;

import java.util.ArrayList;

/**
 * An arraylist of segments to follow, curves are achived by straight segments oriented at different angles
 * @author Connor_Hofenbitzer
 *
 */
public class Path {
  private ArrayList<PathSegment> segments;
  private int currentIndex = 0;

  public Path(ArrayList<PathSegment> segments) {
    this.segments = segments;

  }

  /**
   * Given robots starting position and lookahead will calculate a point on the path that is lookahead distance
   * away from the robots current point
   * @param myPos : robots current position
   * @param lookAhead : tuned value, larger look ahead = less oscilation but longer to get back on path,
   * 						short look ahead will oscilate more but will get to path quicker
   * @return a point on the path
   */
  public Point findPoint(Point myPos , double lookAhead) {
    //get the path segment the robot is currently on
    PathSegment pathSeg = segments.get(currentIndex);
    System.out.println("path seg length " + pathSeg.getLength());
    //find the closest point on the path
    Point closestPoint = pathSeg.getClosestOnPath(myPos);
    System.out.println("current closest " + closestPoint._x);
    //get the point after adding the lookahead
    Point pointAfterLook = pathSeg.pointTransform(closestPoint,lookAhead);
    System.out.println("point after look " + pointAfterLook._x);
    //get the percent across the path
    double closestPointIndex = pathSeg.getPercentAcross(new PathSegment(pathSeg.startPoint , pointAfterLook).length );

    if(currentIndex == segments.size()) {
      return myPos;
    }

    if(closestPointIndex < 0.99) {
      return pointAfterLook;
    }  else {
      currentIndex +=1;
      return pointAfterLook;
    }
  }

  public double getRemaningPathLength(Point curr) {
    return new PathSegment(curr , segments.get(segments.size()-1).endPoint).length;
  }



}
