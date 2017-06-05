package util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Handles all the robots vision
 * @author James Aikins
 *
 */
public class Vision {

  //singleton
  private static Vision vision = null;
  public static synchronized Vision getVisionInstance() {
    if(vision == null) {
      vision = new Vision();
    }
    return vision;
  }

  static NetworkTable contours = NetworkTable.getTable("GRIP/myContoursReport");
  
  //field of view
  private static final double fov = 47.0;
  private static final double yfov = fov*0.78;
  private static final double camHeight = 19.5;
  private static final double cameraAngle = 23; // center y pixel
  private static final double targetHeight = 88;
  private static final double imageWidth = 320.0;
  private static final double imageHeight = 240.0;
  private static final double cx = imageWidth/2.0; // center x pixel
  private static final double cy = imageHeight/2.0; // center y pixel 
  private static final double xdpp = fov/imageWidth; // degrees per pixel in the x direction
  private static final double ydpp = yfov/imageHeight; // degrees per pixel in the y direction\
 
  private static double targetsSeen = 0;
  private static Target targetStrip = new Target();
  private static double[] defaultVal = new double[0];

  private void update() {
    try {
      
      double[] areas = contours.getNumberArray("area" ,defaultVal);
      double[] xVals = contours.getNumberArray("centerX" ,defaultVal);
      double[] yVals = contours.getNumberArray("centerY" ,defaultVal);
      targetsSeen = contours.getNumberArray("centerX" , defaultVal).length;
      
      /*
       * Locates largest contour
       */
      if(targetsSeen >= 2){
    	    int val = (areas[0] > areas[1]) ? 0 : 1;
    	    targetStrip.setCoord(xVals[val], yVals[val]);
      }

    } catch(Exception e) {

    }
  }

 

  /**
   * The horizontal angle to the target
   * @return
   */
  public double hAngleToTarget() {
    try {
      update();
      return((targetStrip.getX()-cx)*xdpp);
    }	catch(Exception e) {
      DriverStation.reportError("Target Not Found!", false);
      return 0;
    }
  }

  /**
   * The vertical angle to the target
   * @return
   */
  public double vAngleToTarget() {
    try{
      update();
      return(((targetStrip.getY()-cy)*ydpp)+cameraAngle);
    } catch(Exception e) {
      DriverStation.reportError("Target Not Found", false);
      return 2;
    }
  }
  
  /**
   * The amount of targets the robot sees
   * @return
   */
  public double targetFound() {
    update();
    return targetsSeen;
  }

  /**
   * The x distance to the target
   * @return
   */
  public double xDistanceToTarget() {
    update();
    System.out.println("vangle " + vAngleToTarget());
    return (targetHeight-camHeight)/Math.tan(Math.toRadians(vAngleToTarget()));
  }

  /**
   * The y distance to the target
   * @return
   */
  public double yDistanceToTarget() {
    update();
    return xDistanceToTarget()*Math.tan(Math.toRadians(hAngleToTarget()));
  }

  /**
   * The angle to turn to center the target
   * @return
   */
  public double angleToTarget() {
    try {
      update();
      return hAngleToTarget();
    }  catch(Exception e)  {
      DriverStation.reportError("VISION BROKEN ANGLE TO TARGET", false);
      return 0.0;
    }
  }
}
