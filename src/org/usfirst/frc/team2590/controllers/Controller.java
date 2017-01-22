package org.usfirst.frc.team2590.controllers;

public abstract class Controller {
	public abstract void setSetpoint(double setpoint);
	public abstract double calculate(double processVar);
}
