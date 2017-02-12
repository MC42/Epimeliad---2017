package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;

public class FrontGearDrop extends AutoRoutine {

  DriveAtAngle driveToDropGear;

  public FrontGearDrop() {
    driveToDropGear = new DriveAtAngle(-7,0);
  }

  @Override
  public void run() {
    driveToDropGear.run();
  }

  @Override
  public void end() {

  }

}
