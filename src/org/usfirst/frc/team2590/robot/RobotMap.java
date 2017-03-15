package org.usfirst.frc.team2590.robot;

public interface RobotMap {
  /*
   * PORT NUMBERS
   */

  //motors
  public static final int LEFTMOTORPWM = 1;
  public static final int RIGHTMOTORPWM = 0;
  public static final int CLIMBMOTORPWM = 4;
  public static final int FEEDERMOTORPWM = 2;
  public static final int INTAKEMOTORPWM = 3;
  public static final int GEARINTAKEMOTORPWM = 5;

  public static final int SHOOTERSLAVEID = 0 ; //1 for real
  public static final int SHOOTERMASTERID = 1; //0 for real

  //sol
  public static final int INTAKE_SOLENOID = 1;
  public static final int SHIFTER_SOLENOID = 0;
  public static final int GEAR_HOLDER_SOLENOID = 4;

  //encoders
  public static final int LEFTENCODERA = 2;
  public static final int LEFTENCODERB = 3;
  public static final int RIGHTENCODERA = 0;
  public static final int RIGHTENCODERB = 1;

  //real shooter
  public static final double SHOOTERKP = 0.075; //0.0429 0.055
  public static final double SHOOTERKI = 0.0;
  public static final double SHOOTERKD = 0.0;
  public static final double SHOOTERKF = 0.08; //0.083
  public static final double SHOOTERKA = 0.0;

  //drive straight
  public static final double MAXACC = 6; //6
  public static final double VELFF = 0.13; //velocity feed forward //0.13 //0.05
  public static final double SMALLFF = 0.3;
  public static final double ROBOTLENGTH = 18;
  public static final double ROBOTWIDTH = (29/12);

  //pure pursuit
  public static final double PUREKV = 0.2;
  public static final double CRUISEVEL = 12;
  public static final double LOOKAHEAD = 0.5; //in feet 3 //1.5 //1
  public static final double DRIVEBASE = 2.333;
  public static final double DRIVETURNCOMP = 0.0075; //.015 0.0075



}
