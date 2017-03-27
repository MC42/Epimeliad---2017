package org.usfirst.frc.team2590.robot;
import java.util.HashMap;

import org.usfirst.frc.team2590.looper.Looper;
import org.usfirst.frc.team2590.routine.AutoRoutine;
import org.usfirst.frc.team2590.routine.DoNothing;
import org.usfirst.frc.team2590.routine.FivePointBoi;
import org.usfirst.frc.team2590.routine.FrontGearDrop;
import org.usfirst.frc.team2590.routine.HopperShootLeft;
import org.usfirst.frc.team2590.routine.SideGearSimple;
import org.usfirst.frc.team2590.routine.SideGearWithHopper;
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
import util.LEDController;
import util.NemesisCamera;
import util.SmartJoystick;
import util.Vision;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot implements RobotMap , Controls {

  //subsystems
  public static Climber climb;
  public static Intake intake;
  public static Feeder feeder;
  public static Shooter shooter;
  public static DriveTrain driveT;
  public static GearIntake gearHold;
  
  //autonomous uses polymorpism
  public static AutoRoutine auto;
  public static HashMap<String, AutoRoutine> autoMap;

  //joysticks
  public static SmartJoystick leftJoy;
  public static SmartJoystick rightJoy;
  public static SmartJoystick operatorJoy;

  //Vision
  public static Vision vision;
  public static LEDController ledController;

  //Looper
  public static Looper LEDLooper;
  public static Looper enabledLooper;

  //compressor
  public static Compressor compressor;
  public static Solenoid plug3 , plug4;

  //camera
  public static NemesisCamera rearViewCamera;

  @Override
  public void robotInit() {

    //misc, but probably needs to be init first
    vision = Vision.getVisionInstance();
    ledController = LEDController.getLED();

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
    enabledLooper.register(gearHold.getGearLoop());
    enabledLooper.register(shooter.getShootLoop());

    
    LEDLooper = new Looper(0.03);
    LEDLooper.register(ledController.getLEDLoop());
    LEDLooper.startLoops();

    //autonomous modes
    autoMap = new HashMap<String , AutoRoutine>();

    autoMap.put("Two Gear",  new TwoGear());
    autoMap.put("Nothing",    new DoNothing());
    autoMap.put("Five Drive", new FivePointBoi());
    autoMap.put("Hopper Left", new HopperShootLeft());
    autoMap.put("Front Gear Left", new FrontGearDrop(true));
    autoMap.put("Turn Left Gear",  new SideGearSimple(true));
    autoMap.put("Front Gear Right", new FrontGearDrop(false));
    autoMap.put("Turn Right Gear",  new SideGearSimple(false));
    autoMap.put("Turn Left Gear Hopper",  new SideGearWithHopper(true));    
    autoMap.put("Turn Left Gear Shoot",  new SideGearWithShooting(true));  
    autoMap.put("Turn Right Gear Hopper",  new SideGearWithHopper(false));
    autoMap.put("Turn Right Gear Shoot",  new SideGearWithShooting(false));
    

    //solenoids
    plug3 = new Solenoid(3);
    plug4 = new Solenoid(2);

    //compressor
    compressor = new Compressor();
    compressor.stop();
    compressor.clearAllPCMStickyFaults();
    
    //camera
    rearViewCamera = new NemesisCamera(false);
    rearViewCamera.setCameraIP("10.25.90.11");
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
    LEDLooper.startLoops();
    ledController.updateGearState(false);
    ledController.updateDisabledState(true);
    ledController.updateShooterState("OFF");
    ledController.updateClimbingState(false);
  }

  @Override
  public void disabledPeriodic() {
    //makes sure the pcm works by keeping it active
    compressor.setClosedLoopControl(true);
    
    //reset everything
    driveT.resetPath();
    driveT.resetDriveController();
    driveT.resetSensors();
  }

  @Override
  public void autonomousInit() {
    ledController.updateDisabledState(false);

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
    ledController.updateDisabledState(false);

    //ends auto
    if(auto != null) {
      auto.end();
    }
    
    //start the loops
    enabledLooper.startLoops();
    LEDLooper.startLoops();
    driveT.setOpenLoop();

    //reset the robot
    driveT.resetSensors();
    driveT.shiftHigh();
    
    intake.stopIntake();
    gearHold.stopGearIntake();

    //close all excess vents
    plug3.set(false);
    plug4.set(false);
    
    //starts the grip
    gearHold.turnOnGrip(true);
  }

  @Override
  public void teleopPeriodic() {
    driveT.setOpenLoop();
    gearHold.turnOnGrip(true);

    //intake balls
    if(leftJoy.getRawButton(FRONT_INTAKE_IN)) {
        intake.intakeBalls();
        feeder.expellBalls();
    } else if(leftJoy.getRawButton(FRONT_INTAKE_OUT)) {
        intake.outtakeBalls();
    } else {
      if(!operatorJoy.getRawButton(INTAKE_AGITATE)) {
        intake.stopIntake();
      }
    }
    
    //handle feeder
    if(!leftJoy.getRawButton(FRONT_INTAKE_IN) && !operatorJoy.getRawButton(FEEDER_OUT) && !operatorJoy.getRawButton(FEEDER_IN)) {
      feeder.stopFeeder();
    }

    //handle shifting
    if(rightJoy.getFallingEdge(DRIVE_SHIFT_HIGH)) {
      driveT.shiftHigh();
    } else if(rightJoy.getFallingEdge(DRIVE_SHIFT_LOW)) {
      driveT.shiftLow();
    }


    //handle shooting
    if(rightJoy.getRawButton(DRIVER_REV_SHOOTER)) {
      feeder.feedIntoShooter();
    } else {
      if(!operatorJoy.getRawButton(OPERATOR_REV_SHOOTER)) {
        shooter.stopShooter();
      }
    }
    
    if(rightJoy.getFallingEdge(GEAR_INTAKE_IN) || rightJoy.getFallingEdge(GEAR_INTAKE_OUT)) {
      gearHold.averageReset();
    }
    
    if(rightJoy.getRawButton(GEAR_INTAKE_IN)) {
        gearHold.intakeGear();
    } else if(rightJoy.getRawButton(GEAR_INTAKE_OUT)) {
       gearHold.outTakeGear();
    } else {
      if(!operatorJoy.getRawButton(OPERATOR_OUT_GEAR)) {
        gearHold.stopGearIntake();
      }
    }


    //handle climber
    if(rightJoy.getRawButton(CLIMB)) {
      compressor.stop();
      driveT.shiftLow();
      climb.startClimb();
    } else {
      climb.stopClimb();
      compressor.start();
    }

    shooter.setSetpoint(SmartDashboard.getNumber("DB/Slider 0" , 0));


    //OPERATOR CONTROLLS
    if(operatorJoy.getRawButton(FEEDER_OUT)) {
      feeder.expellBalls();
    } else if (operatorJoy.getRawButton(FEEDER_IN)) {
      feeder.feedIntoShooter();
    }

    if(operatorJoy.getRawButton(INTAKE_AGITATE)) {
      intake.agitate();
    }
    
    if(operatorJoy.getRawButton(OPERATOR_REV_SHOOTER)) {
      shooter.revShooter();
    }

    if(operatorJoy.getRawButton(OPERATOR_OUT_GEAR)) {
      gearHold.expellGear();
    }

  }
}

