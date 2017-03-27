package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class CheckDrive extends NemesisCommand {

  private boolean done;
  private DriveAtAngle driveIn;
  private DriveAtAngle driveBack;
  
  public CheckDrive(boolean first) {
    done = false;
    driveBack = new DriveAtAngle(6, 0);
    driveIn = new DriveAtAngle(-6, (first?4:-4));
  }

  @Override
  public void run() {
    if(Robot.gearHold.hasGear()) {
      
      //drive into the gear
      Robot.driveT.resetSensors();
      driveIn.run();
      waitUntilDone(2, driveIn::done);
      
      //drop the gear
      Robot.gearHold.outTakeGear();
      Timer.delay(0.75);

      //drive back out
      driveBack.run();
      waitUntilDone(1, driveBack::done);
      
      //raise the dustpan
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
