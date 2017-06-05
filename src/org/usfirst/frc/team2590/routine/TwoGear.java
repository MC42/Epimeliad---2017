package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * Places a gear on the front peg
 * picks up a partners gear and places that on the peg
 * @author Connor_Hofenbitzer
 *
 */
public class TwoGear extends AutoRoutine{

  //drop off the first gear
  FrontGearDrop front;
  
  //drop off first gear
  private DriveAtAngle driveBackOut;
  
  //get second gear
  private DriveAtAngle getToTheGear;
  private TurnToAngle turnToRobot;
  
  //drop off second gear
  private DriveAtAngle driveBackToGear;
  private TurnToAngle turnBackToGear;
  private DriveAtAngle driveBackIntoGear;
  

  public TwoGear() {
    
    //drop off the first gear
    front = new FrontGearDrop();
    
    //aquiring second gear
    turnToRobot = new TurnToAngle(125); //turn towards the gear
    getToTheGear = new DriveAtAngle(-6, 0); //drives towards the gear
    
    //dropping off second gear
    turnBackToGear = new TurnToAngle(-117); //turns towards the peg
    driveBackToGear = new DriveAtAngle(3.5, 0); //drives to the turning point
    driveBackIntoGear = new DriveAtAngle(-4.5, 0); //drives to the peg after the turn
    driveBackOut = new DriveAtAngle(6.5, 0); //drives out after dropping off the gear
    
  }
  @Override
  public void run() {
    front.run();
    Robot.driveT.resetSensors();

    if(!Robot.gearHold.hasGear()) {
      
      //turn towards the other robot
      turnToRobot.run();
      Robot.gearHold.intakeGear();
      waitUntilDone(1.5 , turnToRobot::done);
      Robot.driveT.resetSensors();
    
    
      //picks up the second gear
      Robot.driveT.shiftHigh();
      getToTheGear.run();
      waitUntilDone(2, getToTheGear::done);

      //drives back to perpendicular to the peg
      Robot.driveT.resetSensors();
      driveBackToGear.run();
      Robot.gearHold.stopGearIntake();
      waitUntilDone(2, driveBackToGear::done);
    
      //turn to look at the peg
      turnBackToGear.run();
      waitUntilDone(1.5, turnBackToGear::done);

      //drops off the gear
      driveBackIntoGear.run();
      waitUntilDone(1.5, driveBackIntoGear::done);
      Robot.gearHold.outTakeGear();
      Timer.delay(.5);
      driveBackOut.run();
    }
  }

  @Override
  public void end() {
  }

}
