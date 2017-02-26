package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class CoolDahanyAuto extends AutoRoutine implements RobotMap{
  
  TurnToAngle turnInPlace;
  DriveAtAngle driveBack;
  
  private DriveAtAngle driveToGear;
  private DriveAtAngle driveAwayGear;
  private DriveAtAngle driveBeforeTurn;
  
  private final static double AngleToGear = 60;
  private final static double DistanceToPeg = ((100-ROBOTLENGTH)/12);
  private final static double distanceToFirst = ((79-ROBOTLENGTH)/12);
  
  public CoolDahanyAuto() {
    driveBack = new DriveAtAngle(-(5.0/12.0), 0);
    turnInPlace = new TurnToAngle(-66);
    driveBeforeTurn = new DriveAtAngle(-distanceToFirst, 0);
    driveToGear = new DriveAtAngle(-DistanceToPeg, AngleToGear);
    driveAwayGear = new DriveAtAngle(30/12, 0);
  }
  
  @Override
  public void run() {
    Robot.gearHold.closeWings();
    Robot.shooter.setSetpoint(6500);
    Robot.shooter.shootNow();
    Timer.delay(3);
    Robot.shooter.stopShooter();
    driveBack.run();
    waitUntilDone(2, driveBack::done);
    Robot.driveT.resetSensors();
    Timer.delay(.1);
    turnInPlace.run();
    Timer.delay(1);
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();
    driveBeforeTurn.run();
    waitUntilDone(3, driveBeforeTurn::done);
    driveToGear.run();
    waitUntilDone(3, driveToGear::done);
    Robot.gearHold.openWings();
    Timer.delay(0.5);
    driveAwayGear.run();
    
    //runToGear.run();
  }

  @Override
  public void end() {
  }

}
