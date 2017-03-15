package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.CheckDrive;
import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class SideGearWithShooting extends AutoRoutine implements RobotMap  {
  //turn to the peg
  private TurnToAngle turnInPlace;

  //drive straights
  private DriveAtAngle driveToGear;
  private DriveAtAngle driveAwayGear;
  private DriveAtAngle driveBeforeTurn;
  private CheckDrive driveInOne;
  private CheckDrive driveInTwo;

  //constants
  private final static double AngleToGear = 60; //60
  private final static double DistanceToPeg = (3);
  private final static double distanceToFirst = ((70+ROBOTLENGTH)/12); //86
  
  
  /**
   * Shooting path
   */
  private TurnToAngle turnToBoiler;
  private DriveAtAngle driveToBoiler;
 
  
  public SideGearWithShooting(boolean left) {
    turnToBoiler = new TurnToAngle(-20);
    driveToBoiler = new DriveAtAngle(10, 0);
    driveInOne = new CheckDrive(true);
    driveInTwo = new CheckDrive(false);
    driveAwayGear = new DriveAtAngle(4, 0);
    driveToGear = new DriveAtAngle(-DistanceToPeg, 0);
    driveBeforeTurn = new DriveAtAngle(-distanceToFirst, 0);
    turnInPlace = new TurnToAngle(AngleToGear*(left?1:-1));
  }

  @Override
  public void run() {
    
    //drive to the point before the turn
    driveBeforeTurn.run();
    waitUntilDone(3, driveBeforeTurn::done);
    
    //turn to face the peg
    turnInPlace.run();
    Timer.delay(1.5);
    Robot.driveT.shiftHigh();
    
    //drive into the peg
    driveToGear.run();
    waitUntilDone(.75, driveToGear::done);
    Robot.gearHold.outTakeGear();
    Timer.delay(1);
    
    //drive away
    driveAwayGear.run();
    Timer.delay(.75);
    Robot.gearHold.stopGearIntake();
    Timer.delay(.75);
    driveInOne.run();
    waitUntilDone(2, driveInOne::done);
    
    Robot.shooter.setSetpoint(6300);
    Robot.shooter.revShooter();
    turnToBoiler.run();
    Timer.delay(1);
    Robot.driveT.resetSensors();
    Robot.driveT.shiftHigh();

    driveToBoiler.run();
    Timer.delay(2.5);
    Robot.driveT.setStop();
    Robot.shooter.shootWhenReady();
  }

  @Override
  public void end() {
  }

}
