package controllers;

import edu.wpi.first.wpilibj.Timer;

public class TakeBackHalf {
	

	private double h0;
	private double dV;
	private double out;
	private double gain;
	private double tSpeed;
	private double setpoint;
	private double lastTime;
	private double lastError;
	private double lastOutput = 0;
	
	/**
	 * Creates a new TakeBackHalf controller
	 * 
	 * @param gain The amount of motor output to increase per millisecond per error
	 */
	public TakeBackHalf(double gain, double topSpeed) {		
		h0 = 0;
		out = 1;
		setpoint = 0;
		lastTime = 0;
		lastError = 0;
		this.gain = gain;
		tSpeed = topSpeed;
	}
	
	/**
	 * Changes the target point to be setpoint
	 * 
	 * @param setpoint Where the mechanism should go to
	 */
	public void set(double setpoint) {
		double steady = setpoint/tSpeed;
		if (setpoint < (this.setpoint - 100.0/60)) {
			out = 0;
			h0 = 0;
		} else if (setpoint > (this.setpoint + 100.0/60)) {
			h0 = 2*steady - 1.0;
			out = 1.0;
		}
		
		this.setpoint = setpoint;
	}
	
	
	
	/**
	 * Returns the output for the mechanism (should be called periodically)
	 * 
	 * @param proccessVar The current location of the mechanism
	 * @return The output to the motor controlling the mechanism
	 */
	public double getOutput(double proccessVar) {
				
		double error = setpoint - proccessVar;
		double time = Timer.getFPGATimestamp() * 1000;
		double dt = time - lastTime;
		lastTime = time;
		
		out += gain*error*dt;
		
		if (out > 1) out = 1;
		else if (out < 0) out = 0;
		
		if ((lastError*error) < 0) { 
			out = h0 = 0.5 * (out+h0);
		}
		
		lastError = error;
		
		if (setpoint == 0) {
			return 0;
		} else {
			if (out - lastOutput > (dt * dV)) {
				out = lastOutput + (dt * dV);
			}
			if (out - lastOutput < -(dt * dV)) {
				out = lastOutput - (dt * dV);
			}
			
			lastOutput = out;
			return out;
		}	
	}
	
}
