package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;

public class OnlyHopper extends AutoRoutine{

  DriveAtAngle driveIntoHopper;

  public OnlyHopper() {
    driveIntoHopper = new DriveAtAngle(12.75, 43);
  }

  @Override
  public void run() {
    driveIntoHopper.run();
  }

  @Override
  public void end() {

  }

}
