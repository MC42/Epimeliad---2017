package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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
    STOP , ACCELERATING , SHOOT_NOW , PULLEY_IN , PULLEY_OUT
  };
  private shooterStates shooter = shooterStates.STOP;

  private static final double TOLERANCE = 50;
  
  //setpoint
  private double setpoint;
   private PowerDistributionPanel pdb;
   
  //motors
  private Victor pullyMotor;
  private CANTalon shooterSlave;
  private CANTalon shooterMaster;
  
  public Shooter() {
    //desired speed of the shooter (RPM)
    setpoint = 3000;
    pullyMotor = new Victor(PULLEYMOTORPWM);

    //master shooter motor
    shooterMaster = new CANTalon(SHOOTERMASTERID);
    shooterMaster.changeControlMode(TalonControlMode.Speed);
    shooterMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    shooterMaster.setPID(SHOOTERKP, SHOOTERKI, SHOOTERKD, SHOOTERKF, 0, 0.0, 0);
    shooterMaster.enableBrakeMode(false); //motor can move
    shooterMaster.clearStickyFaults();
    shooterMaster.reverseSensor(true);
    shooterMaster.setInverted(true);
    
    //slave shooter motor
    shooterSlave = new CANTalon(SHOOTERSLAVEID);
    shooterSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
    shooterSlave.set(SHOOTERMASTERID);
    shooterSlave.enableBrakeMode(false);
    shooterSlave.clearStickyFaults();
    shooterSlave.setInverted(true);
    pdb = new PowerDistributionPanel();
    
  }
  
  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop() {
      switch(shooter) {
        //full shooter loop
        case STOP :
          //final death switch
         
          shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
          shooterMaster.set(0);
          pullyMotor.set(0);
          break;
        case ACCELERATING :
          //only runs shooter motor
          shooterMaster.set(setpoint);         
          break;
        case SHOOT_NOW :
          //feeds the balls instantly (runs shooter motor and conveyer)
          shooterMaster.changeControlMode(TalonControlMode.Speed);          
          shooterMaster.set(setpoint);
          handlePully(true);
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
      
      //System.out.println("pdb " + pdb.getCurrent(10) + " " + pdb.getCurrent(11));

      SmartDashboard.putNumber("shooter enc", shooterMaster.getSpeed());
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
        
  }
  
  public void shootNow() {
    shooter = shooterStates.SHOOT_NOW;
  }
  
  public void onlyPulley() {
    shooter = shooterStates.PULLEY_IN;
  }
  
  public void reversePully() {
    shooter = shooterStates.PULLEY_OUT;
  }
  
  public void handlePully(boolean automatic) {
    boolean onTarget;
    if(automatic) {
      onTarget = Math.abs(setpoint - (shooterMaster.getSpeed())) < TOLERANCE;
      pullyMotor.set(onTarget?0.5:0);
    }
  }
  
 }
