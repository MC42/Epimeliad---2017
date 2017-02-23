package util;

import org.usfirst.frc.team2590.looper.Loop;

import util.Target;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

public class Vision {

  private static Vision vision = null;
  public static synchronized Vision getVisionInstance() {
    if(vision == null) {
      vision = new Vision();
    }
    return vision;
  }
  
 
  
  private double[] frontCamValues;
  private double[] backCamValues;
  private boolean backValuesRequested;
  private boolean frontValuesRequested;
  
  public Vision() {
    //x pos and distance?
    backValuesRequested = false;
    frontValuesRequested = false;
    backCamValues = new double[2];
    frontCamValues = new double[2];
  }
  
  	static NetworkTable contours = NetworkTable.getTable("GRIP/myContoursReport");
	
	static double imageHeight = 240.0;
	static double imageWidth = 320.0;
	static double fov = 47.0; //field of view
	static double yfov = fov*0.78;
	static double focalLength = imageWidth/(2*Math.tan(fov/2));
	static double camHeight = 19.5;
	
	
	
	
	
	
	static double[] defaultVal = new double[0];
	
	static Target target1 = new Target();
	static Target target2 = new Target();
	public static Target BoundBox = new Target();
	
	public static void update(){
		try{
			double[] xVals = contours.getNumberArray("centerX",defaultVal); 
			double[] yVals = contours.getNumberArray("centerY",defaultVal); 
		
			
			int targetsSeen = contours.getNumberArray("centerX", defaultVal).length;
			
			if(targetsSeen>=2){ // if sees more than two contours
			
				target1.setCoord(xVals[0], yVals[0]);	
				target2.setCoord(xVals[1], yVals[1]);
		
				BoundBox.setCoord((target1.getX()+target2.getX())/2, (target1.getY()+target2.getY())/2);
				System.out.println("Seeing two contours");
			}
		}
		catch(Exception e){
			
		}
	}
	
	static double cx = imageWidth/2.0;// center x pixel
	static double cy = imageHeight/2.0;// center y pixel
	static double xdpp = fov/imageWidth;// degrees per pixel in the x direction
	static double ydpp = yfov/imageHeight;// degrees per pixel in the y direction
	
	public static double hAngleToTarget(){
		try{
			return((BoundBox.getX()-cx)*xdpp);
			//return Math.atan((targetX-cx)/focalLength);
		}
		catch(Exception e){ 
			DriverStation.reportError("Target Not Found!", false);
			return 0;
		}
	}
	
	public static double vAngleToTarget(){
		try{
			return((BoundBox.getY()-cy)*ydpp);
			//return Math.atan((BoundBox.getY()-cy)/focalLength);
		}
		catch(Exception e){
			DriverStation.reportError("Target Not Found", false);
			return 2;
		}
	}
	
	static double targetHeight = 13.5;
	static double camOffset = 9;
	
	public static double xDistanceToTarget(){
		return (targetHeight-camHeight)/Math.tan(Math.toRadians(vAngleToTarget()));
	}
	
	public static double yDistanceToTarget(){
		return xDistanceToTarget()*Math.tan(Math.toRadians(hAngleToTarget()));
	}
	
	public static double angleToTarget(){
	  try {
		//double gyroHeading = Robot.driveT.getGyroHeading();
		//return Math.atan2((yDistanceToTarget()-(camOffset*Math.cos(Math.toRadians(gyroHeading)))),(xDistanceToTarget()+(camOffset*Math.sin(Math.toRadians(gyroHeading)))));
		return Math.toDegrees(Math.atan((yDistanceToTarget()+camOffset)
				/xDistanceToTarget()));
	  } catch(Exception e) {
	    DriverStation.reportError("VISION BROKEN ANGLE TO TARGET", false);
	    return 0.0;
	  }
	 }
  
  private Loop _loop = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop() {
      
      if(frontValuesRequested) {
        //set your values
        frontValuesRequested = false;
      }
      
      if(backValuesRequested) {
        //set your values
        backValuesRequested = false;
      }
      
    }

    @Override
    public void onEnd() {
    }
    
  };
  
  public Loop getVisionLoop() {
    return _loop;
  }
  
  public synchronized double[] requestFrontValues() {
    frontValuesRequested = true;
    return frontCamValues;
  }
  
  public synchronized double[] requestRearValues() {
    backValuesRequested = true;
    return backCamValues;
  }
}
