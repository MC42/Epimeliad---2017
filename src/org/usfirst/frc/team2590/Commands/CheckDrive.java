package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class CheckDrive extends NemesisCommand{

  private boolean done;
  private DriveAtAngle driveIn;
  private DriveAtAngle driveBack;
  
  public CheckDrive(boolean first) {
    done = false;
    driveBack = new DriveAtAngle(4.5, 0);
    driveIn = new DriveAtAngle(-4.5, (first?4:-4));
  }

  @Override
  public void run() {
    if(Robot.gearHold.hasGear()) {
      
      //reset and drive into the peg
      Robot.driveT.resetSensors();
      driveIn.run();
      Timer.delay(2);
      
      //drop the gear
      Robot.gearHold.outTakeGear();
      Timer.delay(.5);
      
      //drive away
      driveBack.run();
      Timer.delay(.5);
      
      //lift the gearTake
      Robot.gearHold.stopGearIntake();
      done = true;
    } else {
      done = true;
    }
    
  }

  @Override
  public boolean done() {
    return done;
  }
  

  
}
