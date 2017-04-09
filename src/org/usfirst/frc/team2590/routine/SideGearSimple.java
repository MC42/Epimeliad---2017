package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.CheckDrive;
import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class SideGearSimple extends AutoRoutine implements RobotMap{

  //turn to the peg
  private TurnToAngle turnInPlace;

  //drive straights
  private CheckDrive driveInOne;
  private CheckDrive driveInTwo;
  private DriveAtAngle driveToGear;
  private DriveAtAngle driveAwayGear;
  private DriveAtAngle driveBeforeTurn;
  
  //constants
  private final static double AngleToGear = 60; //60
  private final static double DistanceToPeg = (5);
  private final static double distanceToFirst = ((85+ROBOTLENGTH)/12); //86
 // private final static double distanceToFirst = ((85)/12);
  public SideGearSimple(boolean left) {
    driveInOne = new CheckDrive(true);
    driveInTwo = new CheckDrive(false);
    driveAwayGear = new DriveAtAngle(5, 0);
    driveToGear = new DriveAtAngle(-DistanceToPeg, 0);
    turnInPlace = new TurnToAngle(AngleToGear*(left?1:-1));
    driveBeforeTurn = new DriveAtAngle(-distanceToFirst, 0);
  }

  @Override
  public void run() {
    
    //drive to the point before the turn
    driveBeforeTurn.run();
    waitUntilDone(2.0, driveBeforeTurn::done);
    Robot.driveT.shiftLow();
    System.out.println("done ");
    
    
    //turn to face the peg
    turnInPlace.run();
    waitUntilDone(1, turnInPlace::done); //1.5
    Robot.driveT.shiftHigh();

    
    //drive into the peg
    driveToGear.run();
    waitUntilDone(.75, driveToGear::done);
    Robot.gearHold.outTakeGear();
    Timer.delay(1); //allow the gear to fall off
    
    //drive away
    driveAwayGear.run(); //drives away
    waitUntilDone(.75, driveAwayGear::done);
    Robot.gearHold.stopGearIntake();
    Timer.delay(1); //updates the current average

    //do the checks
    driveInOne.run();
    waitUntilDone(2, driveInOne::done);
    Timer.delay(1); //updates current average
    driveInTwo.run();
    waitUntilDone(2, driveInTwo::done);
    
  }

  @Override
  public void end() {
  }


}
