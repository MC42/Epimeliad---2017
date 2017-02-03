package util;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class NemesisDrive {

  Victor left;
  Victor right;

  double turnComp;
  double driveComp;
  double angleError;
  double velocityError;

  Encoder leftE;
  Encoder rightE;
  ADXRS450_Gyro gyro;

  RobotDrive robotDrive;

  public NemesisDrive(ADXRS450_Gyro gyro ,Victor left , Victor right , double turnComp) {
    this.gyro = gyro;
    this.left = left;
    this.right = right;
    this.turnComp = turnComp;

    robotDrive = new RobotDrive(left , right);
    angleError = 0;
    velocityError = 0;
  }

  public void correctiveDrive(double move , double turn , double setPointAngle, double turnBand) {
    if(Math.abs(turn) < turnBand) {
      angleError = setPointAngle - gyro.getAngle();
      turn = angleError*turnComp;
    } else {
      gyro.reset();
    }
    robotDrive.arcadeDrive(move , turn);
  }

  public void tankDrive(double leftM , double rightM) {
    robotDrive.tankDrive(leftM, rightM);
  }


}

