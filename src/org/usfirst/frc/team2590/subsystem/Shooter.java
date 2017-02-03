package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class Shooter implements RobotMap{

  private enum shooterStates {
    STOP , ACCELERATING , SHOOT_NOW
  };
  private shooterStates shooter = shooterStates.STOP;
  
  private double setpoint;

  //motors
  private CANTalon shooterSlave;
  private CANTalon shooterMaster;

  
  public Shooter() {
    setpoint = 0;
    
    //master shooter motor
    shooterMaster = new CANTalon(SHOOTERMASTERID);
    shooterMaster.changeControlMode(TalonControlMode.Speed);
    shooterMaster.setPID(SHOOTERKP, SHOOTERKI, SHOOTERKD, SHOOTERKF, 0, 0.0, 0);
    shooterMaster.enableBrakeMode(false);
    shooterMaster.clearStickyFaults();
    
    
    //slave shooter motor
    shooterSlave  = new CANTalon( SHOOTERSLAVEID);
    shooterSlave.changeControlMode(TalonControlMode.Follower);
    shooterSlave.set(shooterMaster.getDeviceID());
    shooterSlave.enableBrakeMode(false);
    shooterSlave.clearStickyFaults();

  }
  
  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop() {
      switch(shooter) {
        case STOP :
          shooterMaster.changeControlMode(TalonControlMode.Voltage);
          shooterMaster.set(0);
          break;
        case ACCELERATING :
          shooterMaster.set(setpoint);
          break;
        case SHOOT_NOW :
          shooterMaster.set(setpoint);
          break;
      }
    }

    @Override
    public void onEnd() {
    }
    
  };
  
  public Loop getShootLoop() {
    return loop_;
  }
  
  public void stopShooter() {
    shooter = shooterStates.STOP;
  }
  
  public void setSetpoint(double setpoint , boolean isControl) {
    this.setpoint = setpoint;
    shooterMaster.changeControlMode(isControl ? TalonControlMode.Speed : 
                                                TalonControlMode.PercentVbus );
      
  }
}
