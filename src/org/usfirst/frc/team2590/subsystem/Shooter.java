package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.FeedbackDeviceStatus;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;

public class Shooter implements RobotMap{

  private static Shooter shot = null;
  public static Shooter getShooterInstance() {
    if(shot == null) {
      shot = new Shooter();
    }
    return shot;
  }
  
  private enum shooterStates {
    STOP , ACCELERATING , SHOOT_NOW , SHOOT_WHEN_READY
  };
  private shooterStates shooter = shooterStates.STOP;
  private static final double TOLERANCE = 10;
  
  private double setpoint;
  private int cyclesOnTarget;
  
  private CANTalon shooterSlave;
  private CANTalon shooterMaster;
  
  public Shooter() {
    //doubles
    setpoint = 0;
    cyclesOnTarget = 0;
    
    //master shooter motor
    shooterMaster = new CANTalon(SHOOTERMASTERID);
    shooterMaster.changeControlMode(TalonControlMode.Speed);
    shooterMaster.setPID(SHOOTERKP, SHOOTERKI, SHOOTERKD, SHOOTERKF, 0, 0.0, 0);
    shooterMaster.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
    shooterMaster.enableBrakeMode(false);
    shooterMaster.clearStickyFaults();
    shooterMaster.enable();
    
    //slave shooter motor
    shooterSlave  = new CANTalon( SHOOTERSLAVEID);
    shooterSlave.changeControlMode(TalonControlMode.Follower);
    shooterSlave.set(shooterMaster.getDeviceID());
    shooterSlave.enableBrakeMode(false);
    shooterSlave.clearStickyFaults();
    
    //checks if the shooter encoder is plugged in
    if (shooterMaster.isSensorPresent(FeedbackDevice.CtreMagEncoder_Relative) != 
                                      FeedbackDeviceStatus.FeedbackStatusPresent) {
      DriverStation.reportError("Could not detect shooter encoder!", false);
    }

  }
  
  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop() {
      switch(shooter) {
        case STOP :
          shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
          shooterMaster.set(0);
          break;
        case ACCELERATING :
          shooterMaster.set(setpoint);
          break;
        case SHOOT_WHEN_READY :
          shooterMaster.set(setpoint);
          
          if(Math.abs(shooterMaster.getSpeed() - setpoint) < TOLERANCE) { 
            cyclesOnTarget += 1;
            if(cyclesOnTarget > MINCYCLESONTARGET) {
              shooter = shooterStates.SHOOT_NOW;
            }
          } else {
            cyclesOnTarget = 0;
          }
          
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
  
  public void takeTheShot() {
    shooter = shooterStates.SHOOT_NOW;
  }
  
  public void setSetpoint(double setpoint , boolean isControl) {
    this.setpoint = setpoint;
    shooterMaster.changeControlMode(isControl ? TalonControlMode.Speed : 
                                                TalonControlMode.PercentVbus );
      
  }
}
