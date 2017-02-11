package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;

import edu.wpi.first.wpilibj.Victor;

public class ShitShooter {

  private Victor shooterVictor;
  private double setPoint;
  
  public ShitShooter() {
    setPoint = 0;
    shooterVictor = new Victor(1);
  }
  
   private Loop _loop = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop() {
      System.out.println("running");
      shooterVictor.set(setPoint);
    }

    @Override
    public void onEnd() {
    }
    
  };
  
  public Loop getShootLoop() {
    return _loop;
  }
  public void setSetpoint(double setpoint) {
    this.setPoint = setpoint;
  }
}
