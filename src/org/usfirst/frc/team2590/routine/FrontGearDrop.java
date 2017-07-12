package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.CheckDrive;
import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

/**
 * 60 points
 * @author Connor_Hofenbitzer
 *
 */
public class FrontGearDrop extends AutoRoutine implements RobotMap{

  //checks if weve dropped it
  private CheckDrive driveInOne;
  private CheckDrive driveInTwo;
  
  //drive straights
  private DriveAtAngle driveOut;
  private DriveAtAngle driveToDropGear;

  public FrontGearDrop() {

    //drive straight
    driveOut = new DriveAtAngle(4, 0);
    driveToDropGear = new DriveAtAngle(-7.833, 0);
    
    driveInOne = new CheckDrive(true);
    driveInTwo = new CheckDrive(false);
  }

  @Override
  public void run() {

    Robot.gearHold.turnOnGrip(true);
    
    //drive to the gear
    driveToDropGear.run();
    waitUntilDone(2, driveToDropGear::done);
    Robot.gearHold.outTakeGear();
    Timer.delay(.5); //wait for the gear to fall on the peg
    
    //drive away from the peg
    Robot.driveT.resetSensors();
    driveOut.run();
    Robot.driveT.resetSensors();
    Timer.delay(.25);
    Robot.gearHold.stopGearIntake();
    waitUntilDone(2, driveOut::done);
    Timer.delay(.35);

    Robot.driveT.resetSensors();
    //checks if we still have a gear
    driveInOne.run();
    waitUntilDone(5, driveInOne::done);
    Robot.gearHold.stopGearIntake();
    Timer.delay(.35);
    
    Robot.driveT.resetSensors();
    //checks if we still have a gear
    driveInTwo.run();
    waitUntilDone(5, driveInTwo::done);

  }

  @Override
  public void end() {
    Robot.driveT.setStop();
    Robot.feeder.stopFeeder();
  }


}
