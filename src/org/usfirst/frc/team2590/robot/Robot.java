package org.usfirst.frc.team2590.robot;
import java.util.HashMap;

import org.usfirst.frc.team2590.looper.Looper;
import org.usfirst.frc.team2590.routine.AutoRoutine;
import org.usfirst.frc.team2590.routine.CoolDahanyAuto;
import org.usfirst.frc.team2590.routine.DoNothing;
import org.usfirst.frc.team2590.routine.FivePointBoi;
import org.usfirst.frc.team2590.routine.FourtyBall;
import org.usfirst.frc.team2590.routine.FrontGearDrop;
import org.usfirst.frc.team2590.routine.LeftGear;
import org.usfirst.frc.team2590.routine.LeftGearSimple;
import org.usfirst.frc.team2590.routine.RightGearSimple;
import org.usfirst.frc.team2590.subsystem.Climber;
import org.usfirst.frc.team2590.subsystem.DriveTrain;
import org.usfirst.frc.team2590.subsystem.GearHolder;
import org.usfirst.frc.team2590.subsystem.Intake;
import org.usfirst.frc.team2590.subsystem.Shooter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
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
  public static Shooter shooter;
  public static DriveTrain driveT;
  public static GearHolder gearHold;

  private double lastTime = 0;
  private double currentTime = 0;
  
  //autonomous
  private static AutoRoutine auto;
    //super class ::
  private static HashMap<String, AutoRoutine> autoMap;

  //joysticks
  private SmartJoystick leftJoy;
  private SmartJoystick rightJoy;
	
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
		
    //subsystems
    intake  = Intake.getIntake();
    climb   = Climber.getClimber();    
    shooter = Shooter.getShooter();
    gearHold = GearHolder.getGearHolder();
    driveT  = DriveTrain.getDriveTrain(leftJoy, rightJoy);
      
    
    //looper
    enabledLooper = new Looper(0.02);
    enabledLooper.register(driveT.getDriveLoop());
    enabledLooper.register(climb.getClimbLoop());
    enabledLooper.register(intake.getIntakeLoop());
    enabledLooper.register(shooter.getShootLoop());
    enabledLooper.register(gearHold.getGearLoop());
    
    //autonomous modes
    autoMap = new HashMap<String , AutoRoutine>();
    autoMap.put("Nothing",    new DoNothing());
    autoMap.put("Five Drive", new FivePointBoi());
    autoMap.put("Left Gear Left",  new LeftGear(true));
    autoMap.put("Left Gear Right",  new LeftGear(false));
    autoMap.put("Left Gear Simple", new LeftGearSimple());
    autoMap.put("Cool Dahany Auto", new CoolDahanyAuto());
    autoMap.put("Right Gear Simple", new RightGearSimple());
    autoMap.put("Front Gear Left", new FrontGearDrop(true));
    autoMap.put("Front Gear Right", new FrontGearDrop(false));
    autoMap.put("Hopper", new FourtyBall());
		
    //solenoids
    plug3 = new Solenoid(3);
    plug4 = new Solenoid(4);
    
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
  
  public void disabledPeriodic() {
    //makes sure the pcm works by keeping it active
    compressor.setClosedLoopControl(true);
  }
	
  @Override
  public void autonomousInit() {
    auto = autoMap.get(SmartDashboard.getString("DB/String 0" , "Left Gear Left"));
    System.out.println("Starting loops , auto = " + SmartDashboard.getString("DB/String 0" , "Front Gear Left"));
    enabledLooper.startLoops(); 
    auto.run();
    
  }

  @Override
  public void autonomousPeriodic() {
		
  }

	
  public void teleopInit() {
    System.out.println("Starting loops");
    enabledLooper.startLoops();
    driveT.setOpenLoop();
    
    //close all excess vents
    plug3.set(false);
    plug4.set(false);
  }
  
  @Override
  public void teleopPeriodic() {
    currentTime = Timer.getFPGATimestamp();
    
    //intake balls
    if(leftJoy.getRawButton(1)) {
      intake.intakeBalls();
      shooter.reversePully();
    } else if(leftJoy.getRawButton(2)) {
      intake.outtakeBalls();
    } else if(!leftJoy.getRawButton(1) && !leftJoy.getRawButton(2)) {
      intake.stopIntake();
    }
   
    //handle shifting
    if(rightJoy.getFallingEdge(5)) {
      driveT.shiftHigh();
    } else if(rightJoy.getFallingEdge(3)) {
      driveT.shiftLow();
    }
    
    //handle shooting
    if(rightJoy.getRawButton(1)) {
      shooter.revShooter();
    } else if(rightJoy.getRawButton(2)) {
      driveT.shiftLow();
      intake.agitate();
      shooter.shootNow();
    } else if (!rightJoy.getRawButton(1) && !leftJoy.getRawButton(1) && !rightJoy.getRawButton(2)) {
      shooter.stopShooter();
    }
    
    //handle climber
    if(rightJoy.getRawButton(6)) {
      compressor.stop();
      driveT.shiftLow();
      climb.startClimb(); 
    } else {
      compressor.start();
      climb.stopClimb();
    }
    
    if(rightJoy.getFallingEdge(4)) {
      gearHold.toggleWings();
    }
    
    shooter.setSetpoint(SmartDashboard.getNumber("DB/Slider 0" , 0));
    //System.out.println("delta t " + (currentTime - lastTime));
    lastTime = currentTime;
  }
}

