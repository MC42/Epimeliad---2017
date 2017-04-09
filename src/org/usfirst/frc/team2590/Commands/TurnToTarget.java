package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class TurnToTarget extends NemesisCommand {

  private boolean forceDone;
  private TurnToAngle turnToTarget;

  public TurnToTarget() {
    forceDone = false;
    turnToTarget = new TurnToAngle(0);
  }
  
  @Override
  public void run() {
    if(Robot.vision.targetFound() > 0) {
      turnToTarget.changeAngle(Robot.vision.angleToTarget());
      turnToTarget.run();
    } else {
      forceDone = true;
    }
  }

  @Override
  public boolean done() {
    return turnToTarget.done() || forceDone;
  }

  
}
