package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;

public class SideGearWithHopper extends AutoRoutine{

  private SideGearSimple dropGear;

  /**
   * Shooting path
   */
  private TurnToAngle turnToHopper;
  private DriveAtAngle driveToHopper;
  
  public SideGearWithHopper(boolean side) {
    dropGear = new SideGearSimple(side);
    turnToHopper = new TurnToAngle(-20 * (side?-1:1));
    driveToHopper = new DriveAtAngle(10, 0);
  }
  
  @Override
  public void run() {
  //get to and drop off the gear
    dropGear.run();
    
    //turn towards the boiler
    turnToHopper.run();
    waitUntilDone(1, turnToHopper::done);
        
    //ram into the boiler
    Robot.driveT.resetSensors();
    Robot.driveT.shiftHigh();
    driveToHopper.run();
    waitUntilDone(2.5, driveToHopper::done);
    
    Robot.driveT.setStop();
  }
  
  @Override
  public void end() {
  }
  
  
  
  
}
