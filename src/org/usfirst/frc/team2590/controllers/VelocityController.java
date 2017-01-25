package org.usfirst.frc.team2590.controllers;

import edu.wpi.first.wpilibj.Timer;

public class VelocityController extends Controller {

	//gains
	private double kP;
	private double kD;
	private double kF;
	
	//holders
	private double lastTime;
	private double lastError;
	private double lastSetpoint;
	
	//setpoints
	private double velSetpoint;
	private double rpmSetpoint;

	public VelocityController(double kP , double kD , double kF) {
		this.kP = kP;
		this.kD = kD;
		this.kF = kF;
		
		lastTime = 0;
		lastError = 0;
		velSetpoint = 0;
		rpmSetpoint = 0;
		lastSetpoint = 0;
	}
	
	@Override
	public void setSetpoint(double rpm) {
		rpmSetpoint = rpm;
		velSetpoint = rpm;
	}
	
	public void setSetpoint(double vel , double dist) {
		rpmSetpoint = dist;
		
	}
	
	public void updateGains(double kp , double kd , double kf) {
		this.kF = kf;
		this.kD = kd;
		this.kP = kp;
	}
	
	@Override
	public double calculate(double processVariable) {
		
		//store needed variables
		double currentTime = Timer.getFPGATimestamp()*1000;
		double currentError = rpmSetpoint - processVariable;
		
		//if the setpoint changes
		if(lastSetpoint != rpmSetpoint) {
			lastError = currentError;
			lastTime  = currentTime;
		}
		
		//delta calculations
		double dT = currentTime  - lastTime;
		double dE = currentError - lastError;
		
		//D motor calculations 
		double newD = kD * (dE / (dT - velSetpoint));
		
		//output calculation
		double output = (kP*currentError) + newD + (kF*velSetpoint);
		
		//set all lasts
		lastTime = currentTime;
		lastError = currentError;
		
		return output;
	}
}
