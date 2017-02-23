package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class TurnToAngle extends NemesisCommand{

  double setpoint;
  
  public TurnToAngle(double stp) {
    this.setpoint = stp;
  }
  
  @Override
  public void run() {
    Robot.driveT.shiftLow();
    Robot.driveT.turnToAngle(setpoint);
  }

  @Override
  public boolean done() {
    return false;
  }

}
