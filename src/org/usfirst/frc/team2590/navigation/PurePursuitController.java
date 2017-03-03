package org.usfirst.frc.team2590.navigation;

import org.usfirst.frc.team2590.robot.RobotMap;

/**
 * Controller which calculates the lookahead
 * point relative to the current point and commands
 * the robot to the position
 * @author Connor_Hofenbitzer
 * Thanks for help from Harsha Pavuluri throughout the project 
 */
public class PurePursuitController implements RobotMap{

  private Path path;
  private boolean flip;
  private boolean done;
  private double lookAhead;
  private Point lookAheadPoint;
  private DriveAtAngleController velCont;

  public PurePursuitController(double kF , double maxAcc , double lookAhead) {
    done = false;
    flip = false;
    this.path = new Path(null);
    this.lookAhead = lookAhead;
    lookAheadPoint = new Point(0, 0, 0);
    velCont = new DriveAtAngleController(maxAcc , kF , DRIVETURNCOMP , 0);
  }

  public double Calculate(Point currPoint , boolean isRight ,double dt) {
    
    //gets the lookahead point
    try {
      lookAheadPoint = path.findPoint(currPoint, lookAhead);
    } catch(Exception e) {
      done = true;
      return 0.0;
    }
    
    double currTheta = Math.toDegrees(currPoint._theta);

    double theta = new PathSegment(currPoint , lookAheadPoint).theta_;

    //calculates travel from start of path to lookahead
    double travel = (new PathSegment(new Point(0,0,0), lookAheadPoint).length)*(flip?-1:1);
    
    //current distance from the start of the path to current point
    double currDist = (new PathSegment(new Point(0,0,0) , currPoint).length)*(flip?-1:1);
    
    velCont.setSetpoint(travel , theta );
    //calculates output to drive motor
    //System.out.println("curr " + currPoint._x + " " + currPoint._y);
    return velCont.calculate(currDist , currTheta , isRight , dt);
  }

  public boolean isDone() {
    return done || path.isDone();
  }
  
  public void setPath(Path newPath) {
    this.path = newPath;
  }
  
  public void flip() {
    flip = true;
  }
 

}
