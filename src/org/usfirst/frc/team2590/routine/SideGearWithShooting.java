package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

public class SideGearWithShooting extends AutoRoutine implements RobotMap  {
 
  private SideGearSimple getGear;

  /**
   * Shooting path
   */
  private TurnToAngle turnToBoiler;
  private DriveAtAngle driveToBoiler;
 
  
  public SideGearWithShooting(boolean left) {
    getGear = new SideGearSimple(left);
    turnToBoiler = new TurnToAngle(-20 * (left?1:-1));
    driveToBoiler = new DriveAtAngle(10, 0);
  }

  @Override
  public void run() {
    
    //get to and drop off the gear
    getGear.run();
    
    //revv the shooter
    Robot.shooter.setSetpoint(3300);
    Robot.shooter.revShooter();
    
    //turn towards the boiler
    turnToBoiler.run();
    waitUntilDone(1, turnToBoiler::done);
        
    //ram into the boiler
    Robot.driveT.resetSensors();
    Robot.driveT.shiftHigh();
    driveToBoiler.run();
    waitUntilDone(2.5, driveToBoiler::done);
    
    Robot.driveT.setStop();
    Robot.shooter.shootWhenReady();
  }

  @Override
  public void end() {
  }

}
