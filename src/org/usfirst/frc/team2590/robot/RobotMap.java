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
	
	
	//control variables 
	public static final double ShooterkP = 0;
	public static final double ShooterkD = 0;
	public static final double ShooterkF = 0;

	

}
