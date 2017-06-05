package util;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

/**
 * I was honestly expecting to do a lot more with drivetrain control this year
 * @author Connor_Hofenbitzer
 *
 */
public class NemesisDrive {

  Victor left;
  Victor right;

  Encoder leftE;
  Encoder rightE;
  ADXRS450_Gyro gyro;

  RobotDrive robotDrive;

  public NemesisDrive(ADXRS450_Gyro gyro ,Victor left , Victor right) {
    this.gyro = gyro;
    this.left = left;
    this.right = right;
    robotDrive = new RobotDrive(left , right);
  }

  public void openLoopDrive(double move , double turn) {
    robotDrive.arcadeDrive(move, turn);
  }

  public void tankDrive(double leftM , double rightM) {
    robotDrive.tankDrive(leftM, rightM);
  }


}

