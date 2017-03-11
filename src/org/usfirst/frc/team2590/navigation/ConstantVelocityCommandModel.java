package org.usfirst.frc.team2590.navigation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ConstantVelocityCommandModel {

  private double accelCont;
  private double decelCont;

  private double lastPos;
  private double currentPos;

  private double lastVelocity;
  private double currentVelocity;

  private double targetVelocity;
  private double displacementStp;

  public ConstantVelocityCommandModel(double accelCont , double decelCont) {

    this.lastPos = 0;
    this.currentPos = 0;
    this.lastVelocity = 0;
    this.targetVelocity = 0;
    this.currentVelocity = 0;
    this.displacementStp = 0;

    this.accelCont = accelCont;
    this.decelCont = decelCont;

  }


  public void setSetpoint(double displacement , double velocity) {
    targetVelocity = velocity;
    displacementStp = displacement;
  }

  public void reset() {
    this.lastPos = 0;
    this.currentPos = 0;
    this.lastVelocity = 0;
    this.targetVelocity = 0;
    this.currentVelocity = 0;
    this.displacementStp = 0;

  }

  public void calculate(double processVariable , double myVelocity , double dT) {
    double error = displacementStp - processVariable;

    //acceleration
    if(error > 0.09*myVelocity) {
      currentVelocity = accelCont*lastVelocity + (1.0-accelCont)*targetVelocity;

    } else {
      //deceleration
      System.out.println("DECEL");
      currentVelocity = lastVelocity*decelCont;
    }

    currentPos = lastPos + (dT*0.5)*(currentVelocity+lastVelocity);

    lastPos = currentPos;
    lastVelocity = currentVelocity;

    SmartDashboard.putNumber("Commanded Position " , currentPos);
    System.out.printf("Velocity %g Position %g ", currentVelocity , currentPos  );
    SmartDashboard.putNumber("Commanded Velocity " , currentVelocity);

  }

  public double getCurrentPosition() {
    return currentPos;
  }

  public double getCurrentVelocity() {
    return currentVelocity;
  }

}
