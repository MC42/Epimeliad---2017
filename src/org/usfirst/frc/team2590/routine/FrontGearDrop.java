package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.DriveStraight;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class FrontGearDrop extends AutoRoutine {

  DriveAtAngle driveForward;
  DriveAtAngle driveToHopper;
  DriveAtAngle driveToDropGear;

  public FrontGearDrop() {
    driveForward = new DriveAtAngle(0.5, 0);
    driveToHopper = new DriveAtAngle(3,80);
    driveToDropGear = new DriveAtAngle(-7,0);
  }

  @Override
  public void run() {
    driveToDropGear.run();
    Timer.delay(3);
    driveForward.run();
    Timer.delay(2);
    driveToHopper.run();
   // driveToHopper.run();
  }

  @Override
  public void end() {

  }

}
