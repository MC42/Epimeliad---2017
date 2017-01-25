
package org.usfirst.frc.team2590.robot;

import org.usfirst.frc.team2590.looper.Looper;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import screen.LED;
import screen.Profile;
import screen.ProfileHandler;
import subsystem.DriveTrain;
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
	public static LED display;
	public static DriveTrain dt;
	public static Shooter shooter;
	
	//joystick
	private static Joystick left;
	private static Joystick right;

	private static Looper enabledLooper;

	//profiles 
	public static Profile autoProfile;
	public static Profile teleopProfile;
	public static Profile disabledProfile;
	public static ProfileHandler profiles;

	@Override
	public void robotInit() {
		
		
		//joysticks
		left = new Joystick(0);
		right = new Joystick(1);
		
		display = new LED();
		shooter = new Shooter();
		dt = new DriveTrain(left, right);
	
		//looper
		enabledLooper = new Looper(1);
		enabledLooper.register(shooter.getShooterLoop());
		enabledLooper.register(dt.getDriveLoop());
		
		autoProfile = new Profile("Auto", "Auto mode is now Enabled!");
		disabledProfile = new Profile("Disabled", "I am disabled , safe to touch :)");
		teleopProfile = new Profile("Teleop", "Teleop mode is now Enabled!" , "Shooter " + shooter.getEncoderVal());
		profiles = new ProfileHandler(autoProfile , disabledProfile , teleopProfile);
		
	}

	@Override
	public void disabledInit() {
		enabledLooper.onEnd();
		profiles.setProfile("Disabled");
	}
	@Override
	public void autonomousInit() {
		profiles.setProfile("Auto");
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		profiles.setProfile("Teleop");
		enabledLooper.startLoops();
		dt.driveOpenLoop();
	}

	@Override
	public void teleopPeriodic() {
		shooter.setSetpoint(SmartDashboard.getNumber("DB/Slider 0", 3100));
				
		if(right.getRawButton(1)) {
		  shooter.takeShot();
		} else {
		  shooter.stopShot();
		}

	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
