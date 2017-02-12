package util;

import org.usfirst.frc.team2590.looper.Loop;

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
