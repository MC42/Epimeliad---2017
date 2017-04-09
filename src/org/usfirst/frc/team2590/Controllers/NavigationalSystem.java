package org.usfirst.frc.team2590.Controllers;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Navigational system which
 * Calculates the robots position on a
 * X Y plane 
 * @author Connor_Hofenbitzer
 *
 */
public class NavigationalSystem extends Thread {

  //gyro and sensor readings
  private double lastGyro;
  private boolean inverted;
  private double lastReadingleft;
  private double lastReadingright;


  //position based stuff
  ADXRS450_Gyro gyro;
  private Point pose;
  private Encoder leftEncoder;
  private Encoder rightEncoder;

  //thread sleep rate
  private static final long SLEEPRATE = 10;

  public NavigationalSystem(Encoder left , Encoder right , ADXRS450_Gyro gyro) {

    //sensor readings
    lastGyro = 0;
    inverted = false;
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
      double currEncoderleft = leftEncoder.getDistance()*1.054;
      double currEncoderright = rightEncoder.getDistance()*1.054;
      double currGyro = Math.toRadians(gyro.getAngle() * (inverted?1:-1));

      //get the delta between the last reading and now
      double dEncoderleft = currEncoderleft - lastReadingleft;
      double dEncoderright = currEncoderright - lastReadingright;
      double dTheta = currGyro - lastGyro;

      //translate the point to calculate it on an x y field
      Point arcPoint = Point.fromVelocity((dEncoderleft + dEncoderright)/2, dTheta);
      pose = pose.translateBy(arcPoint);
      
      //save all values
      lastGyro = currGyro;
      lastReadingleft = currEncoderleft;
      lastReadingright = currEncoderright;

      try {
        Thread.sleep(SLEEPRATE);
      } catch(Exception e) {

      }
    }
  }
  
  public Point getCurrentPoint() {
    return pose;
  }

  public void reset() {
    gyro.reset();
    leftEncoder.reset();
    rightEncoder.reset();
    
    lastGyro = 0;
    lastGyro = 0;
    inverted = false;
    
    lastReadingleft = 0;
    lastReadingright = 0;
    pose = new Point(0, 0, 0);
  }

}
