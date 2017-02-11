package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.DriveStraight;

/**
 * Drops the gear on the center peg and hits the hopper
 * @author Connor_Hofenbitzer
 *
 */
public class FrontGearDrop extends AutoRoutine {

  private DriveStraight driveForward;
  private DriveAtAngle driveToHopper;
  private DriveStraight driveToDropGear;

  public FrontGearDrop() {
    driveForward = new DriveStraight(5);
    driveToHopper = new DriveAtAngle(-10,-45);
    driveToDropGear = new DriveStraight(-5);
  }

  @Override
  public void run() {
    
    //drive backwards to drop the gear
    runCommand(driveToDropGear);
    
    //drives out after releasing gear
    runCommand(driveForward);
    
    //drives over to the hopper
    runCommand(driveToHopper);
  }

  @Override
  public void end() {

  }

}
