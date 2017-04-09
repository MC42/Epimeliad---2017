package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class TurnToAngle extends NemesisCommand {

  private double setpoint;
  
  public TurnToAngle(double stp) {
    this.setpoint = stp;
  }

  public void changeAngle(double angle) {
    this.setpoint = angle;
  }
  
  @Override
  public void run() {
      Robot.driveT.shiftLow();
      Robot.driveT.turnToAngle(setpoint);
  }

  @Override
  public boolean done() {
    return Robot.driveT.getTurnDone(); 
  }

}
