package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.navigation.DriveAtAngleController;
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
    STOP , VELOCITY_CONTROL , OPEN_LOOP , BENT_DRIVE , PATH_FOLLOWING
  };
  private driveStates drives = driveStates.STOP;

  //joysticks
  private Joystick left;
  private Joystick right;
  private boolean flipped;
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

  public DriveTrain(Joystick leftJ , Joystick rightJ) {

    //joysticks
    left = leftJ;
    right = rightJ;

    //motors
    flipped = false;
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
    empimiliad = new NemesisDrive(gyro,  leftVictor, rightVictor);
    NASA = new NavigationalSystem(leftEncoder, rightEncoder , gyro);
    purell = new PurePursuitController(PUREKV , MAXACC,  LOOKAHEAD);
    bentDrive = new DriveAtAngleController(MAXACC, VELFF, DRIVETURNCOMP);

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
        case VELOCITY_CONTROL:
          //main drive
          empimiliad.velocityDrive(left.getY(), right.getX());
          break;
        case OPEN_LOOP :
          //if a sensor breaks
          empimiliad.openLoopDrive(-left.getY(), right.getX());
          break;
        case PATH_FOLLOWING :
          //update signals for path following
          driveSignal.updateSignal(purell.Calculate(NASA.getCurrentPoint(), !flipped) ,
                                   purell.Calculate(NASA.getCurrentPoint(), flipped)); 
          break;
        case BENT_DRIVE :
          //update signals for drive at an angle
          driveSignal.updateSignal(bentDrive.calculate(leftEncoder.getDistance() , gyro.getAngle() , false , false) ,
                                   bentDrive.calculate(rightEncoder.getDistance() , gyro.getAngle() , true , false));
          break;
      }
      
      if(drives == driveStates.PATH_FOLLOWING || drives == driveStates.BENT_DRIVE) {
        //send signals 
        empimiliad.tankDrive( driveSignal.getSignals()[0],
                              driveSignal.getSignals()[1] );  
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
   * Stops the drivetrain
   */
  public void setStop() {
    drives = driveStates.STOP;
  }
  
  //teleop control
  
  /**
   * Starts teleop in velocity control mode
   */
  public void startTelop() {
    drives = driveStates.VELOCITY_CONTROL;
  }
  
  /**
   * Sets open loop control
   */
  public void setOpenLoop() {
    drives = driveStates.OPEN_LOOP;
  }

  //Path Following

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
    drives = driveStates.PATH_FOLLOWING;
  }

  public void flip() {
    purell.flip(true);
  }
  
  //angled drive 
  
  /**
   * Makes the drive controler drive at an angle
   * @param driveSet : setpoint to drive to
   * @param angleSet : angle to turn to
   */
  public void driveAtAngle(double driveSet ,double angleSet) {
    drives = driveStates.BENT_DRIVE;
    bentDrive.setSetpoint(driveSet , angleSet);
  }

  /**
   * angle drive check
   * @return if the angle drive is done
   */
  public boolean angleDriveDone() {
    return bentDrive.isDone();
  }
  
}
