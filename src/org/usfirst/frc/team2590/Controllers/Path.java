package org.usfirst.frc.team2590.Controllers;

import java.util.ArrayList;

/**
 * An arraylist of segments to follow, curves are achived by straight segments oriented at different angles
 * @author Connor_Hofenbitzer
 *
 */
public class Path {
  
  private boolean done;
  private int currentIndex;
  private ArrayList<PathSegment> segments;

  public Path(ArrayList<PathSegment> segments) {
    done = false;
    currentIndex = 0;
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
    double closestPointPercent = pathSeg.getPercentAcross(new PathSegment(pathSeg.getStart() , pointAfterLook).getLength() );

    //prevents index out of bounds
    if(currentIndex >= segments.size()) {
      done = true;
      return myPos;
    }
    //if the CPI is less than one then return the point
    if(closestPointPercent < 1) {
      return pointAfterLook;
    } else {
      //other wise increment the index
      pathSeg.getEnd().runInsideCommand();

      if(currentIndex == segments.size()-1) {
         done = true;
         return segments.get(segments.size()-1).getEnd();
      }
      currentIndex+=1;

      if(currentIndex <= segments.size()-1) {
        segments.get(currentIndex).getStart().runInsideCommand();
      }
      
      return findPoint(pointAfterLook , lookAhead);
    }

  }

  public boolean isDone() {
    return done;
  }
  
  public double getRemaningPathLength(Point curr) {
    double dist = new PathSegment(curr , segments.get(currentIndex).getEnd()).getLength();
    
    for(int i = currentIndex; i < segments.size()-1; i++) {
      dist += segments.get(i).getLength();
    }
    return dist;
  }



}
