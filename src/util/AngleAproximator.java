package util;

import edu.wpi.first.wpilibj.AnalogAccelerometer;

public class AngleAproximator {

  private AnalogAccelerometer staticAccelerometer;
  private AnalogAccelerometer movingAccelerometer;
  
  //will this ever change? moon game?
  private final static double GRAVITY = 9.8;
  
  private double Nb; //load factors
  private double Nd;
  private double lastAd;
  private double lastAb;
  private double lastAbFiltered;
  private double lastAdFiltered;
  private double filteringConstant;

  public AngleAproximator(AnalogAccelerometer staticAccelerometer, AnalogAccelerometer movingAccelerometer , double filteringConstant) {
    
    //accelerometers
    this.staticAccelerometer = staticAccelerometer;
    this.movingAccelerometer = movingAccelerometer;
    
    //variables
    this.Nb = 0.0;
    this.Nd = 0.0;
    this.lastAd = 0.0;
    this.lastAb = 0.0;
    this.lastAbFiltered = 0.0;
    this.lastAdFiltered = 0.0;
    this.filteringConstant = filteringConstant;
   
  }
   //ad = gcostheta - absintheta
  private double calculateTheta() {
    return Math.sqrt((Nb*Nb) + 2*(1-Nd))-Nb; 
  }
  
  private double filterAd() {
    double filteredAd = (1-filteringConstant)*lastAd + (filteringConstant*lastAdFiltered);
    lastAd = movingAccelerometer.getAcceleration();
    lastAdFiltered = filteredAd;
    return filteredAd;
  }
  
  private double filterAb() {
    double filteredAb = (1-filteringConstant)*lastAb + (filteringConstant*lastAbFiltered);
    lastAb = staticAccelerometer.getAcceleration();
    lastAbFiltered = filteredAb;
    return filteredAb;
  }
  
}
