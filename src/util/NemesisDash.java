package util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NemesisDash {
  
  private String index;
  
  public NemesisDash(String index) {
    this.index = index; 
  }
  
  public void updateAdd(double amountToAdd) {
     double curr = SmartDashboard.getNumber(index , 0);
     SmartDashboard.putNumber(index, curr+amountToAdd); 
  }
  
  public void updateSubtract(double amountToSubtract) {
    double curr = SmartDashboard.getNumber(index , 0);
    SmartDashboard.putNumber(index, curr-amountToSubtract); 
  }
  
  public double getNumber() {
    return SmartDashboard.getNumber(index , 0);
  }
  
}
