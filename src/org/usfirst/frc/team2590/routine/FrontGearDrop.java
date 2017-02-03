package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.DriveStraight;

public class FrontGearDrop extends AutoRoutine {

  DriveStraight driveForward;
  DriveAtAngle driveToHopper;
  DriveStraight driveToDropGear;

  public FrontGearDrop() {
    driveForward = new DriveStraight(5);
    driveToHopper = new DriveAtAngle(10,45);
    driveToDropGear = new DriveStraight(-5);
  }

  @Override
  public void run() {
    driveToDropGear.run();
    waitUntilDone(driveToDropGear.isDone());

    driveForward.run();
    waitUntilDone(driveForward.isDone());

    driveToHopper.run();
    waitUntilDone(driveToHopper.isDone());
  }

  @Override
  public void end() {

  }

}
