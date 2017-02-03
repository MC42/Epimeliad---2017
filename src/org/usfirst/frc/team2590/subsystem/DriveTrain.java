package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.navigation.DriveAtAngleController;
import org.usfirst.frc.team2590.navigation.DriveStraightController;
import org.usfirst.frc.team2590.navigation.NavigationalSystem;
import org.usfirst.frc.team2590.navigation.Path;
import org.usfirst.frc.team2590.navigation.PurePursuitController;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import util.DualSignal;
import util.NemesisDrive;

public class DriveTrain implements RobotMap {

  private enum driveStates {
    STOP , TELEOP , AUTO
  };
  private driveStates drives = driveStates.STOP;

  //joysticks
  private Joystick left;
  private Joystick right;

  private static final double WHEEL_DIAM = 4;

  //motors
  private Victor leftVictor;
  private Victor rightVictor;
  private DualSignal driveSignal;

  //sensors
  private ADXRS450_Gyro gyro;
  private Encoder leftEncoder;
  private Encoder rightEncoder;

  //control systems
  private NemesisDrive empimiliad;
  private NavigationalSystem NASA;
  private PurePursuitController purell;
  private DriveAtAngleController bentDrive;
  private DriveStraightController straightVelocity;

  public DriveTrain(Joystick leftJ , Joystick rightJ) {

    //joysticks
    left = leftJ;
    right = rightJ;

    //motors
    driveSignal = DualSignal.DEAD;
    leftVictor = new Victor(LEFTMOTORPWM);
    rightVictor = new Victor(RIGHTMOTORPWM);

    //sensors
    gyro = new ADXRS450_Gyro();
    leftEncoder = new Encoder(LEFTENCODERA , LEFTENCODERB);
    rightEncoder = new Encoder(RIGHTENCODERA , RIGHTENCODERB);

    leftEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / 12));
    rightEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / 12));

    //control systems
    straightVelocity = new DriveStraightController(MAXACC, VELFF);
    NASA = new NavigationalSystem(leftEncoder, rightEncoder , gyro);
    bentDrive = new DriveAtAngleController(MAXACC, VELFF, DRIVETURNCOMP);
    purell = new PurePursuitController(PUREKV , MAXACC,  LOOKAHEAD, DRIVEBASE);
    empimiliad = new NemesisDrive(gyro,  leftVictor, rightVictor, DRIVETURNCOMP);

  }

  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {

    }

    @Override
    public void loop() {

      switch (drives) {
        case STOP:
          break;

        case TELEOP:
          empimiliad.correctiveDrive(left.getY(), right.getX(), 0 , 0.1);
          break;

        case AUTO :
          empimiliad.tankDrive( driveSignal.getSignals()[0],
                                driveSignal.getSignals()[1] );

          break;

      }
    }

    @Override
    public void onEnd() {

    }

  };

  public Loop getDriveLoop() {
    return loop_;
  }

  /**
   * Sets the drivetrain to corrective human control
   */
  public void setTeleop() {
    drives = driveStates.TELEOP;
  }

  /**
   * Stops the drivetrain
   */
  public void setStop() {
    drives = driveStates.STOP;
  }


  public void setDriveSetpoint(double setpoint) {
    drives = driveStates.AUTO;
    straightVelocity.setSetpoint(setpoint);
    driveSignal.updateSignal(straightVelocity.calculate(leftEncoder.getDistance()) ,
                              straightVelocity.calculate(rightEncoder.getDistance()));

  }


  /**
   * Changes the path the purell follows
   * @param newPath : new path to follow
   */
  public void changePath(Path newPath) {
    purell.setPath(newPath);
  }

  /**
   * Follows the path using the purell controller
   */
  public void followPath() {
    drives = driveStates.AUTO;

    driveSignal.updateSignal(purell.Calculate(NASA.getCurrentPoint(), false) ,
                              purell.Calculate(NASA.getCurrentPoint(), true));
  }

  public boolean driveStDone() {
    return straightVelocity.isDone();
  }

  public boolean angleDriveDone() {
    return bentDrive.isDone();
  }

  public void driveAtAngle(double driveSet ,double angleSet) {
    drives = driveStates.AUTO;
    bentDrive.setSetpoint(driveSet , angleSet);
    driveSignal.updateSignal(bentDrive.calculate(leftEncoder.getDistance() , gyro.getAngle() , false) ,
                              bentDrive.calculate(rightEncoder.getDistance() , gyro.getAngle() , true));
  }


}
