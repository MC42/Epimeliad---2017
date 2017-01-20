package subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain implements RobotMap { 
	
	//System States
	private enum driveState {
		LOCKED , OPEN_LOOP , PATH_FOLLOWING , TURNING 
	};
	private driveState drive = driveState.LOCKED;
	
	//Joysticks
	private Joystick left_;
	private Joystick right_;
	
	//Motor Controllers
	private Victor leftMotor;
	private Victor rightMotor;	
	private RobotDrive driver;

	public DriveTrain(Joystick left , Joystick right) {	
		
		//Joysticks
		left_ = left;
		right_ = right;
		
		//Motor Controllers
		leftMotor = new Victor(leftPWM);
		rightMotor = new Victor(rightPWM);
		driver = new RobotDrive(leftMotor, rightMotor);
		
	}
	
	
	private Loop loop_ = new Loop() {

		@Override
		public void onStart() {
			
		}

		@Override
		public void loop() {
			
			switch(drive) {
				case LOCKED : //do nothing
					break;
				case OPEN_LOOP : //drive from input
					driver.arcadeDrive(left_.getY(), right_.getX());
					break;
				case PATH_FOLLOWING : //follow a path
					break;
				case TURNING : //turn
					break;
				default : DriverStation.reportWarning("Drive state unknown", false);
			}
			
		}

		@Override
		public void onEnd() {
			
		}
		
	};
	
	public void startOpenLoop() {
		drive = driveState.OPEN_LOOP;
	}
	
	public Loop getDriveLoop() {
		return loop_;
	}

	
}
