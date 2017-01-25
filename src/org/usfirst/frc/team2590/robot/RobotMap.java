package org.usfirst.frc.team2590.robot;

public interface RobotMap {
	public static final int leftPWM = 0;
	public static final int climbPWM = 5;
	public static final int rightPWM = 1;
	public static final int intakePWM = 4;
	public static final int shooterPWM = 3;
	public static final int feederPWM = 9;
	
	//encoders
	public static final int shooterEncA = 0;
	public static final int shooterEncB = 1;
	
	public static final int leftEncoderA = 2;
	public static final int leftEncoderB = 3;
	
	public static final int rightEncoderA = 4;
	public static final int rightEncoderB = 5;
	
	
	//solenoids
	public static final int leftShiftSol = 0;
	public static final int rightShiftSol = 1;
	
	//***********************************\\
	//**********CONTROL VARIABLES********\\
	//***********************************\\
	
	//shooter
	public static final double ShooterkP = 0;
	public static final double ShooterkD = 0;
	public static final double ShooterkF = 0;

	//drive vel controller
	public static final double DrVelkP = 0;
	public static final double DrVelkD = 0;
	public static final double DrVelkF = 0;
	public static final double DrVelkT = 0;

	
	//turn controller
	public static final double turnkP = 0;
	public static final double turnkD = 0;
	public static final double turnkF = 0;

	

}
