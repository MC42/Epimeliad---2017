package org.usfirst.frc.team2590.robot;
import java.util.HashMap;

import org.usfirst.frc.team2590.looper.Looper;
import org.usfirst.frc.team2590.routine.AutoRoutine;
import org.usfirst.frc.team2590.routine.DoNothing;
import org.usfirst.frc.team2590.routine.FivePointBoi;
import org.usfirst.frc.team2590.routine.FourtyBall;
import org.usfirst.frc.team2590.routine.FourtyBallRefined;
import org.usfirst.frc.team2590.routine.FrontGearDrop;
import org.usfirst.frc.team2590.routine.FrontGearShoot;
import org.usfirst.frc.team2590.routine.SideGearSimple;
import org.usfirst.frc.team2590.routine.SideGearWithShooting;
import org.usfirst.frc.team2590.routine.TwoGear;
import org.usfirst.frc.team2590.subsystem.Climber;
import org.usfirst.frc.team2590.subsystem.DriveTrain;
import org.usfirst.frc.team2590.subsystem.ExpandingBox;
import org.usfirst.frc.team2590.subsystem.Feeder;
import org.usfirst.frc.team2590.subsystem.GearIntake;
import org.usfirst.frc.team2590.subsystem.Intake;
import org.usfirst.frc.team2590.subsystem.Shooter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.LEDController;
import util.NemesisCamera;
import util.NemesisDash;
import util.NemesisJoystick;
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
  public static ExpandingBox expandBox;
  public static PowerDistributionPanel pdp;
  
  //autonomous uses polymorpism
  public static AutoRoutine auto;
  public static HashMap<String, AutoRoutine> autoMap;

  //joysticks
  public static NemesisJoystick leftJoy;
  public static NemesisJoystick rightJoy;
  public static NemesisJoystick operatorJoy;

  //Vision
  public static Vision vision;
  public static LEDController ledController;

  //Looper
  public static Looper LEDLooper;
  public static Looper enabledLooper;

  //compressor
  public static Solenoid plug4;
  public static Compressor compressor;

  //camera
  public static NemesisCamera rearViewCamera;

  public static int PDPCLIMBERONE = 0;
  public static int PDPCLIMBERTWO = 0;

  //dashboard
  private static NemesisDash shooterSetpoint;
  
  @Override
  public void robotInit() {

    //misc, but probably needs to be init first
    vision = Vision.getVisionInstance();
    ledController = LEDController.getLED();

    //joysticks
    leftJoy = new NemesisJoystick(0 , 0.1 , 0.1);
    rightJoy = new NemesisJoystick(1 , 0.1 , 0.1);
    operatorJoy = new NemesisJoystick(2, 0.0, 0.0);

    //subsystems
    feeder = Feeder.getFeeder();
    intake  = Intake.getIntake();
    climb   = Climber.getClimber();
    shooter = Shooter.getShooter();
    gearHold = GearIntake.getGearHolder();
    expandBox = ExpandingBox.getExpandingBox();
    driveT  = DriveTrain.getDriveTrain(leftJoy, rightJoy);

    //looper
    enabledLooper = new Looper(0.02);
    enabledLooper.register(climb.getClimbLoop());
    enabledLooper.register(driveT.getDriveLoop());
    enabledLooper.register(expandBox.getBoxLoop());
    enabledLooper.register(feeder.getFeederLoop());
    enabledLooper.register(intake.getIntakeLoop());
    enabledLooper.register(gearHold.getGearLoop());
    enabledLooper.register(shooter.getShootLoop());

    
    LEDLooper = new Looper(0.03);
    LEDLooper.register(ledController.getLEDLoop());
    LEDLooper.startLoops();

    //autonomous modes
    autoMap = new HashMap<String , AutoRoutine>();

    autoMap.put("Five", new FivePointBoi());    //drives to the five point line
    autoMap.put("Two Gear",  new TwoGear());    // (DO NOT USE) drops a gear on the front peg, gets an alliance partners gear and puts that on the peg
    autoMap.put("Nothing",    new DoNothing()); //does absolutely nothing
    autoMap.put("Front Gear", new FrontGearDrop()); //drops a gear on the middle peg $$
    autoMap.put("Hopper Left", new FourtyBall(true)); //40 ball on the left hopper relative to boiler position from driver as looking at field $$
    autoMap.put("Hopper Right", new FourtyBall(false)); //40 ball on the left hopper relative to boiler position from driver as looking at field $$
    autoMap.put("Turn Left Gear",  new SideGearSimple(true)); //Turns to the left peg relative to driver (really turns right, its just onto the left peg) $$
    autoMap.put("Front Gear Shoot",  new FrontGearShoot()); //(UNTESTED on a field) puts a gear on the middle peg and curves to the boiler
    autoMap.put("Turn Right Gear",  new SideGearSimple(false)); //same as left gear see above, but for the right peg
    autoMap.put("Hopper Left New", new FourtyBallRefined(true)); //UNTESTED , theoretical path, slightly faster path than above hopper autos
    autoMap.put("Hopper Right New", new FourtyBallRefined(false)); //UNTESTED , theoretical path, slightly faster path than above hopper autos
    autoMap.put("Turn Left Gear Shoot",  new SideGearWithShooting(true));  //DOES NOT WORK , Drops a gear on the left peg and shoots 10 balls
    autoMap.put("Turn Right Gear Shoot",  new SideGearWithShooting(false)); //DOES NOT WORK , Drops a gear on the right peg and shoots 10 balls
    
    //solenoids
    plug4 = new Solenoid(3);

    //compressor
    compressor = new Compressor();
    compressor.stop();
    compressor.clearAllPCMStickyFaults();
    
    //camera
    rearViewCamera = new NemesisCamera();
    //rearViewCamera.addUsbCamera();
    rearViewCamera.setCameraIP("10.25.90.11");
    
    //dash
    shooterSetpoint = new NemesisDash("DB/Slider 0");
    
    pdp = new PowerDistributionPanel();
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
    plug4.set(false);
    
    //starts the grip
    gearHold.turnOnGrip(true);
    //compressor.stop();

  }

  @Override
  public void teleopPeriodic() {
    //compressor.stop();

    gearHold.turnOnGrip(true);

    if(leftJoy.getFallingEdge(TOGGLE_BOX)) {
      expandBox.toggleBox();
    }
    
    //intake balls
    if(leftJoy.getRawButton(FRONT_INTAKE_IN)) {
        intake.intakeBalls();
        feeder.expellBalls();
    } else {
      if(!operatorJoy.getRawButton(INTAKE_AGITATE)) {
        intake.stopIntake();
      }
    }
    
    if(leftJoy.getRawButton(2)) {
      gearHold.expellGear();
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
    
    if(rightJoy.getFallingEdge(DRIVER_REV_SHOOTER)) {
      rearViewCamera.changeToNightMode();
      Robot.driveT.shiftLow();
      driveT.resetSensors();
      driveT.turnToAngle(vision.angleToTarget());
    }
    
    //System.out.println("dist " + vision.xDistanceToTarget());
    if(driveT.getTurnDone() || operatorJoy.getRawButton(6) || Math.abs(leftJoy.getY()) > 0.1 || Math.abs(rightJoy.getX()) > 0.1) {
      driveT.setOpenLoop();
    }
    
    if(rightJoy.getFallingEdge(GEAR_INTAKE_IN) || rightJoy.getFallingEdge(GEAR_INTAKE_OUT)) {
      gearHold.averageReset();
    }
    
    if(rightJoy.getRawButton(GEAR_INTAKE_IN)) {
        gearHold.intakeGear();
    } else if(rightJoy.getRawButton(GEAR_INTAKE_OUT)) {
      gearHold.expellGear();
    } else if(leftJoy.getRawButton(2)) { 
      gearHold.outTakeGear();
    }else {
      if(!operatorJoy.getRawButton(OPERATOR_OUT_GEAR)) {
        gearHold.stopGearIntake();
      }
    }


    //handle climber
    if(rightJoy.getRawButton(CLIMB)) {
      compressor.stop();
      driveT.shiftLow();
      climb.startClimb();
    } else if(!rightJoy.getRawButton(CLIMB) && !operatorJoy.getRawButton(OPERATOR_REV_CLIMBER)) {
      climb.stopClimb();
      //compressor.start();
    }

    shooter.setSetpoint(shooterSetpoint.getNumber());
    //shooter.setSetpoint(vision.interpolateSpeed(vision.xDistanceToTarget()));


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
    } else {
      shooter.stopShooter();
    }

    if(operatorJoy.getRawButton(OPERATOR_OUT_GEAR)) {
      gearHold.expellGear();
    }

    if(operatorJoy.getRawButton(OPERATOR_REV_CLIMBER)) {
      compressor.stop();
      driveT.shiftLow();
      climb.inverseClimb();
    }
    
    if(operatorJoy.getPOV() == 0) {
      shooterSetpoint.updateAdd(50);
    } else if(operatorJoy.getPOV() == 180) {
      shooterSetpoint.updateSubtract(50);
    }
    
  }
  
  public void testPeriodic() {
    System.out.println("Climber 1 " + pdp.getCurrent(PDPCLIMBERONE));
    System.out.println("Climber 2 " + pdp.getCurrent(PDPCLIMBERTWO));
  }
}

