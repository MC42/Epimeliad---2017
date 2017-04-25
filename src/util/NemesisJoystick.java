package util;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Smart joystick for smart people
 * @author Connor_Hofenbitzer
 */
public class NemesisJoystick extends Joystick {

  double xdeadband;
  double ydeadband;
  boolean[] lastRisingVal = new boolean[10];
  boolean[] lastFallingVal = new boolean[10];

  public NemesisJoystick(int port , double xdeadband , double ydeadband) {
    super(port);
    this.xdeadband = xdeadband;
    this.ydeadband = ydeadband;
  }

  /**
   * button being released
   * @param button : the button clicked
   * @return if it is being released
   */
  public boolean getRisingEdge(int button) {
    boolean cur = this.getRawButton(button);
    boolean ret = !lastRisingVal[button] && cur;
    lastRisingVal[button] = cur;
    return ret;
  }

  /**
   * button being pressed
   * @param button : the button being pressed
   * @return : if it is being clicked
   */
  public boolean getFallingEdge(int button) {
    boolean cur = this.getRawButton(button);
    boolean ret = lastFallingVal[button] && !cur;
    lastFallingVal[button] = cur;
    return ret;
  }

  /**
   * gets the x axis value after being constrained to the xdead band
   * @return x axis
   */
  public double getXValue() {
    return constrain(getX() , xdeadband);
  }

  /**
   * gets the y axis value after being constrained to the ydeadband
   * @return y axis
   */
  public double getYValue() {
    return constrain(getY(), ydeadband);
  }

  /**
   * deadband Constrains a value to the other value
   * @param current : value before constrained
   * @param constrainTo : constrain to this range (positive this, negitive this)
   * @return constrain
   */
  private double constrain(double current , double constrainTo) {
    return (Math.abs(current) < constrainTo) ? 0.0 : current;
  }

}
