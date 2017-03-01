package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class AutomatedShootingSequence extends NemesisCommand{

  private double setpoint;
  private double running;
  private double startTime;
  
  public AutomatedShootingSequence(double setpoint , double runTime) {
    this.setpoint = setpoint;
    running = runTime;
  }

  @Override
  public void run() {
    Robot.shooter.setSetpoint(setpoint);
    Robot.shooter.revShooter();
    startTime = Timer.getFPGATimestamp();
       
    
    while(Timer.getFPGATimestamp() - startTime < running) {
      if(Robot.shooter.getSpeed()-100 > setpoint) {
        Robot.feeder.feedIntoShooter();
        Robot.intake.agitate();
      } else {
        Robot.intake.stopIntake();
        Robot.feeder.stopFeeder();
      }
    }
  }
  

  @Override
  public boolean done() {
    return false;
  }
  
  
}
