package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class SideGearSimple extends AutoRoutine implements RobotMap{

  //turn to the peg
  private TurnToAngle turnInPlace;

  //drive straights
  private DriveAtAngle driveToGear;
  private DriveAtAngle driveAwayGear;
  private DriveAtAngle driveBeforeTurn;

  //constants
  private final static double AngleToGear = 60; //60
  private final static double DistanceToPeg = ((40+ROBOTLENGTH)/12);
  private final static double distanceToFirst = ((66+ROBOTLENGTH)/12); //86

  public SideGearSimple(boolean left) {
    driveAwayGear = new DriveAtAngle(30/12, 0);
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
    Robot.gearHold.openWings();
    waitUntilDone(.75, driveToGear::done);
    
    //drive away
    driveAwayGear.run();
    
  }

  @Override
  public void end() {
  }


}
