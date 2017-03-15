package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class DriveToGear extends NemesisCommand {

  private double speed;
  private double cycles;
  private double timeOut;
  private boolean done;
  private double currentCycles;
  
  public DriveToGear(double speed , double timeOut , double cycles) {
    this.done = false;
    this.speed = speed;
    this.cycles = cycles;
    this.timeOut = timeOut;
    this.currentCycles = 0;
  }

  @Override
  public void run() {
    
    Robot.gearHold.intakeGear();
    double start = Timer.getFPGATimestamp();
    while(Timer.getFPGATimestamp()-start < timeOut) {
      
      Robot.driveT.force(speed, speed);
      
      if(Robot.gearHold.hasGear()) {
        currentCycles += 1;
      } else {
        currentCycles = 0;
      }
      
      if(currentCycles >= cycles) {
        done = true;
        return;
      }
      
    }
    
    Robot.gearHold.stopGearIntake();
    Robot.driveT.setStop();
    done = true;
  }
  
  @Override
  public boolean done() {
    return done;
  }
  
  
  
}
