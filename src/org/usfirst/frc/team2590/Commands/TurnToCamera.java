package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import util.Vision;

public class TurnToCamera extends NemesisCommand {

  @Override
  public void run() {
    Robot.driveT.shiftLow();
    System.out.println("turn " + Robot.vision.angleToTarget());
    Robot.driveT.turnToAngle(Robot.vision.angleToTarget());
  }

  @Override
  public boolean done() {
    return false;
  }

}
