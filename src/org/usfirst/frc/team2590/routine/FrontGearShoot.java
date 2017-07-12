package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.Commands.TurnToTarget;
import org.usfirst.frc.team2590.Controllers.PathSegment;
import org.usfirst.frc.team2590.Controllers.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class FrontGearShoot extends AutoRoutine {

  /**
   * Points
   */
  private Point start;
  private Point middle;
  private Point shootingPoint;

  /**
   * Segments
   */
  private PathSegment middleSegment;
  private PathSegment getToShootingPoint;

  /**
   * Path
   */
  private RunPath getToShootingPath;

  /**
   * Basic movement
   */
  private boolean done;
  private TurnToTarget turnToBoiler;
  private TurnToAngle turnToBoilerOne;
  private FrontGearDrop getTheGearOn;

  public FrontGearShoot(boolean left) {

    done  = false;
    /**
     * Points
     */
    start = new Point(0, 0, 0);
    middle = new Point(2.5, -4 * (left ? 1 : -1), 0); // 2.5 -4 3600rpm
    shootingPoint = new Point(2.6, -5 * (left ? 1 : -1));

    /**
     * Segments
     */
    middleSegment = new PathSegment(middle, shootingPoint);
    getToShootingPoint = new PathSegment(start, middle);

    /**
     * Path
     */
    getToShootingPath = new RunPath(getToShootingPoint, middleSegment);

    /**
     * Basic movement
     */
    turnToBoilerOne = new TurnToAngle(35 * (left ? -1 : 1));
    turnToBoiler = new TurnToTarget();
    getTheGearOn = new FrontGearDrop();

  }

  @Override
  public void run() {

    // init robot
    Robot.driveT.shiftHigh();
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();
    Robot.gearHold.stopGearIntake();
    Robot.driveT.resetDriveController();

    // drop the gear on the peg
    getTheGearOn.run();

    // reset all
    Robot.driveT.resetPath();
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();
    Robot.shooter.setInterpolation(true);
    Robot.shooter.setSetpoint(3400);
    Robot.shooter.revShooter();

    // drive to the boiler
    getToShootingPath.run();
    waitUntilDone(3, getToShootingPath::done);

    Robot.expandBox.openBox();
    // reset all
    Robot.driveT.setStop();
    Robot.driveT.shiftLow();
    Timer.delay(.25);
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();

    // revv the shooter
    Robot.intake.agitate();
    turnToBoilerOne.run();
    waitUntilDone(1.5, turnToBoiler::done);
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();

    Robot.driveT.setStop();
    Robot.driveT.resetPath();
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();

    turnToBoiler.run();
    waitUntilDone(1.5, turnToBoiler::done);
    Robot.driveT.setStop();
    Robot.shooter.setSetpoint(3400);

    // shots fired
    Robot.feeder.feedIntoShooter();
    done = true;
  }
  
  public boolean getDone() {
    return done;
  }
  
  @Override
  public void end() {
  }

}
