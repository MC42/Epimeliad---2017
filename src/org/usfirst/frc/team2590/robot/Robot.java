
package org.usfirst.frc.team2590.robot;

import org.usfirst.frc.team2590.looper.Looper;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import sensors.LED;
import subsystem.DriveTrain;
import subsystem.Intake;
import subsystem.Shooter;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	//subsystems
	public static Intake intake;
	public static DriveTrain dt;
	public static Shooter shooter;
	public static LED display;
	
	//joystick
	private static Joystick left;
	private static Joystick right;

	private static Looper enabledLooper;

	@Override
	public void robotInit() {
		
		//joysticks
		left = new Joystick(0);
		right = new Joystick(1);
		
		display = new LED();
		intake = new Intake();
		shooter = new Shooter();
		dt = new DriveTrain(left, right);

	
		//looper
		enabledLooper = new Looper(5);
		enabledLooper.register(shooter.getShooterLoop());
		enabledLooper.register(intake.getIntakeLoop());
		enabledLooper.register(dt.getDriveLoop());
	}

	@Override
	public void disabledInit() {
		enabledLooper.onEnd();

	}
	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		enabledLooper.startLoops();
		dt.driveOpenLoop();
	}

	@Override
	public void teleopPeriodic() {
		shooter.setSetpoint(SmartDashboard.getNumber("DB/Slider 0", 3100));
		
		
		if(right.getRawButton(1)) {
		  shooter.getReady();
		} else {
		  shooter.stopShot();
		}
		
		if (left.getRawButton(1)) {
		  intake.suckBalls();
		} else if (left.getRawButton(2)) {
		  intake.unSuck();
		} else {
		  intake.stopSuck();
		}
		
		display.LCDwriteCMD(display.LCD_CLEARDISPLAY);
		display.LCDwriteString("Encoder " + shooter.getEncoderVal() , 1);
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
