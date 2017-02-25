package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class LeftGearSimple extends AutoRoutine implements RobotMap{

  private DriveAtAngle driveToGear;
  private DriveAtAngle driveAwayGear;
  private DriveAtAngle driveBeforeTurn;
  
  private final static double AngleToGear = 60;
  private final static double DistanceToPeg = ((100-ROBOTLENGTH)/12);
  private final static double distanceToFirst = ((86-ROBOTLENGTH)/12);

  public LeftGearSimple() {
    driveBeforeTurn = new DriveAtAngle(-distanceToFirst, 0);
    driveToGear = new DriveAtAngle(-DistanceToPeg, AngleToGear);
    driveAwayGear = new DriveAtAngle(30/12, 0);
  }
  
  @Override
  public void run() {
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();
    driveBeforeTurn.run();
    waitUntilDone(3, driveBeforeTurn::done);
    driveToGear.run();
    waitUntilDone(3, driveToGear::done);
    Robot.gearHold.openWings();
    Timer.delay(0.5);
    driveAwayGear.run();
  }

  @Override
  public void end() {
  }
  

}
