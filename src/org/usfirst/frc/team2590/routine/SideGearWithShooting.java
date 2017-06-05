package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.TurnToTarget;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

/**
 * Drops a gear on one of the side pegs and 
 * shoots the preloaded balls
 * @author Connor_Hofenbitzer
 *
 */
public class SideGearWithShooting extends AutoRoutine implements RobotMap  {
 
  /**
   * Drop off the gear
   */
  private SideGearSimple getGear;

  /**
   * Shooting path
   */
  private TurnToTarget turnToBoiler;
  private DriveAtAngle driveToBoiler;
 
  
  public SideGearWithShooting(boolean left) {
    turnToBoiler = new TurnToTarget();
    getGear = new SideGearSimple(left);
    driveToBoiler = new DriveAtAngle(8, 0);
  }

  @Override
  public void run() {
    
    //get to and drop off the gear
    getGear.run();
    
    //revv the shooter
    Robot.shooter.setSetpoint(3300);
    Robot.shooter.revShooter();
   
    //ram into the boiler
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();
    driveToBoiler.run();
    waitUntilDone(2, driveToBoiler::done);
    
    //turn towards the boiler
    turnToBoiler.run();
    waitUntilDone(1.5 , turnToBoiler::done);
        
    Robot.driveT.setStop();
    Robot.feeder.feedIntoShooter();
  }

  @Override
  public void end() {
  }

}
