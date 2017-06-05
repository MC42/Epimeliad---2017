package util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Enhanced dashboard input
 * @author Connor_Hofenbitzer
 *
 */
public class NemesisDash {
  
  private String index;
  
  public NemesisDash(String index) {
    this.index = index; 
  }
  
  /**
   * adds an amount to the input 
   * @param amountToAdd : amount to add to the input
   */
  public void updateAdd(double amountToAdd) {
     double curr = SmartDashboard.getNumber(index , 0);
     SmartDashboard.putNumber(index, curr+amountToAdd); 
  }

  /**
   * Get the number from the dashboard 
   * @return : dashboard imput number
   */
  public double getNumber() {
    return SmartDashboard.getNumber(index , 0);
  }
  
}
