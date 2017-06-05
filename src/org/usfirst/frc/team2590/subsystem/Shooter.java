package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The ball shooter subsystem for Eris
 * This uses two Talon SRX control loops to get the
 * shooter wheels to a desired RPM
 * @author Connor_Hofenbitzer
 *
 */
public class Shooter implements RobotMap {

  //singleton
  private static Shooter shooterInstance = null;
  public static Shooter getShooter() {
    if (shooterInstance == null) {
      shooterInstance = new Shooter();
    }
    return shooterInstance;
  }

  //shooter states
  private shooterStates shooter = shooterStates.STOP;
  private enum shooterStates {
    STOP, ACCELERATING, SHOOT_READY, LOCKED_SHOT
  };

  // setpoints
  private double setpoint;
  private boolean interpolate;
  private boolean lockingShot;

  // motors
  private CANTalon shooterSlave;
  private CANTalon shooterMaster;

  public Shooter() {

    // desired speed of the shooter (RPM)
    setpoint = 0;
    interpolate = true;
    lockingShot = false;

    // master shooter motor
    shooterMaster = new CANTalon(SHOOTERMASTERID);
    shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
    shooterMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);

    // settings
    shooterMaster.setIZone(0);
    shooterMaster.enableBrakeMode(false); // motor can coast
    shooterMaster.setCloseLoopRampRate(0.0);
    shooterMaster.setPID(SHOOTERKP, SHOOTERKI, SHOOTERKD, SHOOTERKF, 0, 0, 0);
    shooterMaster.configEncoderCodesPerRev(360);

    shooterMaster.configPeakOutputVoltage(0.0, -12.0);
    shooterMaster.reverseOutput(true);
    shooterMaster.reverseSensor(true);

    // slave shooter motor
    shooterSlave = new CANTalon(SHOOTERSLAVEID);
    shooterSlave.changeControlMode(TalonControlMode.Follower);
    shooterSlave.set(shooterMaster.getDeviceID());
    shooterSlave.enableBrakeMode(false);

    // clear sticky faults
    shooterSlave.clearStickyFaults();
    shooterMaster.clearStickyFaults();

  }

  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop(double delta) {

      switch (shooter) {
        // stops the shooter
        case STOP:
          shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
          shooterMaster.set(0);
          lockingShot = false;
          break;

        // only runs shooter motor
        case ACCELERATING:
          shooterMaster.changeControlMode(TalonControlMode.Speed);
          shooterMaster.set(setpoint);
          break;

        // only shoots when the shooters up to speed
        case SHOOT_READY:
          shooterMaster.changeControlMode(TalonControlMode.Speed);
          shooterMaster.set(setpoint);
          if (getAboveTarget()) {
            Robot.feeder.feedIntoShooter();
          }
          break;

        // shoots when ready and doesnt stop shooting until commanded to stop
        case LOCKED_SHOT:
          shooterMaster.changeControlMode(TalonControlMode.Speed);
          shooterMaster.set(setpoint);
          if (getAboveTarget()) {
            lockingShot = true;
          }
          if (lockingShot) {
            Robot.feeder.feedIntoShooter();
          }
          break;

        default:
          DriverStation.reportWarning("Hit default case in shooter", false);
          break;
      }

      if (shooter == shooterStates.STOP) {
        Robot.ledController.updateShooterState("OFF");
      } else {
        Robot.ledController.updateShooterState(getAboveTarget() ? "SHOOT" : "REVVING");
      }

      SmartDashboard.putNumber("Shooter encoder", shooterMaster.getSpeed());
    }

    @Override
    public void onEnd() {
    }

  };

  public Loop getShootLoop() {
    return loop_;
  }

  /**
   * allows the robot to use the interpolation equation to set the setpoint based off of distance
   * @param interpolation : if the robot may use interpolation, or a manual setpoint
   */
  public void setInterpolation(boolean interpolation) {
    interpolate = interpolation;
    SmartDashboard.putBoolean("interpolation", interpolate);
  }

  /**
   * Sets the setpoint of the shooter
   * @param setpoint
   */
  public void setSetpoint(double setpoint) {
    if (interpolate) {
      double dist = Robot.vision.vAngleToTarget();
      this.setpoint = (22.658 * dist) + 2350;
    } else {
      this.setpoint = setpoint;
    }
  }

  public boolean getInterpolation() {
    return interpolate;
  }

  /**
   * Stops the shooter
   */
  public void stopShooter() {
    shooter = shooterStates.STOP;
  }

  /**
   * Just runs the shooter , no feeder
   */
  public void revShooter() {
    shooter = shooterStates.ACCELERATING;
  }

  /**
   * Shoots when the shooter gets up to speed
   */
  public void shootWhenReady() {
    shooter = shooterStates.SHOOT_READY;
  }

  /**
   * Shoots when the shooter gets up to speed and doesn't stop until the shooter
   * is set to the stop state
   */
  public void lockingShot() {
    shooter = shooterStates.LOCKED_SHOT;
  }

  /**
   * Gets the setpoint of the shooter
   * 
   * @return : setpoint in rpm
   */
  public double getSetpoint() {
    return setpoint;
  }

  /**
   * Gets if the shooter is on target
   * 
   * @return on target , within 100 rpms
   */
  public boolean getOnTarget() {
    return Math.abs(setpoint - this.getSpeed()) < 100;
  }

  /**
   * gets if the shooter is above the setpoint
   * 
   * @return : is the shooter above target
   */
  public boolean getAboveTarget() {
    return this.getSpeed() > ((setpoint - 50) - 500); // -500 because control
                                                      // stabilizes 500 below,
  }

  /**
   * checks the shooter encoder value
   * @return : speed of the shooter
   */
  public double getSpeed() {
    try {
      return shooterMaster.getSpeed();
    } catch (Exception e) {
      shooter = shooterStates.STOP;
      DriverStation.reportWarning("Shooter encoder died, shutting down shooter", true);
      return 0.0;
    }
  }
}
