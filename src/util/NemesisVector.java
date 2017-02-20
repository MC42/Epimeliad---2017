package util;

public class NemesisVector {

  double startX;
  double startY;
  double magnitude;
  
  public NemesisVector(double x , double y , double mag) {
    startX = x;
    startY = y;
    magnitude = mag;
  }
  
  public double getX() {
    return startX;
  }
  
  public double getY() {
    return startY;
  }
  
  public double getMagnitude() {
    return magnitude;
  }
  
}
