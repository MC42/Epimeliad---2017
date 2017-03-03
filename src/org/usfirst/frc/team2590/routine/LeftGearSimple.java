package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class LeftGearSimple extends AutoRoutine implements RobotMap{

  private DriveAtAngle driveToGear;
  private DriveAtAngle driveAwayGear;
  private DriveAtAngle driveBeforeTurn;
  private TurnToAngle turnInPlace;

  
  private final static double AngleToGear = 60; //60
  private final static double DistanceToPeg = ((40+ROBOTLENGTH)/12);
  private final static double distanceToFirst = ((66+ROBOTLENGTH)/12); //86

  public LeftGearSimple() {
    driveBeforeTurn = new DriveAtAngle(-distanceToFirst, 0);
    driveToGear = new DriveAtAngle(-DistanceToPeg, 0);
    driveAwayGear = new DriveAtAngle(30/12, 0);
    turnInPlace = new TurnToAngle(60);
  }
  
  @Override
  public void run() {
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();
    driveBeforeTurn.run();
    waitUntilDone(3, driveBeforeTurn::done);
    turnInPlace.run();
    Timer.delay(1.5);
    Robot.driveT.shiftHigh();
    driveToGear.run();
    Robot.gearHold.openWings();
    waitUntilDone(1, driveToGear::done);
    driveAwayGear.run();
  }

  @Override
  public void end() {
  }
  

}
