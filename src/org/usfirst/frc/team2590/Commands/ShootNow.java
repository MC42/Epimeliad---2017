package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class ShootNow extends NemesisCommand {


  @Override
  public void run() {
    Robot.intake.agitate();
    Robot.shooter.shootNow();
  }

  @Override
  public boolean done() {
    return true;
  }

}
