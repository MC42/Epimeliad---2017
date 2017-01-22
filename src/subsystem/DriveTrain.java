package subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain implements RobotMap { 
	
   //System States
   private enum driveState {
      LOCKED , OPEN_LOOP , PATH_FOLLOWING , TURNING , DEAD_RECKONING
   };
   private driveState drive = driveState.LOCKED;
	
   //drive base overrides 
   double leftPower; 
   double rightPower;
   
   //Joysticks
   private Joystick left;
   private Joystick right;
	
   //Motor Controllers
   private Victor leftMotor;
   private Victor rightMotor;	
   private RobotDrive driver;
   
   //sensors
   private ADXRS450_Gyro gyro;
   private Encoder leftEncoder;
   private Encoder rightEncoder;

   public DriveTrain(Joystick left , Joystick right) {	
	
	  //drive base overrides
	  rightPower = 0;
	  leftPower  = 0;
	  
	  //Joysticks
	  this.left = left;
	  this.right = right;
		
	  //Motor Controllers
	  leftMotor = new Victor(leftPWM);
	  rightMotor = new Victor(rightPWM);
	  driver = new RobotDrive(leftMotor, rightMotor);
	
	  //sensors 
	  gyro = new ADXRS450_Gyro();
	  leftEncoder = new Encoder(leftEncoderA , leftEncoderB);
	  rightEncoder = new Encoder(rightEncoderA , rightEncoderB);
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
		   driver.arcadeDrive(left.getY(), left.getX());
		   break;
		  case PATH_FOLLOWING : //follow a path
			break;
	      case TURNING : //turn
			break;
	      case DEAD_RECKONING :
	    	  driver.tankDrive(leftPower, rightPower);
	    	  break;
		  default : DriverStation.reportWarning("Drive state unknown", false);
		}
			
		}

		@Override
		public void onEnd() {
			
		}
		
	};
	
	public Loop getDriveLoop() {
	  return loop_;
	}
	
	public void lock() {
		drive = driveState.LOCKED;
	}
	
	public void deadReckon(double leftSide , double rightSide) {
		leftPower = leftSide;
		rightPower = rightSide;
	}
	
	public void driveOpenLoop() {
	  drive = driveState.OPEN_LOOP;
	}


	
}
