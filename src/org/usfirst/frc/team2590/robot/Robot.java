
package org.usfirst.frc.team2590.robot;

import org.usfirst.frc.team2590.looper.Looper;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
	private static DriveTrain dt;
	private static Shooter shooter;
	
	//joystick
	private static Joystick left;
	private static Joystick right;

	private static Looper enabledLooper;

	@Override
	public void robotInit() {
		
		//joysticks
		left = new Joystick(0);
		right = new Joystick(1);
		
		shooter = new Shooter();
		dt = new DriveTrain(left, right);
		
		//looper
		enabledLooper = new Looper(10);
		enabledLooper.register(shooter.getShooterLoop());

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
		dt.startOpenLoop();
	}

	@Override
	public void teleopPeriodic() {
		shooter.setSetpoint(3200);

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
