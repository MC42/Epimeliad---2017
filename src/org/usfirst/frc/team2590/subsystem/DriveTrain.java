package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.navigation.DriveAtAngleController;
import org.usfirst.frc.team2590.navigation.NavigationalSystem;
import org.usfirst.frc.team2590.navigation.Path;
import org.usfirst.frc.team2590.navigation.PurePursuitController;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.DualSignal;
import util.NemesisDrive;
import util.NemesisSolenoid;

public class DriveTrain implements RobotMap {

  private static DriveTrain drive = null;
  public static DriveTrain getDriveTrain(Joystick leftJ , Joystick rightJ) {
    if(drive == null) {
      drive = new DriveTrain(leftJ, rightJ);
    }
    return drive;
  }
  
  private enum driveStates {
    STOP , VELOCITY_CONTROL , OPEN_LOOP , ANGLED_DRIVE , PATH_FOLLOWING , TURN
  };
  private driveStates drives = driveStates.STOP;

  //joysticks
  private boolean done;
  private Joystick left;
  private Joystick right;
  private double turnStp;
  private static final double WHEEL_DIAM = 4;

  //motors
  private Victor leftVictor;
  private Victor rightVictor;
  private DualSignal driveSignal;

  //sensors
  private ADXRS450_Gyro gyro;
  private Encoder leftEncoder;
  private Encoder rightEncoder;
  private NemesisSolenoid shifters;
  //ball shifters on 0
  
  //control systems
  private NavigationalSystem NASA;
  private NemesisDrive straightDrive;
  private PID turn;
  private PurePursuitController purell;
  private DriveAtAngleController angledDriveCont;

  public DriveTrain(Joystick leftJ , Joystick rightJ) {

    //joysticks
    done = false;
    turnStp = 0;
    left = leftJ;
    right = rightJ;

    //motors
    driveSignal = DualSignal.DEAD;
    leftVictor = new Victor(LEFTMOTORPWM);
    rightVictor = new Victor(RIGHTMOTORPWM);

    //sensors
    gyro = new ADXRS450_Gyro();
    shifters = new NemesisSolenoid(SHIFTER_SOLENOID);
    leftEncoder = new Encoder(LEFTENCODERA , LEFTENCODERB);
    rightEncoder = new Encoder(RIGHTENCODERA , RIGHTENCODERB);
    
    //feet 
    leftEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / 12));
    rightEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / 12));

    //control systems
    NASA = new NavigationalSystem(leftEncoder, rightEncoder , gyro);
    purell = new PurePursuitController(PUREKV , MAXACC,  LOOKAHEAD);
    straightDrive = new NemesisDrive(gyro,  leftVictor, rightVictor);
    angledDriveCont = new DriveAtAngleController(3, VELFF, 0.075);
    turn = new PID(TURNKP, TURNKI, TURNKD, false, 1);
  }

  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {
      shifters.set(false);
    }

    @Override
    public void loop() {

      switch (drives) {
        case STOP:
          break;
        case VELOCITY_CONTROL:
          //teleop drive
          straightDrive.velocityDrive(left.getY(), right.getX());
          break;
        case OPEN_LOOP :
          //if a sensor breaks, fall back
          //pure driver control
          straightDrive.openLoopDrive(-left.getY(), right.getX());
          break;
        case PATH_FOLLOWING :
          //update signals for path following
          driveSignal.updateSignal(purell.Calculate(NASA.getCurrentPoint(), true) ,
                                   purell.Calculate(NASA.getCurrentPoint(), false)); 
          break;
        case ANGLED_DRIVE :
          //update signals for drive at an angle
          driveSignal.updateSignal(angledDriveCont.calculate(leftEncoder.getDistance() , gyro.getAngle() , false) ,
                                   angledDriveCont.calculate(rightEncoder.getDistance() , gyro.getAngle() , true));
          break;
        case TURN :
          double error = turnStp - gyro.getAngle();
          double kP = 0.2;
          
          if(Math.abs(error) > 1) {
          done = false;
          System.out.println(" gyro angle " + gyro.getAngle() + " setpoint " + turnStp);
          driveSignal.updateSignal(-error*kP, 
                                    error*kP);  
          straightDrive.tankDrive( driveSignal.getSignals()[0],
                                   driveSignal.getSignals()[1] );
          } else {
            System.out.println("done " + error);
            done = true;
          }
          
        default :
          driveSignal.updateSignal(0,0);
          DriverStation.reportWarning("Hit default case in drive train", false);
          break;
      }
      SmartDashboard.putNumber("encoder l " , leftEncoder.getDistance());     
      SmartDashboard.putNumber("encoder r " , rightEncoder.getDistance());

      //System.out.println("enc " + leftEncoder.getDistance() + " " + rightEncoder.getDistance());
      if(drives == driveStates.PATH_FOLLOWING || drives == driveStates.ANGLED_DRIVE || drives == driveStates.TURN) {
        //send signals 
        straightDrive.tankDrive( driveSignal.getSignals()[0],
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

  
  public boolean pathIsDone() {
    return purell.isDone();
  }
  
  //angled drive 
  
  /**
   * Makes the drive controller drive at an angle
   * @param driveSet : setpoint to drive to
   * @param angleSet : angle to turn to
   */
  public void driveAtAngle(double driveSet ,double angleSet) {
    drives = driveStates.ANGLED_DRIVE;
    angledDriveCont.setSetpoint(driveSet , angleSet);
  }
  
  public void unInvert() {
    leftEncoder.setReverseDirection(false);
    rightEncoder.setReverseDirection(false);
  }
  public void flipPath() {
    purell.flip();
    leftEncoder.setReverseDirection(true);
    rightEncoder.setReverseDirection(true);
  }

  /**
   * angle drive check
   * @return if the angle drive is done
   */
  public boolean angleDriveDone() {
    return angledDriveCont.isDone();
  }
  
  public void reset() {
    NASA.reset();
  }
  public void resetSensors() {
    gyro.reset();
    leftEncoder.reset();
    rightEncoder.reset();
  }
  
  //turning 
  public void turnToAngle(double angle) {
    System.out.println("starting turn" + angle);
    if(Math.abs(turnStp) > 1) {
      turnStp = angle;
      drives = driveStates.TURN;
    } else {
      done = true;
    }
  }
  
  public boolean getTurnDone() {
    return done;
  }
  
  public double getGyroHeading() {
    return gyro.getAngle();
  }
  
  //shifters
  
  public void setSolenoid(boolean open) {
    shifters.set(open);
  }
  
  public boolean getShifterState() {
    return shifters.get();
  }
  
}
