package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToTarget;
import org.usfirst.frc.team2590.Controllers.PathSegment;
import org.usfirst.frc.team2590.Controllers.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * Places the gear on the center peg
 * and shoots
 * @author Connor_Hofenbitzer
 *
 */
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
  private TurnToTarget turnToBoiler;
  private FrontGearDrop getTheGearOn;
  
  public FrontGearShoot() {
    
    /**
     * Points
     */
    start = new Point(0,0,0);
    middle = new Point(2.5,-4,0); //2.5 -4 3600rpm
    shootingPoint = new Point(3.5, -5.5);
    
    /**
     * Segments
     */
    middleSegment = new PathSegment(middle, shootingPoint);
    getToShootingPoint = new PathSegment(start, middle);
    
    /**
     * Path
     */
    getToShootingPath = new RunPath(getToShootingPoint,middleSegment);
    
    /**
     * Basic movement
     */
    turnToBoiler = new TurnToTarget();
    getTheGearOn = new FrontGearDrop();
    
  }
  
  @Override
  public void run() {
    
    //init robot
    Robot.driveT.shiftHigh();
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();
    Robot.gearHold.stopGearIntake();
    Robot.driveT.resetDriveController();
    Robot.shooter.setInterpolation(true);
    
    //drop the gear on the peg
    getTheGearOn.run();
   
    //reset all
    Robot.driveT.resetPath();
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();
    
    //drive to the boiler
    getToShootingPath.run();
    Robot.expandBox.openBox();
    waitUntilDone(3, getToShootingPath::done);

    //reset all
    Robot.driveT.setStop();
    Robot.driveT.shiftLow();
    Timer.delay(.25);
    
    //revv the shooter
    Robot.shooter.revShooter();
    
    //turn to the boiler
    turnToBoiler.run();
    waitUntilDone(3, turnToBoiler::done);
    Robot.driveT.setStop();
    Robot.driveT.shiftHigh();
    Timer.delay(.25);
    
    turnToBoiler.run();
    Robot.shooter.setSetpoint(3400);
    waitUntilDone(3, turnToBoiler::done);
    Robot.driveT.setStop();
    
    if(!Robot.shooter.getAboveTarget())
      Timer.delay(1);
    
    //shots fired
    Robot.feeder.feedIntoShooter();
  }
  

  @Override
  public void end() {
  }

}
