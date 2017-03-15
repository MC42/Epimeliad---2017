package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.navigation.ConstantVelocityCommandModel;
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

  private static DriveTrain driveTrainInstance = null;
  public static DriveTrain getDriveTrain(Joystick leftJ , Joystick rightJ) {
    if(driveTrainInstance == null) {
      driveTrainInstance = new DriveTrain(leftJ, rightJ);
    }
    return driveTrainInstance;
  }

  private enum driveStates {
    STOP , VELOCITY_CONTROL , OPEN_LOOP , ANGLED_DRIVE , PATH_FOLLOWING , TURN , DEAD_RECKON
  };
  private driveStates drives = driveStates.STOP;

  private enum shiftState {
    MANUAL_HIGH , MANUAL_LOW , AUTOMATIC
  };
  private shiftState shift = shiftState.MANUAL_HIGH;

  //joysticks
  private Joystick left;
  private Joystick right;
  private double turnSetP;
  private boolean turnDone;

  private static final double WHEEL_DIAM = 4;
  private static final double LOW_THRESHOLD = 6; //the point at which the robot shifts

  //motors
  private Victor leftVictor;
  private Victor rightVictor;
  private DualSignal driveSignal;

  //sensors
  private ADXRS450_Gyro gyro;
  private Encoder leftEncoder;
  private Encoder rightEncoder;
  private NemesisSolenoid shifters;

  //control systems
  private NemesisDrive straightDrive;
  private PurePursuitController pureP;
  private NavigationalSystem navigationSys;
  private DriveAtAngleController angledDriveCont;
  private ConstantVelocityCommandModel constantCommand;

  public DriveTrain(Joystick leftJ , Joystick rightJ) {

    //joysticks
    turnDone = false;
    turnSetP = 0;
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

    //units are in feet
    leftEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / 12));
    rightEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / 12));

    //control systems
    constantCommand = new ConstantVelocityCommandModel(0.8, 0.4);
    straightDrive = new NemesisDrive(gyro,  leftVictor, rightVictor);
    angledDriveCont = new DriveAtAngleController(3, VELFF, 0.09 , 0.000); //0.075 //3 //0.09
    navigationSys = new NavigationalSystem(leftEncoder, rightEncoder , gyro);
    pureP = new PurePursuitController(PUREKV , MAXACC,  LOOKAHEAD );
  }

  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {
      shifters.set(false);
    }

    @Override
    public void loop(double delta) {

      synchronized(this) {
        switch (drives) {
          case STOP:
            break;

          case VELOCITY_CONTROL:
           
            /*//teleop drive
            constantCommand.calculate((leftEncoder.getDistance()+rightEncoder.getDistance())/2,
                (leftEncoder.getRate()+rightEncoder.getRate())/2, delta);

            //left drive signal calculations
            double leftSignal = leftVelCont.calculate(constantCommand.getCurrentPosition(),
                constantCommand.getCurrentVelocity(),
                leftEncoder.getDistance());

            //right side signal calculations
            double rightSignal = rightVelCont.calculate(constantCommand.getCurrentPosition(),
                constantCommand.getCurrentVelocity(),
                rightEncoder.getDistance());

            driveSignal.updateSignal(leftSignal, rightSignal);
            break;*/
            break;
            
          case OPEN_LOOP :
            //pure driver control
            straightDrive.openLoopDrive(-left.getY(), right.getX());
            break;

          case PATH_FOLLOWING :
            //update signals for path following
            driveSignal.updateSignal(pureP.Calculate(navigationSys.getCurrentPoint(), true , delta) ,
                pureP.Calculate(navigationSys.getCurrentPoint() , false , delta));
            break;

          case ANGLED_DRIVE :
            //update signals for drive at an angle
            driveSignal.updateSignal(angledDriveCont.calculate(leftEncoder.getDistance() , gyro.getAngle() , false , delta) ,
                angledDriveCont.calculate(rightEncoder.getDistance() , gyro.getAngle() , true , delta));
            break;

          case TURN :
            double error = turnSetP - gyro.getAngle();
            double kP = 0.09; 
            if(Math.abs(error) > .5) {
              turnDone = false;
              driveSignal.updateSignal(-error*kP, error*kP);
            } else {
              turnDone = true;
              System.out.println("done " + error);
            }
            
            straightDrive.tankDrive(driveSignal.getSignals()[0], driveSignal.getSignals()[1] );
            break;
          case DEAD_RECKON :
            straightDrive.tankDrive(driveSignal.getSignals()[0], driveSignal.getSignals()[1] );
            break;
          default :
            driveSignal.updateSignal(0,0);
            DriverStation.reportWarning("Hit default case in drive train", false);
            break;
        }

        //System.out.printf("current x: %.3f currrent y: %.3f current theta: %.3f raw: %.3f\n" , NASA.getCurrentPoint()._x , NASA.getCurrentPoint()._y , NASA.getCurrentPoint()._theta , leftEncoder.getDistance()*1.054);
        switch(shift) {
          case MANUAL_HIGH :
            shifters.set(false);
            break;
          case MANUAL_LOW :
            shifters.set(true);
            break;
          case AUTOMATIC :
            double robotSpeed = ((leftEncoder.getRate()*60) + (rightEncoder.getRate()*60))/2;
            shifters.set(robotSpeed < LOW_THRESHOLD);
            break;
          default :
            break;
        }

        if(drives == driveStates.PATH_FOLLOWING || drives == driveStates.ANGLED_DRIVE || drives == driveStates.TURN || drives == driveStates.VELOCITY_CONTROL) {
          
          SmartDashboard.putNumber("Gyro", gyro.getAngle());
          SmartDashboard.putNumber("Left Drive Encoder", leftEncoder.getDistance());
          SmartDashboard.putNumber("Right Drive Encoder", rightEncoder.getDistance());
          //send signals
          straightDrive.tankDrive( driveSignal.getSignals()[0],
              driveSignal.getSignals()[1] );
        }
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

  public void force(double left , double right) {
    drives = driveStates.DEAD_RECKON;
    driveSignal.updateSignal(left, right);  
  }
  /**
   * Resets the angled drive controller
   */
  public void resetDriveController() {
    angledDriveCont.reset();
  }

  //teleop control

  /**
   * Starts teleop in velocity control mode
   */
  public void setVelControl() {
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
    pureP.setPath(newPath);
  }

  /**
   * Follows the path using the purell controller
   */
  public void followPath() {
    drives = driveStates.PATH_FOLLOWING;
  }

  /**
   * Checks if the path is done
   * @return if the path is done
   */
  public boolean pathIsDone() {
    return pureP.isDone();
  }
 
  /**
   * Sets the follower from backwards paths
   * to forwards paths
   */
  public void unFlipPath() {
    pureP.unFlip();
    leftEncoder.setReverseDirection(false);
    rightEncoder.setReverseDirection(false);
  }

  /**
   * Sets the follower from forwards paths
   * to backwards paths
   */
  public void flipPath() {
    pureP.flip();
    leftEncoder.setReverseDirection(true);
    rightEncoder.setReverseDirection(true);
  }

  /**
   * @deprecated : do not use, in development
   * @param position
   * @param velocity
   */
  public void setVelSetpoint(double position , double velocity) {
    constantCommand.setSetpoint(position, velocity);
    /*leftVelCont.setSetpoint(position , false);
      rightVelCont.setSetpoint(position , false);*/
  }

  /**
   * @deprecated do not use, in development
   */
  public void commandModelReset() {
    constantCommand.reset();
  }


  /**
   * Makes the drive controller drive at an angle
   * @param driveSet : setpoint to drive to
   * @param angleSet : angle to turn to
   */
  public void driveAtAngle(double driveSet ,double angleSet) {
    drives = driveStates.ANGLED_DRIVE;
    if(Math.abs(driveSet) < 1.5) {
      angledDriveCont.changeF(SMALLFF);
    } else {
      angledDriveCont.changeF(VELFF);
    }
    angledDriveCont.setSetpoint(driveSet , angleSet);
  }

 
  /**
   * angle drive check
   * @return if the angle drive is done
   */
  public boolean angleDriveDone() {
    return angledDriveCont.isDone();
  }

  /**
   * Resets the path follower
   * this includes encoder values and drive values
   */
  public void resetPath() {
    navigationSys.reset();
  }

  /**
   * resets all drive sensors
   */
  public void resetSensors() {
    gyro.reset();
    leftEncoder.reset();
    rightEncoder.reset();
  }

  //turning
  
  /**
   * Turn to a specified angle
   * @param angle : angle to turn to
   */
  public void turnToAngle(double angle) {
    if(Math.abs(angle) > 1) {
      turnSetP = angle;
      drives = driveStates.TURN;
    } else {
      turnDone = true;
    }
  }

  /**
   * Tells if the turn is done
   * @return if the turn is done
   */
  public boolean getTurnDone() {
    return turnDone;
  }

  /**
   * Gets the heading of the robot
   * @return the heading of the robot
   */
  public double getGyroHeading() {
    return gyro.getAngle();
  }

  //shifters

  /**
   * Shift into high gear
   */
  public void shiftHigh() {
    shift = shiftState.MANUAL_HIGH;
  }

  /**
   * Shift into low gear
   */
  public void shiftLow() {
    shift = shiftState.MANUAL_LOW;
  }

  /**
   * Switch into automatic shifting
   */
  public void autoShifting() {
    shift = shiftState.AUTOMATIC;
  }


  /**
   * Returns the left drive encoder
   * @return left drive encoder
   */
  public Encoder getLeftEncoder() {
    return leftEncoder;
  }
  
  /**
   * Returns the right drive encoder
   * @return right drive encoder
   */
  public Encoder getRightEncoder() {
    return rightEncoder;
  }
 
}
