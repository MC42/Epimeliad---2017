package org.usfirst.frc.team2590.robot;

public interface RobotMap {

	//motors
	public static final int LEFTMOTORPWM = 0;
	public static final int RIGHTMOTORPWM = 1;
	public static final int CLIMBMOTORPWM = 2;
	public static final int PULLYMOTORPWM = 4;
	public static final int INTAKEMOTORPWM = 3;

	public static final int SHOOTERMASTERID = 0;
      public static final int SHOOTERSLAVEID  = 1;

	//sol
	public static final int INTAKE_SOLENOID = 0;
	
	//encoders
	public static final int LEFTENCODERA = 0;
	public static final int LEFTENCODERB = 1;
	public static final int RIGHTENCODERA = 2;
	public static final int RIGHTENCODERB = 3;
	
	//Shooter 
	public static final double SHOOTERKP = 0.0;
	public static final double SHOOTERKI = 0.0;
	public static final double SHOOTERKD = 0.0;
	public static final double SHOOTERKF = 0.0;
	public static final double SHOOTERKA = 0.0;

	//drive straight
	public static final double MAXACC = 6;
	public static final double VELFF = 0.13; //velocity feed forward
	
	//pure pursuit
	public static final double PUREKV = 0.2;
	public static final double LOOKAHEAD = 1.5; //in feet 3 //1.5
	public static final double DRIVEBASE = 2.333;
	
	//adaptive controller
	public static final double DRIVETURNCOMP = 0.01; //.025
}
