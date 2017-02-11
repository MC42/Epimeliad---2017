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
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.DualSignal;
import util.NemesisDrive;
import util.SmartJoystick;

public class DriveTrain implements RobotMap {

  private static DriveTrain driveTrain = null;
  public static DriveTrain getDriveInstance(SmartJoystick leftJoy, SmartJoystick rightJoy) {
    if(driveTrain == null) {
      driveTrain = new DriveTrain(leftJoy, rightJoy);
    }
    return driveTrain;
  }
  
  private enum driveStates {
    STOP , TELEOP , AUTO
  };
  private driveStates drives = driveStates.STOP;

  //joysticks
  private SmartJoystick left;
  private SmartJoystick right;
  
  private static final double WHEEL_DIAM = 4;

  //motors
  private Victor leftVictor;
  private Victor rightVictor;
  private DualSignal driveSignal;

  //sensors
  private ADXRS450_Gyro gyro;
  private Encoder leftEncoder;
  private Encoder rightEncoder;

  //drive shifting solenoid
  private Solenoid driveShifters;
  
  //control systems
  private NemesisDrive empimiliad;
  private NavigationalSystem NASA;
  private PurePursuitController purell;
  private DriveAtAngleController bentDrive;
  private DriveStraightController straightVelocity;

  public DriveTrain(SmartJoystick leftJ , SmartJoystick rightJ) {

    //joysticks
    left = leftJ;
    right = rightJ;

    //motors
    driveSignal = DualSignal.DEAD;
    leftVictor = new Victor(LEFTMOTORPWM);
    rightVictor = new Victor(RIGHTMOTORPWM);
    rightVictor.setInverted(true);

    //sensors
    gyro = new ADXRS450_Gyro();
    leftEncoder = new Encoder(LEFTENCODERA , LEFTENCODERB);
    rightEncoder = new Encoder(RIGHTENCODERA , RIGHTENCODERB);

    leftEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / 12));
    rightEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / 12));

    //control systems
    driveShifters = new Solenoid(1);
    straightVelocity = new DriveStraightController(MAXACC, VELFF);
    NASA = new NavigationalSystem(leftEncoder, rightEncoder , gyro);
    bentDrive = new DriveAtAngleController(MAXACC, VELFF, DRIVETURNCOMP);
    purell = new PurePursuitController(PUREKV , MAXACC,  LOOKAHEAD, DRIVEBASE);
    empimiliad = new NemesisDrive( leftVictor, rightVictor , leftEncoder , rightEncoder , 0.05 , WHEEL_DIAM);

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
         // empimiliad.tankDrive(driveSignal.getSignals()[0], driveSignal.getSignals()[1]);
          empimiliad.setVelocitySetpoints(left.getYVal()*10, left.getYVal()*10);
          break;
        case AUTO :
        /*  driveSignal.updateSignal(purell.Calculate(NASA.getCurrentPoint(), false) ,
              purell.Calculate(NASA.getCurrentPoint(), true));
          empimiliad.tankDrive(driveSignal.getSignals()[0], driveSignal.getSignals()[1]);
          */
          break;
      }
      //System.out.println("gyro " + gyro.getAngle());
      SmartDashboard.putNumber("left encoder", leftEncoder.getDistance());
      SmartDashboard.putNumber("right encoder", rightEncoder.getDistance());
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
    //driveSignal.updateSignal(0,0);    
    drives = driveStates.TELEOP;
    //System.out.println("now in teleop");
  }

  /**
   * Stops the drivetrain
   */
  public void setStop() {
    drives = driveStates.STOP;
  }


  public void setDriveSetpoint(double setpoint) {
    //change the systems state
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

  public void resetAllSensors() {
    leftEncoder.reset();
    rightEncoder.reset();
  }

  public void shift() {
    driveShifters.set(!driveShifters.get());
  }
  
  public void shift(boolean high) {
    driveShifters.set(high);

  }
}
