package org.usfirst.frc.team2590.robot;
import java.util.HashMap;

import org.usfirst.frc.team2590.looper.Looper;
import org.usfirst.frc.team2590.routine.AutoRoutine;
import org.usfirst.frc.team2590.routine.DoNothing;
import org.usfirst.frc.team2590.routine.FivePointBoi;
import org.usfirst.frc.team2590.routine.FrontGearDrop;
import org.usfirst.frc.team2590.routine.OnlyHopper;
import org.usfirst.frc.team2590.routine.TenPointBalls;
import org.usfirst.frc.team2590.subsystem.Climber;
import org.usfirst.frc.team2590.subsystem.DriveTrain;
import org.usfirst.frc.team2590.subsystem.Intake;
import org.usfirst.frc.team2590.subsystem.Shooter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
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
  public static DriveTrain dt;
  public static Climber climb;
  public static Intake intake;
  public static Shooter shooter;

  //autonomous
  private static String autoMode;
  private static AutoRoutine auto;
  private static HashMap<String, AutoRoutine> autoMap;

  //joysticks
  private SmartJoystick left;
  private SmartJoystick right;
	
  //Vision
  public static Vision vision;
  private static Looper visionLooper;

  //Looper
  private static Looper enabledLooper;
  
  //compressor
  private static Compressor compressor;
	
  @Override
  public void robotInit() {
		
    //joysticks
    left = new SmartJoystick(0 , 0.1 , 0.1);
    right = new SmartJoystick(1 , 0.1 , 0.1);
		
    //autonomous modes
    autoMap = new HashMap<String , AutoRoutine>();
    autoMap.put("Nothing",    new DoNothing());
    autoMap.put("Hopper",     new OnlyHopper());
    autoMap.put("Five Drive", new FivePointBoi());
    autoMap.put("Front Gear", new FrontGearDrop());
    autoMap.put("Ten Balls",  new TenPointBalls(true));
		
    autoMode = "Five Drive";
    auto = autoMap.get(autoMode);
		
    //subsystems
    climb = new Climber();    
    intake = new Intake();
    //shooter = new Shooter();
    dt = new DriveTrain(left, right);
	
    //looper
    enabledLooper = new Looper(10);
    enabledLooper.register(dt.getDriveLoop());
    enabledLooper.register(climb.getClimbLoop());
    enabledLooper.register(intake.getIntakeLoop());
    //enabledLooper.register(shooter.getShootLoop());
    
    //vision
    visionLooper = new Looper(50);
    vision = Vision.getVisionInstance();
    visionLooper.register(vision.getVisionLoop());

    compressor = new Compressor();
    compressor.start();
}

  @Override
  public void disabledInit() {
    System.out.println("Stopping loops");
    enabledLooper.onEnd();
  }
	
  @Override
  public void autonomousInit() {
    System.out.println("Starting loops");
    enabledLooper.startLoops(); 
    auto.run();
    
  }

  /**
   * This function is called periodically during autonomous
   */
  @Override
  public void autonomousPeriodic() {
		
  }

	
  public void teleopInit() {
    System.out.println("Starting loops");
    enabledLooper.startLoops();
    dt.setOpenLoop();
  }
  /**
   * This function is called periodically during operator control
   */
  @Override
  public void teleopPeriodic() {
    
    //intake balls
    if(left.getRawButton(1)) {
      intake.intakeBalls();
    } else if(left.getRawButton(2)){
      intake.outtakeBalls();
    } else {
      intake.stopIntake();
    }
    
  }
  
  /**
   * This function is called periodically during test mode
   */
  @Override
  public void testPeriodic() {
  }
}

