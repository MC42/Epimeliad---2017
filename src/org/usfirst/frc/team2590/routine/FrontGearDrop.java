package org.usfirst.frc.team2590.routine;

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


  //drive straight
  private DriveAtAngle driveOut;
  private DriveAtAngle driveBackIn;
  private DriveAtAngle driveBackOut;
  private DriveAtAngle driveToDropGear;

  public FrontGearDrop(boolean side) {

    //drive straight
    driveOut = new DriveAtAngle(4, 0);
    driveBackIn = new DriveAtAngle(-4, 4);
    driveBackOut = new DriveAtAngle(4, 0);
    driveToDropGear = new DriveAtAngle(-7.833, 0);
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
    
    Timer.delay(.5);
    Robot.gearHold.stopGearIntake(); //raise the dustpan
    waitUntilDone(1, driveOut::done);
    
    //if we still have a gear
    if(Robot.gearHold.hasGear()) {
      
      Robot.driveT.resetSensors();
      
      //drives back in to try again
      driveBackIn.run();
      waitUntilDone(2, driveOut::done);
      
      //puts the gear down
      Robot.gearHold.outTakeGear();
      Timer.delay(1);
      
      //drive away
      driveBackOut.run();
      waitUntilDone(1, driveBackOut::done);
      Robot.gearHold.stopGearIntake();
      Timer.delay(1); //waits for average to update

      if(Robot.gearHold.hasGear()) {
        
        //drives in and trys again
        Robot.driveT.resetSensors();
        driveBackIn.run();
        waitUntilDone(2, driveBackIn::done);

        //drops the dustpan
        Robot.gearHold.outTakeGear();
        driveBackOut.run();
        Timer.delay(1); //waits for average to update
        
        //raises the dustpan
        Robot.gearHold.stopGearIntake();
     
      }
    }
    
  }

  @Override
  public void end() {
    Robot.driveT.setStop();
    Robot.feeder.stopFeeder();
  }


}
