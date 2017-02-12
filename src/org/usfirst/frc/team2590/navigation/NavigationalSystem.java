package org.usfirst.frc.team2590.navigation;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Navigational system which
 * Calculates the robots position on a
 * X Y plane (planes fly ... plane game confirmed)
 * @author Connor_Hofenbitzer
 *
 */
public class NavigationalSystem extends Thread {

  //gyro and sensor readings
  private double lastGyro;
  private double lastReadingleft;
  private double lastReadingright;

  //position based stuff
  ADXRS450_Gyro gyro;
  private Point pose;
  private Encoder leftEncoder;
  private Encoder rightEncoder;


  public NavigationalSystem(Encoder left , Encoder right , ADXRS450_Gyro gyro) {

    //sensor readings
    lastGyro = 0;
    lastReadingleft = 0;
    lastReadingright = 0;

    //position based stuff
    this.gyro = gyro;
    leftEncoder = left;
    rightEncoder  = right;
    pose = new Point(0, 0, 0);

    //start the thread
    this.start();

  }

  @Override
  public void run() {
    while(true) {

      //get all encoders and gyro positions
      double currEncoderleft = leftEncoder.getDistance();
      double currEncoderright = rightEncoder.getDistance();
      double currGyro = Math.toRadians(-gyro.getAngle());

      //get the delta between the last reading and now
      double dEncoderleft = currEncoderleft - lastReadingleft;
      double dEncoderright = currEncoderright - lastReadingright;
      double dTheta = currGyro - lastGyro;

      //play with the point to calculate it on a x y field
      Point arcPoint = Point.fromVelocity((dEncoderleft + dEncoderright)/2, dTheta);
      pose = pose.translateBy(arcPoint);

      //save all values
      lastGyro = currGyro;
      lastReadingleft = currEncoderleft;
      lastReadingright = currEncoderright;

      try {
        Thread.sleep(200);
      } catch(Exception e) {

      }
    }
  }

  public Point getCurrentPoint() {
    return pose;
  }
}
