package subsystem;

import org.usfirst.frc.team2590.controllers.DrivePosHeading;
import org.usfirst.frc.team2590.controllers.PositionVelocityController;
import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import autonomousCommands.TimeOut;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain implements RobotMap { 
	

   //System States
   private enum driveState {
      LOCKED , OPEN_LOOP , PATH_FOLLOWING , TURNING , DEAD_RECKONING , DRIVEATVEL
   };
   private driveState drive = driveState.LOCKED;
	
   //drive base overrides 
   private double leftPower; 
   private double rightPower;
   
   //shifting variables 
   private boolean shiftHigh = true;
   private double lastShiftError = 0;
   
   //turning variable
   private boolean firstRun = true;
   
   //Kepsilon
   private static final double KEPSILON = 1E-6;
   
   //Highest speed you can go in low gear
   private static final double HIGH_SHIFT = 12.0;

   //wheel diameter , dont be an idiot
   private static final double WHEEL_DIAM = 4.0;
   
   //number to divide encoder by to get desired units, default is inches (1 = inches 12 = feet 40 = metres)
   private static final double CONVERSION_FACT = 12.0;
  
   //Joysticks
   private Joystick left;
   private Joystick right;
	
   //Motor Controllers
   private Victor leftMotor;
   private Victor rightMotor;	
   private RobotDrive driver;
   
   //solenoids 
   private Solenoid leftShift;
   private Solenoid rightShift;
   
   //sensors
   private ADXRS450_Gyro gyro;
   private Encoder leftEncoder;
   private Encoder rightEncoder;

   //controllers

   
   private DrivePosHeading leftPosCont;
   private DrivePosHeading rightPosCont;

   private PositionVelocityController turnController;
   
   //time out 
   TimeOut timer;
   
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
	
	  //shifting solenoids
	  leftShift = new Solenoid(leftShiftSol);
	  rightShift = new Solenoid(rightShiftSol);
	  
	  //sensors 
	  gyro = new ADXRS450_Gyro();
	  leftEncoder = new Encoder(leftEncoderA , leftEncoderB);
	  rightEncoder = new Encoder(rightEncoderA , rightEncoderB);
	  leftEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / CONVERSION_FACT));
	  rightEncoder.setDistancePerPulse(1.0/360.0 * ((WHEEL_DIAM * Math.PI) / CONVERSION_FACT));

	  
	  leftPosCont = new DrivePosHeading(DrVelkP, DrVelkD, DrVelkF , DrVelkT , 8 , gyro , false);
	  rightPosCont = new DrivePosHeading(DrVelkP, DrVelkD, DrVelkF , DrVelkT , 8 , gyro , true);
	  
	  turnController = new PositionVelocityController(turnkP, turnkD, turnkF , 8);

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
		   handleShifting(true);
		   driver.arcadeDrive(left.getY(), -right.getX());
		   break;
		   
		  case PATH_FOLLOWING : //follow a path
			break;
			
	      case TURNING : //turn
	    	  leftMotor.set(turnController.calculate(gyro.getAngle()));
	    	  rightMotor.set(-turnController.calculate(gyro.getAngle()));
			break;
			
	      case DEAD_RECKONING :
	    	  driver.tankDrive(leftPower, rightPower);
	    	  break;
	    	  
	      case DRIVEATVEL :
	    	  driver.tankDrive(leftPosCont.calculate(leftEncoder.getRate()), 
	    			  rightPosCont.calculate(rightEncoder.getRate()));
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
	
	/**
	 * Lock the drive base , it will not move
	 */
	public void lock() {
		drive = driveState.LOCKED;
	}
	
	/**
	 * Drives at the given speeds
	 * @param leftSide speed to drive left side
	 * @param rightSide speed to drive right side
	 */
	public void deadReckon(double leftSide , double rightSide) {
		drive = driveState.DEAD_RECKONING;
		leftPower = leftSide;
		rightPower = rightSide;
	}
	
	/**
	 * Velocity setpoint
	 * @param leftSide : left side setpoint
	 * @param rightSide : right side setpoint
	 */
	public void setVelocity(double leftSide , double rightSide  , double heading) {
		drive = driveState.DRIVEATVEL;
		leftPosCont.setSetpoint(leftSide , heading);
		rightPosCont.setSetpoint(rightSide , heading);
	}
		
	/**
	 * Starts open loop driving
	 */
	public void driveOpenLoop() {
	  drive = driveState.OPEN_LOOP;
	}
	
	/**
	 * This all assumes that high gear is when the solenoid is true
	 * @param isAuto
	 */
	public void handleShifting(boolean isAuto) {
		
		if(isAuto) {
			double shiftError = HIGH_SHIFT - ((leftEncoder.getRate() + rightEncoder.getRate()) / 2);
			
			if((lastShiftError*shiftError) < KEPSILON) { //change in sign
				boolean forceShift = shiftError < KEPSILON; //meaning we are higher than high shift / need to go high
				leftShift.set(forceShift);
				rightShift.set(forceShift);
			}
			
			lastShiftError = shiftError;
		} else {
			leftShift.set(shiftHigh);
			rightShift.set(shiftHigh);
		}
		
	}
	
	/**
	 * Force the shifter to a position
	 * @param high : set it to high?
	 */
	public void setShift(boolean high) {
		shiftHigh = high;
	}

	public void zeroGyro() {
		gyro.reset();
	}
	
	/**
	 * Turn to an angle 3 second time out
	 * @param setpoint : angle to turn to
	 */
	public void turnToPoint(double setpoint) {
		double gyroError = setpoint - gyro.getAngle();		
		turnController.setSetpoint(setpoint);
		
		if(Math.abs(gyroError-setpoint) < 0.5) {
			timer = new TimeOut(3000);
			firstRun = false;
		}
		
		if(Math.abs(gyroError) > 0.5 && !timer.isTimedOut()) {
			drive = driveState.TURNING;
		} else {
			drive = driveState.LOCKED;
		}
	}
	
	public boolean turnIsDone() {
		return turnController.done();
	}
	
	public boolean driveIsDone() {
		return leftPosCont.done();
	}
}
