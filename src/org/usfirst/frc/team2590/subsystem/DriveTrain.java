package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.navigation.ConstantVelocityCommandModel;
import org.usfirst.frc.team2590.navigation.ConstantVelocityController;
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

  private enum shiftState {
    MANUAL_HIGH , MANUAL_LOW , AUTOMATIC
  };
  private shiftState shift = shiftState.MANUAL_HIGH;

  //joysticks
  private boolean done;
  private Joystick left;
  private Joystick right;
  private double turnStp;

  private static final double WHEEL_DIAM = 4;
  private static final double LOW_THRESHOLD = 6; //the point at which to shift

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
  private PurePursuitController purell;
  private ConstantVelocityController leftVelCont;
  private ConstantVelocityController rightVelCont;
  private DriveAtAngleController angledDriveCont;
  private ConstantVelocityCommandModel constantCommand;

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
    leftVelCont = new ConstantVelocityController(0.05, 0.2);
    rightVelCont = new ConstantVelocityController(0.05, 0.2);
    constantCommand = new ConstantVelocityCommandModel(0.8, 0.4);
    //max aacc , velff , kP , kI
    angledDriveCont = new DriveAtAngleController(3, VELFF, 0.09 , 0.000); //0.075 //3 //0.09
    NASA = new NavigationalSystem(leftEncoder, rightEncoder , gyro);
    purell = new PurePursuitController(PUREKV , MAXACC,  LOOKAHEAD);
    straightDrive = new NemesisDrive(gyro,  leftVictor, rightVictor);
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
            //teleop drive
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
            break;

          case OPEN_LOOP :

            //pure driver control
            straightDrive.openLoopDrive(-left.getY(), right.getX());
            break;

          case PATH_FOLLOWING :
            //update signals for path following
            driveSignal.updateSignal(purell.Calculate(NASA.getCurrentPoint(), true , delta) ,
                purell.Calculate(NASA.getCurrentPoint(), false , delta));
            break;

          case ANGLED_DRIVE :
            //update signals for drive at an angle
            driveSignal.updateSignal(angledDriveCont.calculate(leftEncoder.getDistance() , gyro.getAngle() , false , delta) ,
                angledDriveCont.calculate(rightEncoder.getDistance() , gyro.getAngle() , true , delta));
            break;

          case TURN :
            //System.out.println("turning to " + turnStp);
            double error = turnStp - gyro.getAngle();
            double kP = 0.07; //0.09 real
            if(Math.abs(error) > .5) {
              done = false;
              driveSignal.updateSignal(-error*kP, error*kP);
            } else {
              System.out.println("done " + error);
            }
            straightDrive.tankDrive( driveSignal.getSignals()[0],
                driveSignal.getSignals()[1] );
            break;

          default :
            driveSignal.updateSignal(0,0);
            DriverStation.reportWarning("Hit default case in drive train", false);
            break;
        }

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
        //System.out.println("left " + leftEncoder.getDistance() + " " + rightEncoder.getDistance());
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

  public void resetDrive() {
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
    purell.setPath(newPath);
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
    return purell.isDone();
  }


  public void setVelSetpoint(double position , double velocity) {
    constantCommand.setSetpoint(position, velocity);
    leftVelCont.setSetpoint(position , false);
    rightVelCont.setSetpoint(position , false);
  }

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
    //System.out.println("starting turn" + angle);
    if(Math.abs(angle) > 1) {
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

  public void shiftHigh() {
    shift = shiftState.MANUAL_HIGH;
  }

  public void shiftLow() {
    shift = shiftState.MANUAL_LOW;
  }

  public void autoShifting() {
    shift = shiftState.AUTOMATIC;
  }

  public boolean getShifterState() {
    return shifters.get();
  }

}
