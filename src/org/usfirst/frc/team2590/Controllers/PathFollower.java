package org.usfirst.frc.team2590.Controllers;

import org.usfirst.frc.team2590.robot.RobotMap;

/**
 * Controller which calculates the lookahead
 * point relative to the current point and commands
 * the robot to the position
 * @author Connor_Hofenbitzer
 */
public class PathFollower implements RobotMap{

  private Path path;
  private boolean flip;
  private boolean done;
  private double lookAhead;
  private Point lookAheadPoint;
  private DriveAtAngleController velCont;

  public PathFollower(double kF , double lookAhead) {
    
    done = false;
    flip = false;
    this.path = new Path(null);
    this.lookAhead = lookAhead;
    lookAheadPoint = new Point(0, 0, 0);
    velCont = new DriveAtAngleController(kF , DRIVETURNCOMP , 0);
    
  }

  public double Calculate(Point currPoint , boolean isRight ,double dt) {

    //gets the lookahead point
    try {
      lookAheadPoint = path.findPoint(currPoint, lookAhead);
    } catch(Exception e) {
      done = true;
      return 0.0;
    }

    double currTheta = Math.toDegrees(currPoint.getTheta());

    
    double theta = new PathSegment(currPoint , lookAheadPoint).getTheta();

    //calculates travel from start of path to lookahead
    double travel = (new PathSegment(new Point(0,0,0), lookAheadPoint).getLength())*(flip?-1:1);

    //current distance from the start of the path to current point
    double currDist = (new PathSegment(new Point(0,0,0) , currPoint).getLength())*(flip?-1:1);

    velCont.setSetpoint(travel , theta );
    
    //calculates output to drive motor
    return velCont.calculate(currDist , currTheta , isRight , dt);
  }

  public boolean isDone() {
    return done || path.isDone();
  }

  public void setPath(Path newPath) {
    done = false;
    this.path = newPath;
    lookAheadPoint = new Point(0,0,0);
  }

  public void unFlip() {
    flip = false;
  }
  
  public void flip() {
    flip = true;
  }


}
