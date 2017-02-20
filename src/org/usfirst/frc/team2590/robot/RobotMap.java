package org.usfirst.frc.team2590.robot;

public interface RobotMap {
      /*
       * PORT NUMBERS
       */

	//motors
	public static final int LEFTMOTORPWM = 1;
	public static final int RIGHTMOTORPWM = 0;
	public static final int CLIMBMOTORPWM = 4;
	public static final int PULLEYMOTORPWM = 2;
	public static final int INTAKEMOTORPWM = 3;
	//public static final int SHOOTERBASICPWM = 6;
	
      public static final int SHOOTERSLAVEID = 1; //3
	public static final int SHOOTERMASTERID = 0; //3

	//sol
	public static final int INTAKE_SOLENOID = 1;
	public static final int SHIFTER_SOLENOID = 0;
	public static final int GEAR_HOLDER_SOLENOID = 5;
	
	//encoders
	public static final int LEFTENCODERA = 2; 
	public static final int LEFTENCODERB = 3; 
	public static final int RIGHTENCODERA = 0;
	public static final int RIGHTENCODERB = 1;
	
	//Shooter
	//PBOT
	/*public static final double SHOOTERKP = 0.35; //0.05
	public static final double SHOOTERKI = 0.0;
	public static final double SHOOTERKD = 0.55;
	public static final double SHOOTERKF = 0.072;
	public static final double SHOOTERKA = 0.0; */
	
	//real shooter
	public static final double SHOOTERKP = 0.0; //0.05
      public static final double SHOOTERKI = 0.0;
      public static final double SHOOTERKD = 0.0;
      public static final double SHOOTERKF = 0.074;
      public static final double SHOOTERKA = 0.0; 

	//drive straight
	public static final double MAXACC = 4;
	public static final double VELFF = 0.13; //velocity feed forward
	
	//pure pursuit
	public static final double PUREKV = 0.2;
	public static final double LOOKAHEAD = 1.5; //in feet 3 //1.5
	public static final double DRIVEBASE = 2.333;
	public static final double DRIVETURNCOMP = 0.01; //.025
	
	//turn in place
	public static final double TURNKP = 0.01;
	public static final double TURNKI = 0.0;
	public static final double TURNKD = 0.0;

	
}
