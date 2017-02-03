package org.usfirst.frc.team2590.navigation;

/**
 * Controller which calculates the lookahead
 * point relative to the current point and commands
 * the robot to the position
 * @author Connor_Hofenbitzer
 * Thanks for help from Harsha Pavuluri throughout the project
 */
public class PurePursuitController {

  private Path path;
  private double lookAhead;
  private double driveLength;
  private Point lookAheadPoint;
  private DriveStraightController velCont;

  public PurePursuitController(double kF , double maxAcc , double lookAhead, double driveLength) {
    this.path = new Path(null);
    this.lookAhead = lookAhead;
    this.driveLength = driveLength;
    lookAheadPoint = new Point(0, 0, 0);
    velCont = new DriveStraightController(maxAcc , kF);
  }

  public double Calculate(Point currPoint , boolean isRight) {

    //gets the lookahead point
    try {
      lookAheadPoint = path.findPoint(currPoint, lookAhead);
    } catch(IndexOutOfBoundsException e) {
      return 0.0;
    }

    //calculates travel from start of path to lookahead
    double travel = (new PathSegment(new Point(0,0,0), lookAheadPoint).length) +
        ( (driveLength/2) * ((isRight)?-1:1) );

    //current distance from the start of the path to current point
    double currDist = (new PathSegment(new Point(0,0,0) , currPoint).length) +
        ( (driveLength/2) * ((isRight)?-1:1) );

    //sets the velocity controllers setpoint and calculates output
    velCont.setSetpoint(travel);
    return velCont.calculate(currDist);
  }

  public void setPath(Path newPath) {
    this.path = newPath;
  }

}
