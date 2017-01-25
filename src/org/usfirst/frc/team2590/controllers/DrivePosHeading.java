package org.usfirst.frc.team2590.controllers;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;

public class DrivePosHeading extends Controller{

	//gains
	private double kP;
	private double kD;
	private double kF;
	private double kT;
	private double maxAcc;
	private boolean isRight;
	//holders
	private double lastTime;
	private double lastError;
	private double lastSetpoint;
	private double currentError ;
	
	//setpoints
	private double angleSet;
	private double velSetpoint;
	private double rpmSetpoint;

	private ADXRS450_Gyro gyro;
	
	public DrivePosHeading(double kP , double kD , double kF , double kT , double maxAcceleration , ADXRS450_Gyro gyro , boolean isRight) {
		this.kP = kP;
		this.kD = kD;
		this.kF = kF;
		this.kT = kT;
		this.isRight = isRight;
		
		angleSet = 0;
		lastTime = 0;
		lastError = 0;
		velSetpoint = 0;
		rpmSetpoint = 0;
		lastSetpoint = 0;
		currentError = 0;
		this.gyro = gyro;
		maxAcc = maxAcceleration;
	}
	
	@Override
	public void setSetpoint(double dist) {
		rpmSetpoint = dist;
	}

	public void setSetpoint(double dist , double angle) {
		angleSet = angle;
		rpmSetpoint = dist;
	}
	
	@Override
	public double calculate(double processVariable) {
		
		//store needed variables
		double currentTime = Timer.getFPGATimestamp()*1000;
		
		currentError = rpmSetpoint - processVariable;
		
		//if the setpoint changes
		if(lastSetpoint != rpmSetpoint) {
			lastError = currentError;
			lastTime  = currentTime;
		}
		
		velSetpoint =  Math.sqrt(2 * maxAcc * currentError);
		
		//delta calculations
		double dT = currentTime  - lastTime;
		double dE = currentError - lastError;
		
		//D motor calculations 
		double newD = kD * (dE / (dT - velSetpoint));
		
		//output calculation
		double output = (kP*currentError) + newD + (kF*velSetpoint) + calcHeading();
		
		//set all lasts
		lastTime = currentTime;
		lastError = currentError;
		
		return output;
	}
	
	public double calcHeading() {
		double headingError = angleSet - gyro.getAngle();
		return (headingError*kT) * (isRight?-1:1);
	}
	public boolean done() {
		return Math.abs(currentError) < 0.5;
	}
}
