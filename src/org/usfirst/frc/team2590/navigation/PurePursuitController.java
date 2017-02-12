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
  private double lookAhead;
  private double driveLength;
  private Point lookAheadPoint;
  private DriveAtAngleController velCont;

  public PurePursuitController(double kF , double maxAcc , double lookAhead, double driveLength) {
    this.path = new Path(null);
    this.lookAhead = lookAhead;
    this.driveLength = driveLength;
    lookAheadPoint = new Point(0, 0, 0);
    velCont = new DriveAtAngleController(maxAcc , kF , DRIVETURNCOMP);
  }

  public double Calculate(Point currPoint , boolean isRight) {

    //gets the lookahead point
    try {
      lookAheadPoint = path.findPoint(currPoint, lookAhead);
    } catch(IndexOutOfBoundsException e) {
      //System.out.println("get memed");
      return 0.0;
    }

    //calculates travel from start of path to lookahead
    double travel = (new PathSegment(new Point(0,0,0), lookAheadPoint).length);
    
    //current distance from the start of the path to current point
    double currDist = (new PathSegment(new Point(0,0,0) , currPoint).length);
    
    double theta = new PathSegment(currPoint , lookAheadPoint).theta_;
    velCont.setSetpoint(travel , theta );
    return velCont.calculate(currDist , currPoint._theta , isRight);
  }

  public void setPath(Path newPath) {
    this.path = newPath;
  }

}
