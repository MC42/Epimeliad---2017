package util;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * @author Connor_Hofenbitzer
 *
 */
public class NemesisDrive {

  private Victor left;
  private Victor right;
  private Encoder leftE;
  private Encoder rightE;
  private double feedBack;
  private double wheelSize;
  
  public NemesisDrive(Victor left , Victor right , Encoder leftE , Encoder rightE , double feedBack , double wheelSize) {
    this.left = left;
    this.right = right;
    this.leftE = leftE;
    this.rightE = rightE;
    this.feedBack = feedBack;
    this.wheelSize = wheelSize;
  }

  public void setVelocitySetpoints(double leftVelocity , double rightVelocity ) {
    double leftError = fpstoRPM(leftVelocity) - (leftE.getRate());
    double rightError = fpstoRPM(rightVelocity) - (rightE.getRate());
    System.out.println("left " + fpstoRPM(leftVelocity) + " " + (leftE.getRate()*60));
    SmartDashboard.putNumber("left encoder error ", leftError);
    SmartDashboard.putNumber("right encoder error ", rightError);
    left.set(leftError*feedBack);
    right.set(rightError*feedBack);
  }

  public void tankDrive(double leftP , double rightP) {
    left.set(leftP);
    right.set(rightP);
  }
  
  private double feetToRotations(double feet) {
    return feet / (wheelSize * Math.PI);
  }

  private double fpstoRPM(double fps) {
    return feetToRotations(fps) * 60;
}


}

