package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.CheckDrive;
import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.DriveToGear;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class TwoGear extends AutoRoutine{

  //drop off first gear
  private CheckDrive check;
  private DriveAtAngle driveToDrop;
  private DriveAtAngle driveBackOut;
  
  //get second gear
  private DriveToGear getTheGear;
  private TurnToAngle TurnToRobot;
  
  //drop off second gear
  private DriveAtAngle DriveBackToGear;
  private TurnToAngle TurnBackToGear;
  private DriveAtAngle DriveBackIntoGear;
  

  public TwoGear() {
    
    //dropping off first gear
    check = new CheckDrive(true);
    driveBackOut = new DriveAtAngle(6, 0);
    driveToDrop = new DriveAtAngle(-6.66, 0);

    //aquiring second gear
    TurnToRobot = new TurnToAngle(90);
    getTheGear = new DriveToGear(0.5, 3, 10);
    
    //dropping off second gear
    TurnBackToGear = new TurnToAngle(-90);
    DriveBackToGear = new DriveAtAngle(5, 0);
    DriveBackIntoGear = new DriveAtAngle(-6, 0);
  }
  @Override
  public void run() {
    
    //drives forward to drop the first gear
    driveToDrop.run();
    waitUntilDone(2 , driveToDrop::done);
    Robot.gearHold.outTakeGear();
    Timer.delay(.5);
    
    //drives out 
    driveBackOut.run();
    waitUntilDone(1.5 , driveBackOut::done);
    
    //check if we still have a gear
    check.run();
    waitUntilDone(2, check::done);
    
    //turn towards the other robot
    TurnToRobot.run();
    waitUntilDone(1.5 , TurnToRobot::done);
    Robot.driveT.resetSensors();
    
    //picks up the second gear
    getTheGear.run();
    waitUntilDone(3, getTheGear::done);
    
    //drives back to start point
    DriveBackToGear.changeConstants(-Robot.driveT.getLeftEncoder().getDistance(), 
                                   -Robot.driveT.getGyroHeading());
    
    //drives back to perpendicular to the peg
    DriveBackToGear.run();
    Robot.gearHold.stopGearIntake();
    waitUntilDone(1.5, DriveBackToGear::done);
    
    //turn to look at the peg
    TurnBackToGear.run();
    waitUntilDone(1.5, TurnBackToGear::done);
    
    //drops off the gear
    DriveBackIntoGear.run();
    waitUntilDone(1.5, DriveBackIntoGear::done);
    Robot.gearHold.outTakeGear();
  }

  @Override
  public void end() {
  }

}
