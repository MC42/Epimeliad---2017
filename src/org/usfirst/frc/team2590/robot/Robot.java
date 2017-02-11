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
import org.usfirst.frc.team2590.subsystem.ShitShooter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.SmartJoystick;

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
  public static ShitShooter shooter;
  //public static Shooter shooter;
  Compressor compressor;
  
  //autonomous
  private static String autoMode;
  private static AutoRoutine auto;
  private static HashMap<String, AutoRoutine> autoMap;

  //joysticks
  private SmartJoystick left;
  private SmartJoystick right;
	
  //Looper
  private static boolean loopsStarted;
  private static Looper enabledLooper;
	
	
  @Override
  public void robotInit() {
		
    //joysticks
    left = new SmartJoystick(0 , 0.1 , 0.1);
    right = new SmartJoystick(1 , 0.1 , 0.1);
		
    //autonomous
    autoMap = new HashMap<String , AutoRoutine>();
    autoMap.put("Nothing", new DoNothing());
    autoMap.put("Hopper", new OnlyHopper());
    autoMap.put("Five Drive", new FivePointBoi());
    autoMap.put("Front Gear", new FrontGearDrop());
    autoMap.put("Ten Balls", new TenPointBalls(true));
		
    autoMode = "Five Drive";
    auto = autoMap.get(autoMode);
		
    //subsystems
    intake = Intake.getIntakeInstance();
    climb = Climber.getClimberInstance();    
    //shooter = Shooter.getShooterInstance();
    shooter = new ShitShooter();
    dt = DriveTrain.getDriveInstance(left, right);
    compressor = new Compressor();
    
    //looper
    loopsStarted = false;
    enabledLooper = new Looper(10);
    enabledLooper.register(dt.getDriveLoop());
    enabledLooper.register(climb.getClimbLoop());
    enabledLooper.register(intake.getIntakeLoop());
    enabledLooper.register(shooter.getShootLoop());
    //enabledLooper.register(shooter.getShootLoop());

    //this loop always runs , no matter what, it never stops , NEVER
}

  @Override
  public void disabledInit() {
    if(loopsStarted) {
      System.out.println("Stopping loops");
      enabledLooper.onEnd();
      loopsStarted = false;
    }
    compressor.start();
    
  }
	
  @Override
  public void autonomousInit() {
    if(!loopsStarted) {
      System.out.println("Starting loops");
      enabledLooper.startLoops();
      loopsStarted = true;
    }		
    auto.run();
  }

  /**
   * This function is called periodically during autonomous
   */
  @Override
  public void autonomousPeriodic() {
		
  }

	
  public void teleopInit() {
    //start the loops if they have not already been started
    if(!loopsStarted) {
      System.out.println("Starting loops");
      enabledLooper.startLoops();
      loopsStarted = true;
    }
        

    dt.setTeleop();
  }
  /**
   * This function is called periodically during operator control
   */
  @Override
  public void teleopPeriodic() {
    dt.setTeleop();

    //big succ for little buck
    if(left.getRawButton(1)) {
      intake.intakeBalls();
    } else if(left.getRawButton(2)) {
      intake.outtakeBalls();
    } else {
      intake.stopIntake();
    }
    if(right.getRawButton(1)) {
      
      shooter.setSetpoint(SmartDashboard.getNumber("DB/Slider 0" , 1));
    } else {
      shooter.setSetpoint(0);
    }
    
    if(right.getFallingEdge(5)) {
      dt.shift(true);
    } else if(right.getFallingEdge(3)) {
      dt.shift(false);
    }
    
  }
  
  /**
   * This function is called periodically during test mode
   */
  @Override
  public void testPeriodic() {
  }
}

