package subsystem;

import org.usfirst.frc.team2590.controllers.Controller;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class ControlledManipulator {
	
	private Controller manipulatorControl;
	private Encoder manipulatorSensor;
	private Victor manipulatorMotor;
	private double setpoint;
	private boolean isRate;
	
	public ControlledManipulator(Controller controller , Encoder sensor , Victor motor , boolean isRates) {
		manipulatorControl = controller;
		manipulatorSensor = sensor;
		manipulatorMotor = motor;
		isRate = isRates;
		setpoint = 0;
	}
	
	/**
	 *  Set the setpoint of the controller
	 */
	public void setSetpoint(double set) {
		setpoint = set;
		manipulatorControl.setSetpoint(set);
	}
	
	/**
	 * Gets the setpoint of the controller
	 * @return
	 */
	public double getSetpoint() {
		return setpoint;
	}
	
	/**
	 * Set the speed of the motor manually
	 * @param motorSpeed
	 */
	public void setMotorManual(double motorSpeed) {
		manipulatorMotor.set(motorSpeed);
	}
	
	/**
	 * Returns the output of the controller
	 * @return based off is rate boolean
	 */
	public double calculateOut() {
		return manipulatorControl.calculate(isRate?manipulatorSensor.getRate()*60 : 
												manipulatorSensor.getDistance());
	}
	
	public void setOutput() {
		manipulatorMotor.set(calculateOut());
	}
	/**
	 * Returns the sensor value
	 * @return based off of the is rate boolean
	 */
	public double getSensor() {
		return (isRate? manipulatorSensor.getRate()*60 : 
					    manipulatorSensor.getDistance());
	}
}
