package org.usfirst.frc.team2590.navigation;

public class ConstantVelocityController {

  private double kP;
  private double newKF;
  private double powerOffset;
   
  public ConstantVelocityController( double kF , double kP) {
    
    this.newKF = 0;
    this.kP = kP;
     
  }
  
  public void setSetpoint(double displacement , boolean isHighGear) {
    
    if(isHighGear) {
      newKF = 0.0975;
      powerOffset = 0.3535;
    } else {
      newKF = 0.0525;
      powerOffset = 0.3601;
    }
  }
 
  
  public double calculate(double desiredPos , double myVelocity , double currentEnc) {
    if(Math.abs(myVelocity) > 0.01) {     
      return (newKF*myVelocity+powerOffset) + (kP*(desiredPos-currentEnc));
    }  
    return 0;
    
    
  }
  

}
