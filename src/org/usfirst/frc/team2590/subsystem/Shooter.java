package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;
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
    STOP , ACCELERATING , SHOOT_NOW , SHOOT_WHEN_READY , PULLEY_IN , PULLEY_OUT
  };
  private shooterStates shooter = shooterStates.STOP;
  
  //setpoint
  private double setpoint;
   
  //motors
  private Victor pullyMotor;
  private CANTalon shooterSlave;
  private CANTalon shooterMaster;
  
  public Shooter() {
    
    //desired speed of the shooter (RPM)
    setpoint = 6600;
    
    //motor from hopper to shooter
    pullyMotor = new Victor(PULLEYMOTORPWM);

    //master shooter motor
    shooterMaster = new CANTalon(SHOOTERMASTERID);
    shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
    shooterMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    shooterMaster.setPID(SHOOTERKP, SHOOTERKI, SHOOTERKD , SHOOTERKF, 0, 0, 0);
    shooterMaster.setCloseLoopRampRate(0.0);
    shooterMaster.setIZone(0);
    shooterMaster.enableBrakeMode(false); //motor can move
    
    //if on the real robot uncomment this
    /* shooterMaster.reverseOutput(true);
    shooterMaster.reverseSensor(true); */

    
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
    public void loop() {
      switch(shooter) {
        
        //selfex
        case STOP :
          shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
          shooterMaster.set(0);
          pullyMotor.set(0);
          break;
          
        //only runs shooter motor
        case ACCELERATING :
          shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
          shooterMaster.set(setpoint); 
          break;
        
        //feeds the balls instantly (runs shooter motor and conveyer)
        case SHOOT_NOW :
          shooterMaster.changeControlMode(TalonControlMode.Speed);
          shooterMaster.set(setpoint); 
          pullyMotor.set(0.75);
          break;
          
        //runs pulley when we get to speed
        case SHOOT_WHEN_READY :
          shooterMaster.changeControlMode(TalonControlMode.Speed);
          shooterMaster.set(setpoint);
          handlePully();
          break;
          
        //just running the pulley
        case PULLEY_IN :
          pullyMotor.set(1);
          break;
        case PULLEY_OUT :
          pullyMotor.set(-1);
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
  
  public void shootNow() {
    shooter = shooterStates.SHOOT_NOW;
  }
  
  public void shootWhenReady() {
    shooter = shooterStates.SHOOT_WHEN_READY;
  }
  
  public void revShooter() {
    shooter = shooterStates.ACCELERATING;
  }
  
  public void onlyPulley() {
    shooter = shooterStates.PULLEY_IN;
  }
  
  public void reversePully() {
    shooter = shooterStates.PULLEY_OUT;
  }
  
  public void handlePully() {
    pullyMotor.set( (shooterMaster.getSpeed() > setpoint )? 1 : 0);
  }
  
 }
