package controllers;

import edu.wpi.first.wpilibj.Timer;

public class PIDF {

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

	public PIDF(double kP , double kD , double kF) {
		this.kP = kP;
		this.kD = kD;
		this.kF = kF;
		
		lastTime = 0;
		lastError = 0;
		velSetpoint = 0;
		rpmSetpoint = 0;
		lastSetpoint = 0;
	}
	
	public void setSetpoint(double rpm) {
		rpmSetpoint = rpm;
		velSetpoint = rpm;
	}
	
	public void updateGains(double kp , double kd , double kf) {
		this.kF = kf;
		this.kD = kd;
		this.kP = kp;
	}
	
	public double calculate(double processVariable) {
		double currentTime = Timer.getFPGATimestamp()*1000;
		double currentError = rpmSetpoint - processVariable;
		
		if(lastSetpoint != rpmSetpoint) {
			lastError = currentError;
			lastTime  = currentTime;
		}
		
		double dT = currentTime  - lastTime;
		double dE = currentError - lastError;
		double newD = kD * (dE / (dT - velSetpoint));
		double output = (kP*currentError) + newD + (kF*velSetpoint);
		
		lastTime = currentTime;
		lastError = currentError;
		
		return output;
	}
}
