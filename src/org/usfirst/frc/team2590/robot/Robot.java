package org.usfirst.frc.team2590.robot;
import java.util.HashMap;

import org.usfirst.frc.team2590.looper.Looper;
import org.usfirst.frc.team2590.routine.AutoRoutine;
import org.usfirst.frc.team2590.routine.DoNothing;
import org.usfirst.frc.team2590.routine.FivePointBoi;
import org.usfirst.frc.team2590.routine.FrontGearDrop;
import org.usfirst.frc.team2590.routine.HopperShootLeft;
import org.usfirst.frc.team2590.routine.SideGearSimple;
import org.usfirst.frc.team2590.routine.SideGearWithShooting;
import org.usfirst.frc.team2590.routine.TwoGear;
import org.usfirst.frc.team2590.subsystem.Climber;
import org.usfirst.frc.team2590.subsystem.DriveTrain;
import org.usfirst.frc.team2590.subsystem.Feeder;
import org.usfirst.frc.team2590.subsystem.GearIntake;
import org.usfirst.frc.team2590.subsystem.Intake;
import org.usfirst.frc.team2590.subsystem.Shooter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.SmartJoystick;
import util.Vision;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot implements RobotMap{

  //subsystems
  public static Climber climb;
  public static Intake intake;
  public static Feeder feeder;
  public static Shooter shooter;
  public static DriveTrain driveT;
  public static GearIntake gearHold;


  //autonomous uses polymorpism
  private static AutoRoutine auto;
  private static HashMap<String, AutoRoutine> autoMap;

  //joysticks
  private SmartJoystick leftJoy;
  private SmartJoystick rightJoy;
  private SmartJoystick operatorJoy;

  //Vision
  public static Vision vision;

  //Looper
  private static Looper enabledLooper;

  //compressor
  private static Compressor compressor;
  private static Solenoid plug3 , plug4;

  @Override
  public void robotInit() {

    vision = Vision.getVisionInstance();

    //joysticks
    leftJoy = new SmartJoystick(0 , 0.1 , 0.1);
    rightJoy = new SmartJoystick(1 , 0.1 , 0.1);
    operatorJoy = new SmartJoystick(2, 0.0, 0.0);

    //subsystems
    feeder = Feeder.getFeeder();
    intake  = Intake.getIntake();
    climb   = Climber.getClimber();
    shooter = Shooter.getShooter();
    gearHold = GearIntake.getGearHolder();
    driveT  = DriveTrain.getDriveTrain(leftJoy, rightJoy);

    //looper
    enabledLooper = new Looper(0.02);
    enabledLooper.register(climb.getClimbLoop());
    enabledLooper.register(driveT.getDriveLoop());
    enabledLooper.register(feeder.getFeederLoop());
    enabledLooper.register(intake.getIntakeLoop());
    enabledLooper.register(shooter.getShootLoop());
    enabledLooper.register(gearHold.getGearLoop());

    //autonomous modes
    autoMap = new HashMap<String , AutoRoutine>();
    // autoMap.put("Hopper", new FourtyBall());
    autoMap.put("Nothing",    new DoNothing());
    autoMap.put("Five Drive", new FivePointBoi());
    autoMap.put("Hopper Left", new HopperShootLeft());
    autoMap.put("Front Gear Left", new FrontGearDrop(true));
    autoMap.put("Turn Left Gear",  new SideGearSimple(true));
    autoMap.put("Front Gear Right", new FrontGearDrop(false));
    autoMap.put("Turn Right Gear",  new SideGearSimple(false));
    autoMap.put("Two Gear",  new TwoGear());
    autoMap.put("Turn Left Gear Shoot",  new SideGearWithShooting(true));    
    autoMap.put("Turn Right Gear Shoot",  new SideGearWithShooting(false));
    

    //solenoids
    plug3 = new Solenoid(3);
    plug4 = new Solenoid(2);

    //compressor
    compressor = new Compressor();
    compressor.start();
    compressor.clearAllPCMStickyFaults();
  }

  /**
   * Every thing below this line handles how the driver
   * talks to the fms and the fms to the robot
   * --------------------------------------------------
   */

  @Override
  public void disabledInit() {
    System.out.println("Stopping loops");
    enabledLooper.onEnd();
  }

  @Override
  public void disabledPeriodic() {
    //makes sure the pcm works by keeping it active
    compressor.setClosedLoopControl(true);
    driveT.resetPath();
    driveT.resetDriveController();
    driveT.resetSensors();
  }

  @Override
  public void autonomousInit() {

    //get the auton to run
    auto = autoMap.get(SmartDashboard.getString("DB/String 0" , "Left Gear Left"));
    System.out.println("Starting loops , auto = " + SmartDashboard.getString("DB/String 0" , "Front Gear Left"));

    //start the loops
    enabledLooper.startLoops();

    //reset the robot
    driveT.resetSensors();
    driveT.shiftHigh();
    gearHold.stopGearIntake();

    //run auton
    auto.run();

  }

  @Override
  public void autonomousPeriodic() {

  }


  @Override
  public void teleopInit() {
    System.out.println("Starting loops");

    //start the loops
    enabledLooper.startLoops();
    driveT.setOpenLoop();

    //reset the robot
    driveT.resetSensors();
    driveT.shiftHigh();
    gearHold.stopGearIntake();

    //close all excess vents
    plug3.set(false);
    plug4.set(false);
    
    Robot.gearHold.turnOnGrip(true);
  }

  @Override
  public void teleopPeriodic() {

    //intake balls
    if(leftJoy.getRawButton(1)) {
      if(!rightJoy.getRawButton(1) && !rightJoy.getRawButton(2)) {
        intake.intakeBalls();
        feeder.expellBalls();
      } else {
        intake.stopIntake();
      }
    } else if(leftJoy.getRawButton(3)) {
      if(!rightJoy.getRawButton(1) && !rightJoy.getRawButton(2)) {
        intake.outtakeBalls();
      } else {
        intake.stopIntake();
      }
    } else {
      if(!operatorJoy.getRawButton(3) && !rightJoy.getRawButton(6)) {
        intake.stopIntake();
      }
    }

    //handle shifting
    if(rightJoy.getFallingEdge(5)) {
      driveT.shiftHigh();
    } else if(rightJoy.getFallingEdge(3)) {
      driveT.shiftLow();
    }


    //handle shooting
    if(rightJoy.getRawButton(4)) {
      shooter.revShooter();
      //intake.dropIntake();
    }
    
    if(rightJoy.getRawButton(1)) {
      if(!leftJoy.getRawButton(1) && !leftJoy.getRawButton(2))
        gearHold.intakeGear();
      else
        gearHold.stopGearIntake();
    } else if(rightJoy.getRawButton(2)) {
      if(!leftJoy.getRawButton(1) && !leftJoy.getRawButton(2))
        gearHold.outTakeGear();
      else
        gearHold.stopGearIntake();    
    } else {
      gearHold.stopGearIntake();
    }

    if(!rightJoy.getRawButton(4)) {
      shooter.stopShooter();
    }

    if(!leftJoy.getRawButton(1) && !rightJoy.getRawButton(4)
        && !operatorJoy.getRawButton(1) && !operatorJoy.getRawButton(2)) {
      feeder.stopFeeder();
    }

    //handle climber
    if(rightJoy.getRawButton(6)) {
      compressor.stop();
      driveT.shiftLow();
      climb.startClimb();
    } else {
      climb.stopClimb();
      compressor.start();
    }

    shooter.setSetpoint(SmartDashboard.getNumber("DB/Slider 0" , 0));


    //OPERATOR CONTROLLS
    if(operatorJoy.getRawButton(1)) {
      feeder.expellBalls();
    } else if (operatorJoy.getRawButton(2)) {
      feeder.feedIntoShooter();
    }

    if(operatorJoy.getRawButton(3)) {
      intake.agitate();
    }


  }
}

