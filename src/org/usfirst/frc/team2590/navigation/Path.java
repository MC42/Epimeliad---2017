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
  private boolean done = false;

  public Path(ArrayList<PathSegment> segments) {
    System.out.println("started");
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

    //find the closest point on the path
    Point closestPoint = pathSeg.getClosestOnPath(myPos);

    //get the point after adding the lookahead
    Point pointAfterLook = pathSeg.pointTransform(closestPoint,lookAhead);

    //get the percent across the path
    double closestPointPercent = pathSeg.getPercentAcross(new PathSegment(pathSeg.startPoint , pointAfterLook).length );

    //prevents index out of bounds
    if(currentIndex >= segments.size()) {
      done = true;
      return myPos;
    }
    //if the CPI is less than one then return the point
    if(closestPointPercent < 1) {
      return pointAfterLook;
    }  else {
      //other wise increment the index
      pathSeg.endPoint.runInsideCommand();

      if(currentIndex == segments.size()-1) {
         done = true;
         return segments.get(segments.size()-1).endPoint;
      }
        
      currentIndex+=1;

      if(currentIndex <= segments.size()-1) {
        segments.get(currentIndex).startPoint.runInsideCommand();
      }
      

      System.out.println("current index " + currentIndex);
      return findPoint(pointAfterLook , lookAhead);
    }

  }

  public boolean isDone() {
    return done;
  }
  
  public double getRemaningPathLength(Point curr) {
    double dist = new PathSegment(segments.get(currentIndex).startPoint , curr).length;
    for(int i = currentIndex; i < segments.size()-1; i++) {
      dist+=segments.get(i).length;
    }
    return dist;
  }



}
