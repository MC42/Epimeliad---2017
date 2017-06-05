package util;

/**
 * Basically a glorified 2D array
 * @author Connor_Hofenbitzer
 *
 */
public class DualSignal {

  private double leftSignal;
  private double rightSignal;

  public DualSignal(double left , double right) {
    this.leftSignal = left;
    this.rightSignal = right;
  }

  public void updateSignal(double left , double right) {
    this.leftSignal = left;
    this.rightSignal = right;
  }

  public double[] getSignals() {
    return new double[] {
        leftSignal , rightSignal
    };
  }

  public static final DualSignal FULL = new DualSignal(1, 1);
  public static final DualSignal DEAD = new DualSignal(0, 0);

}




