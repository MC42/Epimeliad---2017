package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter implements RobotMap {

  private static Shooter shoot = null;
  public static Shooter getShooter() {
    if(shoot == null) {
      shoot = new Shooter();
    }
    return shoot;
  }
  
  private enum shooterStates {
    STOP , ACCELERATING
  };
  private shooterStates shooter = shooterStates.STOP;
  
  //setpoint
  private double setpoint;
   
  //motors
  private CANTalon shooterSlave;
  private CANTalon shooterMaster;
  
  public Shooter() {
    
    //desired speed of the shooter (RPM)
    setpoint = 6600;
   
    //master shooter motor
    shooterMaster = new CANTalon(SHOOTERMASTERID);
    shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
    shooterMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    shooterMaster.setPID(SHOOTERKP, SHOOTERKI, SHOOTERKD , SHOOTERKF, 0, 0, 0);
    shooterMaster.setCloseLoopRampRate(0.0);
    shooterMaster.configPeakOutputVoltage(0.0, -12.0);
    shooterMaster.setIZone(0);
    shooterMaster.enableBrakeMode(false); //motor can move
    
    //if on the real robot uncomment this
    shooterMaster.reverseOutput(true);
    shooterMaster.reverseSensor(true); 

    
    //slave shooter motor
    shooterSlave = new CANTalon(SHOOTERSLAVEID);
    shooterSlave.changeControlMode(TalonControlMode.Follower);
    shooterSlave.set(shooterMaster.getDeviceID());
    shooterSlave.enableBrakeMode(false);
    shooterSlave.clearStickyFaults(); 
    shooterMaster.clearStickyFaults();
    
  }
  
  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop(double delta) {
      switch(shooter) {
        case STOP :
          shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
          shooterMaster.set(0);
          break;
          
        //only runs shooter motor
        case ACCELERATING :
          shooterMaster.changeControlMode(TalonControlMode.Speed);
          shooterMaster.set(setpoint); 
          break;
    
        default :          
          DriverStation.reportWarning("Hit default case in shooter", false);
          break;      
      }
      SmartDashboard.putNumber("enc ", shooterMaster.getSpeed());
    }

    @Override
    public void onEnd() {
    }
    
  };
  
  public Loop getShootLoop() {
    return loop_;
  }
  
 
  
  public void setSetpoint(double setpoint) {
    this.setpoint = setpoint;
  }
  
  public void stopShooter() {
    shooter = shooterStates.STOP;
  }

  public void revShooter() {
    shooter = shooterStates.ACCELERATING;
  }
  
  public double getSetpoint() {
    return setpoint;
  }
  
  public boolean getOnTarget() {
    return Math.abs(setpoint - shooterMaster.getSpeed()) < 100;
  }
  
  public boolean getAboveTarget() {
    return shooterMaster.getSpeed() > setpoint-50;
  }
  
  public double getSpeed() {
    return shooterMaster.getSpeed();
  }
 }
