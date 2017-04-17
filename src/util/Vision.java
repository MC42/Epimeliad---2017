package util;

import util.Target;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {

  private static Vision vision = null;
  public static synchronized Vision getVisionInstance() {
    if(vision == null) {
      vision = new Vision();
    }
    return vision;
  }

  static NetworkTable contours = NetworkTable.getTable("GRIP/myContoursReport");
  
  //field of view
  private static final double cameraAngle = 23;  
  private static final double fov = 47.0; 
  private static final double yfov = fov*0.78;
  private static final double camHeight = 19.5;
  private static final double imageWidth = 320.0;
  private static final double imageHeight = 240.0;
  private static final double targetHeight = 88;
  private static final double cx = imageWidth/2.0; // center x pixel
  private static final double cy = imageHeight/2.0; // center y pixel 
  private static final double xdpp = fov/imageWidth; // degrees per pixel in the x direction
  private static final double ydpp = yfov/imageHeight; // degrees per pixel in the y direction\
 
  
  private static Target target1 = new Target();
  private static Target target2 = new Target();
  private static Target targetStrip = new Target();
  private static double[] defaultVal = new double[0];
  private static double targetsSeen = 0;
  public Vision() {
    
  }
  
  private void update() {
    try {
      double[] xVals = contours.getNumberArray("centerX" ,defaultVal);
      double[] yVals = contours.getNumberArray("centerY" ,defaultVal);
      double[] areas = contours.getNumberArray("area" ,defaultVal);
      targetsSeen = contours.getNumberArray("centerX" , defaultVal).length;
      
      /*
       * Locates largest contour
       */
      if(targetsSeen>=2){
    	  if(areas[0]>areas[1]){
    		  targetStrip.setCoord(xVals[0], yVals[0]);
    	  } else{
    		  targetStrip.setCoord(xVals[1], yVals[1]);
    	  }
      }
     
      
    } catch(Exception e) {

    }
  }

 

  public double hAngleToTarget() {
    try {
      update();
      return((targetStrip.getX()-cx)*xdpp);
    }	catch(Exception e) {
      DriverStation.reportError("Target Not Found!", false);
      return 0;
    }
  }

  public double vAngleToTarget() {
    try{
      update();
      //System.out.println("bound b " + targetStrip.getY());
      return(((targetStrip.getY()-cy)*ydpp)+cameraAngle);
    } catch(Exception e) {
      DriverStation.reportError("Target Not Found", false);
      return 2;
    }
  }
  
  public double targetFound() {
    update();
    return targetsSeen;
  }

  public double xDistanceToTarget() {
    update();
    System.out.println("vangle " + vAngleToTarget());
    return (targetHeight-camHeight)/Math.tan(Math.toRadians(vAngleToTarget()));
  }

  public double yDistanceToTarget() {
    update();
    return xDistanceToTarget()*Math.tan(Math.toRadians(hAngleToTarget()));
  }
  
  public double interpolateSpeed(double x) {
    return (0.22*(x*x)) - (36.666*x) + 4800; // equation goes here
  }

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