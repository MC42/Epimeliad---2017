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
	
	static int imageHeight = 240;
	static int imageWidth = 320;
	static int fov = 47;
	static double focalLength = imageWidth/(2*Math.tan(fov/2));
	static double camHeight = 19.5;
	
	
	
	
	
	
	static double[] defaultVal = new double[0];
	
	static Target target1 = new Target();
	static Target target2 = new Target();
	static Target BoundBox = new Target();
	
	public static void update(){
		
		double[] xVals = contours.getNumberArray("centerX",defaultVal); 
		double[] yVals = contours.getNumberArray("centerY",defaultVal); 
		
			
		int targetsSeen = contours.getNumberArray("centerX", defaultVal).length;
			
		if(targetsSeen>=2){
			
			target1.setCoord(xVals[0], yVals[0]);	
			target2.setCoord(xVals[1], yVals[1]);
		
			BoundBox.setCoord((target1.getX()+target2.getX())/2, (target1.getY()+target2.getY())/2);
		}
		
	}
	
	static double cx = 274;
	static double cy = imageHeight/2-.5;
	static double dpp = fov/imageWidth;
	
	public static double hAngleToTarget(){
		try{
			return((BoundBox.getX()-cx)*dpp);
			//return Math.atan((targetX-cx)/focalLength);
		}
		catch(Exception e){ 
			DriverStation.reportError("Target Not Found!", false);
			return 0;
		}
	}
	
	public static double vAngleToTarget(){
		try{
			return((BoundBox.getY()-(imageHeight/2))*dpp);
			//return Math.atan((BoundBox.getY()-cy)/focalLength);
		}
		catch(Exception e){
			DriverStation.reportError("Target Not Found", false);
			return 0;
		}
	}
	
	static double targetHeight = 13.5 ;
	static double camOffset = 9;
	
	public static double xDistanceToTarget(){
		return targetHeight/Math.tan(vAngleToTarget());
	}
	
	public static double yDistanceToTarget(){
		return xDistanceToTarget()/Math.tan(hAngleToTarget());
	}
	
	public static double angleToTarget(){
	  try {
	      update();
	      double gyroHeading = Robot.driveT.getGyroHeading();
		return Math.atan2((yDistanceToTarget()-(camOffset*Math.cos(gyroHeading))),(xDistanceToTarget()-(camOffset*Math.sin(gyroHeading))));
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
