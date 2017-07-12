package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

/**
 * Turns to a given angle
 * @author Connor_Hofenbitzer
 *
 */
public class TurnToAngle extends NemesisCommand {

  private double setpoint;
  
  /**
   * Turns to an angle in low gear
   * @param stp : angle to turn to
   */
  public TurnToAngle(double stp) {
    this.setpoint = stp;
  }

  /**
   * Changes the angle to turn to
   * @param angle : angle to turn to
   */
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
